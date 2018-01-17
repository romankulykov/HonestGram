package moran_company.honestgram.fragments.chat;

import android.net.Uri;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.ImageUploadInfo;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fcm.PushNotifictionHelper;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class ChatPresenter extends BasePresenterImpl<ChatMvp.View> implements ChatMvp.Presenter {

    private long lastDialogId ;
    private long currentDialogId ;
    private long idAnotherUser ;

    public ChatPresenter(ChatMvp.View view,long dialogId) {
        super(view);
        this.currentDialogId = dialogId;
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Long> dialogsIdsList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                    messageMap = (HashMap<String, Object>) data.getValue();
                    dialogsIdsList.add(Utility.toDialog(messageMap).getDialog_id());
                }
                lastDialogId = Collections.max(dialogsIdsList);
                if (mView!=null) loadMessages(currentDialogId);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDialogsReference.addValueEventListener(valueEventListener);
    }


    @Override
    public void loadMessages(long dialogId) {
        Users users = PreferencesData.INSTANCE.getUser();
        currentDialogId = dialogId;
        if (users != null) {
            mDialogsReference
                    .orderByChild("user_id")
                    .equalTo(users.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Dialogs> dialogsList = new ArrayList<>();
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                                messageMap = (HashMap<String, Object>) data.getValue();
                                dialogsList.add(Utility.toDialog(messageMap));
                            }
                            if (!dialogsList.isEmpty()) {
                                mDialogsReference
                                        .orderByChild("dialog_id")
                                        .equalTo(dialogId)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                List<Dialogs> dialogsList = new ArrayList<>();
                                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                    Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                                                    messageMap = (HashMap<String, Object>) data.getValue();
                                                    dialogsList.add(Utility.toDialog(messageMap));
                                                }
                                                Collections.sort(dialogsList, Utility.comparator);
                                                for (int i = 0; i <dialogsList.size() ; i++) {
                                                    if (users.getId()!=dialogsList.get(i).getUser_id())
                                                        idAnotherUser = dialogsList.get(i).getUser_id();
                                                }
                                                mView.showDialogs(dialogsList);
                                            }


                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                            }else {
                                mView.showDialogs(dialogsList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override
    public void sendMessage(String s,long dialogId,long messageId) {
        mDialogsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Dialogs> dialogsList = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Map<String, Object> messageMap = new LinkedHashMap<String, Object>();
                    messageMap = (HashMap<String, Object>) data.getValue();
                    dialogsList.add(Utility.toDialog(messageMap));
                }
                //TODO msg_id
                Dialogs dialogs = new Dialogs(dialogId,s,messageId+1,System.currentTimeMillis(),PreferencesData.INSTANCE.getUser().getId());
                mDialogsReference.push().setValue(dialogs);
                try {
                    PushNotifictionHelper.sendPushNotification(Utility.getUserById(idAnotherUser,PreferencesData.INSTANCE.getUsers()).getToken(),s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadMessages(dialogId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
