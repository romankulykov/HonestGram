package moran_company.honestgram.fragments.dialogs;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Dialogs;
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
        //TODO why mView become a null
        if (users != null && mView!=null) {
            mDialogsReference
                    .orderByChild("user_id")
                    .equalTo(users.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Dialogs> dialogsList = new ArrayList<>();
                            List<Dialogs> dialogsListFirst = new ArrayList<>();
                            List<List<Dialogs>> listDialogsList = new ArrayList<>();

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                                messageMap = (HashMap<String, Object>) data.getValue();
                                Dialogs dialog = Utility.toDialog(messageMap);
                                dialogsListFirst.add(dialog);
                            }
                            dialogsList = Utility.removeDuplicates(dialogsListFirst);

                            if (!dialogsList.isEmpty()) {
                                long dialogId = dialogsList.get(0).getDialog_id();
                                for (int i = 0; i < dialogsList.size(); i++) {
                                    mDialogsReference
                                            .orderByChild("dialog_id")
                                            .equalTo(dialogsList.get(i).getDialog_id())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    List<Dialogs> dialogsList = new ArrayList<>();
                                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                        Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                                                        messageMap = (HashMap<String, Object>) data.getValue();
                                                        dialogsList.add(Utility.toDialog(messageMap));
                                                    }
                                                    //listDialogsList.add(dialogsList);
                                                    if (mView!=null)mView.addDialog(dialogsList);
                                                }


                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                }
                                //mView.showDialogs(listDialogsList);
                            } else {
                                mView.showDialogs(listDialogsList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
}
