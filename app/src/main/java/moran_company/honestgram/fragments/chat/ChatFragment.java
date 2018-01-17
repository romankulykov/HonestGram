package moran_company.honestgram.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import moran_company.honestgram.R;
import moran_company.honestgram.adapters.ChatAdapter;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.fragments.base.BaseMvpFragment;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

@FragmentWithArgs
public class ChatFragment extends BaseMvpFragment<ChatMvp.Presenter> implements ChatMvp.View {

    @BindView(R.id.chat)
    RecyclerView chat;
    @BindView(R.id.newMessageEditText)
    EditText newMessageEditText;

    @Arg(bundler = ParcelerArgsBundler.class)
    public List<Dialogs> dialogsList;

    private ChatAdapter chatAdapter = new ChatAdapter();

    @Override
    protected ChatMvp.Presenter createPresenter() {
        return new ChatPresenter(this, dialogsList.get(0).getDialog_id());
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat_detail;
    }

    public static ChatFragment newInstance(List<Dialogs> dialogs) {
        ChatFragment fragment = new ChatFragmentBuilder(dialogs).build();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chat.setAdapter(chatAdapter);
        showDialogs(dialogsList);
        //mPresenter.loadMessages();
    }

    @Override
    public void showDialogs(List<Dialogs> dialogsList) {
        if (dialogsList != null) {
            this.dialogsList = dialogsList;
            chatAdapter.setItems(dialogsList);
            newMessageEditText.setText("");
            chat.getLayoutManager().scrollToPosition(chatAdapter.getItemCount() - 1);
        }
    }

    @OnClick(R.id.sendMessage)
    void sendMessage() {
        List<Long> msgIds = new ArrayList<>();
        long maxDialogId;
        for (int i = 0; i < dialogsList.size(); i++) {
            msgIds.add(dialogsList.get(i).getMessage_id());
        }
        maxDialogId = Collections.max(msgIds);
        String message = newMessageEditText.getText().toString();
        if (!message.isEmpty())
            mPresenter.sendMessage(message, dialogsList.get(0).getDialog_id(), maxDialogId);
        else showToast(R.string.empty_fields);
    }
}
