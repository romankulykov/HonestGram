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

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class ChatAvailablePresenter extends BasePresenterImpl<ChatAvailableMvp.View> implements ChatAvailableMvp.Presenter {

    private long lastDialogId ;
    private long lastDialogId2 ;

    public ChatAvailablePresenter(ChatAvailableMvp.View view) {
        super(view);
        RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference,
                dataSnapshot -> {
                    // do your own mapping here
                    /*List<Dialogs> dialogsIdsList = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                        messageMap = (HashMap<String, Object>) data.getValue();
                        dialogsIdsList.add(Utility.toDialog(messageMap));
                    }*/
                    return dataSnapshot.getChildren();
                }).toFlowable()
                .flatMapIterable(dataSnapshots -> dataSnapshots)
                .map(dataSnapshot -> Utility.toDialog2(dataSnapshot.getValue()).getDialog_id())
                .toList()
                .subscribe(list -> {
                    lastDialogId2 = Collections.max(list);
                    // process author value
                });
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Long> dialogsIdsList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                    messageMap = (HashMap<String, Object>) data.getValue();
                    dialogsIdsList.add(Utility.toDialog(messageMap).getDialog_id());
                }
                if (!dialogsIdsList.isEmpty())lastDialogId = Collections.max(dialogsIdsList);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDialogsReference.addValueEventListener(valueEventListener);
    }

    @Override
    public void getAvailableContacts(List<List<Dialogs>> dialogs) {
        mUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDialogsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshotDialogs) {
                        List<Users> users = new ArrayList();
                        List<Long> idsCoincidence = new ArrayList();
                        Users currentUser = PreferencesData.INSTANCE.getUser();
                        for (DataSnapshot dataDialogs:dataSnapshotDialogs.getChildren()) {
                            Map<String, Object> messageDial = new LinkedHashMap<String, Object>();
                            messageDial = (HashMap<String, Object>) dataDialogs.getValue();
                            Dialogs dialogs = Utility.toDialog(messageDial);
                        }
                        for (DataSnapshot data: dataSnapshot.getChildren()) {
                            Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                            messageMap = (HashMap<String, Object>) data.getValue();
                            Users user = Utility.toUser(messageMap);
                            HashSet<Long> usersAnother = Utility.removeListsDuplicates(dialogs,currentUser.getId());
                            if (currentUser!=null && user.getId()!=currentUser.getId() && !usersAnother.contains(user.getId())) {
                                users.add(user);
                            }
                        }
                        //PreferencesData.INSTANCE.saveUsers((ArrayList)users);
                        if (mView!=null)mView.showUsers(users);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void createDialog(Users user, Users selectedUser) {
        Dialogs dialogs = new Dialogs(lastDialogId+1,"",0,0,user.getId());
        Dialogs dialogsSelected = new Dialogs(lastDialogId+1,"",0,0,selectedUser.getId());
        mDialogsReference.push().setValue(dialogs);
        mDialogsReference.push().setValue(dialogsSelected);
        mView.successCreate();
        //getAvailableContacts();

    }
}
