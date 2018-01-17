package moran_company.honestgram.fragments.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import moran_company.honestgram.R;
import moran_company.honestgram.adapters.ChatAdapter;
import moran_company.honestgram.adapters.DialogsAdapter;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.eventbus.UpdateDialogs;
import moran_company.honestgram.fragments.base.BaseMvpFragment;
import moran_company.honestgram.utility.DebugUtility;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class DialogsFragment extends BaseMvpFragment<DialogsMvp.Presenter> implements DialogsMvp.View {

    private static final String TAG = DialogsFragment.class.getName();
    private List<List<Dialogs>> listDialogsList = new ArrayList<>();

    @BindView(R.id.chat)
    RecyclerView chat;

    private MenuItem addDialog;

    public DialogsAdapter chatAdapter = new DialogsAdapter();

    @Override
    protected DialogsMvp.Presenter createPresenter() {
        return new DialogsPresenter(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_chat;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chat.setAdapter(chatAdapter);
        listDialogsList.clear();
        chatAdapter.setOnItemClickListener((itemView, item) -> {
            mBaseActivity.showChatFragment(item);
        });
        mPresenter.loadMessages();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateProfile(UpdateDialogs update) {
        DebugUtility.logTest(TAG, "onUpdateProfile");
        if (update != null)
            if (update.isUpdate()) {
                listDialogsList.clear();
                mPresenter.loadMessages();
            }
          /*  if ((updateProfileEvent.checkLogin() == null && !(this instanceof MainActivity)))
                finish();*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        addDialog = menu.add(R.string.add_dialog)
                .setIcon(android.R.drawable.btn_plus)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == addDialog) {
            mBaseActivity.showAvailableContacts(listDialogsList);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showDialogs(List<List<Dialogs>> dialogsList) {
        if (dialogsList != null && !dialogsList.isEmpty())
            dialogsList.get(0);
    }

    @Override
    public void addDialog(List<Dialogs> dialogs) {
        if (dialogs != null && !dialogs.isEmpty())
            listDialogsList.add(dialogs);
        Collections.sort(dialogs, Utility.comparator);
        chatAdapter.setItems(listDialogsList);
        ArrayList<ArrayList<Dialogs>> d = new ArrayList<>();
        for (List<Dialogs> dial : listDialogsList) {
            d.add((ArrayList) dial);
        }
        PreferencesData.INSTANCE.saveUserDialogs((ArrayList) listDialogsList);
        //PreferencesData.INSTANCE.saveUserDialogs();
    }
}
