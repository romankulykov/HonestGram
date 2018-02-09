package moran_company.honestgram.fragments.dialogs;

import java.util.Collections;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class DialogsPresenter extends BasePresenterImpl<DialogsMvp.View> implements DialogsMvp.Presenter {

    public DialogsPresenter(DialogsMvp.View view) {
        super(view);
    }

    @Override
    public void loadMessages() {
        Users users = PreferencesData.INSTANCE.getUser();
        /*RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference, DataSnapshotMapper.listOf(Dialogs.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .filter(dialogs -> dialogs.getUser_id() == users.getId())
                .toList()
                .map(Utility::removeDuplicates).toFlowable()
                .flatMapIterable(dialogs -> dialogs)
                .flatMap(dialogs -> RxFirebaseDatabase
                        .observeSingleValueEvent(mDialogsReference, DataSnapshotMapper.listOf(Dialogs.class))
                        .toFlowable()
                        .flatMapIterable(dialogs1 -> dialogs1)
                        .filter(dialogs1 -> dialogs1.getDialog_id() == dialogs.getDialog_id())
                        .toList().toFlowable()
                )
                .subscribe(list -> {
                    if (!list.isEmpty() && isExistsView()) mView.addDialog(list);
                });*/

        RxFirebaseDatabase.observeSingleValueEvent(mChatsReference, DataSnapshotMapper.listOf(Chats.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .filter(dialogs -> dialogs.getCompanionId() == users.getId() || dialogs.getOwnerId() == users.getId())
                .toList()
                .subscribe(list -> {
                    if (!list.isEmpty() && isExistsView()) {
                        for (int i = 0; i < list.size(); i++) {
                            Collections.sort(list.get(i).getDialogs(), Utility.comparatorMessages);
                        }
                        Collections.sort(list, Utility.comparatorDialogs);
                        mView.showDialogs(list);
                    }
                });
    }
}
