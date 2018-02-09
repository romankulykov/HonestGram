package moran_company.honestgram.fragments.chat_available;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import moran_company.honestgram.R;
import moran_company.honestgram.adapters.AvailableContactsAdapter;
import moran_company.honestgram.adapters.BaseAdapter;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.eventbus.UpdateDialogs;
import moran_company.honestgram.fragments.base.BaseDialogFragment;
import moran_company.honestgram.utility.DialogUtility;

/**
 * Created by roman on 14.01.2018.
 */

@FragmentWithArgs
public class ChatAvailableFragment extends BaseDialogFragment<ChatAvailableMvp.Presenter> implements ChatAvailableMvp.View {


    @BindView(R.id.contacts)
    RecyclerView contacts;
    private AvailableContactsAdapter availableContactsAdapter = new AvailableContactsAdapter();

    @Arg(bundler = ParcelerArgsBundler.class)
    List<Chats> dialogs;

    @Override
    protected ChatAvailableMvp.Presenter createPresenter() {
        return new ChatAvailablePresenter(this);
    }

    public static ChatAvailableFragment newInstance(List<Chats> dialogs) {
        ChatAvailableFragment fragment = new ChatAvailableFragmentBuilder(dialogs).build();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_available_contacts, null);
        ButterKnife.bind(this, v);
        Dialog dialog = DialogUtility
                .getAlertDialogWithoutButtons(getContext(),v,getString(R.string.attention),getString(R.string.choose_companion));

        contacts.setAdapter(availableContactsAdapter);
        availableContactsAdapter.setOnItemClickListener((itemView, selectedUser) -> {
            mPresenter.createDialog(PreferencesData.INSTANCE.getUser(),selectedUser);
            //mBaseActivity.showChatFragment();
        });
        mPresenter.getAvailableContacts(dialogs);

        return dialog;
    }

    @Override
    public void successCreate() {
        //mBaseActivity.showChatFragment();
        EventBus.getDefault().post(new UpdateDialogs(true));

        dismiss();
    }

    @Override
    public void showUsers(List<Users> usersList) {
        if (usersList!=null && !usersList.isEmpty())
            availableContactsAdapter.setItems(usersList);
    }
}
