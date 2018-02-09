package moran_company.honestgram.fragments.chat_available;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class ChatAvailablePresenter extends BasePresenterImpl<ChatAvailableMvp.View> implements ChatAvailableMvp.Presenter {

    private long lastDialogId;
    private long lastDialogId2;

    public ChatAvailablePresenter(ChatAvailableMvp.View view) {
        super(view);
        /*RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference, DataSnapshot::getChildren).toFlowable()
                .flatMapIterable(dataSnapshots -> dataSnapshots)
                .map(dataSnapshot -> Utility.toDialog2(dataSnapshot.getValue()).getDialog_id())
                .toList()
                .subscribe(list -> {
                    if (!list.isEmpty()) lastDialogId = Collections.max(list);
                    // process author value
                });
        // process postcomment list item*/
        /*RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference, DataSnapshotMapper.listOf(Dialogs.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .map(Dialogs::getDialog_id)
                .toList()
                .subscribe(list -> {
                    if (!list.isEmpty()) lastDialogId = Collections.max(list);
                }); */
        RxFirebaseDatabase.observeSingleValueEvent(mChatsReference, DataSnapshotMapper.listOf(Chats.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .map(chats -> chats.getId())
                .toList()
                .subscribe(list -> {
                    if (!list.isEmpty()) lastDialogId = Collections.max(list);
                });


    }

    @Override
    public void getAvailableContacts(List<Chats> dialogs) {
        RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable()
                .flatMapIterable(users -> users)
                .filter(userInList -> {
                    Users currentUser = PreferencesData.INSTANCE.getUser();
                    HashSet<Long> usersAnother = Utility.removeListsDuplicates(dialogs, currentUser.getId());

                    //return userInList.getId()!=currentUser.getId() && !usersAnother.contains(userInList.getId());
                    return Utility.isConcreteUserExistInLIst(dialogs,userInList.getId());
                    /*RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference,DataSnapshotMapper.listOf(Chats.class))
                            .toFlowable()
                            .flatMapIterable(chats -> chats)
                            .filter()*/

                })
                .toList()
                .subscribe(users -> {
                    if (isExistsView()) mView.showUsers(users);
                });

        /*Flowable<Users> usersFlowable = RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable().flatMapIterable(users1 -> users1).map(users1 -> users1);

        Flowable<Chats> chatFlowable = RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference,DataSnapshotMapper.listOf(Chats.class))
                .toFlowable()
                .flatMapIterable(chats -> chats)
                .map(chat -> chat);

        Flowable.zip(usersFlowable,chatFlowable,(users, chats) -> {
            if  (users.getId() != chats.getOwnerId() && users.getId() != chats.getCompanionId())
                return users;
        })

                .flatMapIterable(users -> users)
                .filter(user -> user.getId()!=chats.getCompanionId() && user.getId() != chats.getOwnerId())

        RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference,DataSnapshotMapper.listOf(Chats.class))
                .toFlowable()
                .flatMapIterable(chats -> chats)
                .flatMap(chats -> )
                .toList()
                .subscribe(users -> {
                    if (isExistsView()) {
                        mView.showUsers(users);
                    }
                });
*/


    }

    @Override
    public void createDialog(Users user, Users selectedUser) {
/*        Dialogs dialogs = new Dialogs(lastDialogId + 1, "", 0, 0, user.getId());
        Dialogs dialogsSelected = new Dialogs(lastDialogId + 1, "", 0, 0, selectedUser.getId());
        mDialogsReference.push().setValue(dialogs);
        mDialogsReference.push().setValue(dialogsSelected);
        mView.successCreate();*/
        Dialogs dialogsSelected = new Dialogs(lastDialogId + 1, "", 0, 0, selectedUser.getId(),"");
        Chats chat = new Chats(lastDialogId+1,user.getId(),selectedUser.getId(),new ArrayList<>());
        mChatsReference.push().setValue(chat);
        mView.successCreate();
        //getAvailableContacts();

    }
}
