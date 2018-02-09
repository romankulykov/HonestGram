package moran_company.honestgram.fragments.chat;

import android.net.Uri;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.Flowable;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fcm.PushNotifictionHelper;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class ChatPresenter extends BasePresenterImpl<ChatMvp.View> implements ChatMvp.Presenter {

    private long lastDialogId;
    private long currentDialogId;
    private long idAnotherUser;

    public ChatPresenter(ChatMvp.View view, long dialogId) {
        super(view);
        this.currentDialogId = dialogId;
        apiClient.getKeyById(currentDialogId, mChatsReference)
                .map(key -> RxFirebaseDatabase.observeValueEvent(mChatsReference.child(key),/*DataSnapshotMapper.listOf(Chats.class)*/Chats.class))
                .flatMap(dataSnapshotFlowable -> dataSnapshotFlowable)
                .subscribe(chat -> {
                    if (isExistsView()) {
                        loadMessages(currentDialogId);
                    }
                });

    }


    @Override
    public void loadMessages(long dialogId) {
        Users users = PreferencesData.INSTANCE.getUser();
        currentDialogId = dialogId;
        if (users != null) {
            RxFirebaseDatabase.observeSingleValueEvent(mChatsReference, DataSnapshotMapper.listOf(Chats.class))
                    .toFlowable().flatMapIterable(dialogs -> dialogs)
                    .filter(dialogs -> dialogs.getId() == dialogId)
                    .toList()
                    .subscribe(list -> {
                        if (!list.isEmpty() && isExistsView()) {
                            Collections.sort(list.get(0).getDialogs(), Utility.comparatorMessages);
                            mView.showDialogs(list.get(0).getDialogs());
                        }
                    });
        }
    }

    @Override
    public void sendMessage(String s, long dialogId, long messageId,String url) {
        RxFirebaseDatabase.observeSingleValueEvent(mChatsReference, DataSnapshotMapper.listOf(Chats.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .filter(dialogs -> dialogs.getId() == dialogId)
                .toList().toFlowable().flatMap(chats -> {
            Flowable<List<Chats>> chatsList = Flowable.just(chats);
            return Flowable.zip(chatsList, apiClient.getKeyById(currentDialogId, mChatsReference), Pair::create);
        })
                .subscribe(pair -> {
                    List<Chats> list = pair.first;
                    String key = pair.second;
                    if (!list.isEmpty() && isExistsView()) {
                        Dialogs dialogs = new Dialogs(dialogId, s, messageId + 1, System.currentTimeMillis(), PreferencesData.INSTANCE.getUser().getId(),url);
                        Chats chat = list.get(0);
                        List<Dialogs> dialogsArrayList = chat.getDialogs() == null ? new ArrayList<>() : chat.getDialogs();
                        dialogsArrayList.add(dialogs);
                        mChatsReference.child(key).child("dialogs").setValue(dialogsArrayList);
                        Users myUser = PreferencesData.INSTANCE.getUser();
                        if (myUser.getId() != chat.getOwnerId())
                            idAnotherUser = chat.getOwnerId();
                        if (myUser.getId() != chat.getCompanionId())
                            idAnotherUser = chat.getCompanionId();
                        try {
                            PushNotifictionHelper.sendPushNotification(Utility.getUserById(idAnotherUser, PreferencesData.INSTANCE.getUsers()).getToken(), s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (isExistsView()) {
                            mView.successSend();
                            loadMessages(dialogId);
                        };
                    }
                });
    }

    @Override
    public void attachImage(String path,long chatId) {
        Uri filePathUri = Uri.fromFile(new File(path));
        Users oldUser = PreferencesData.INSTANCE.getUser();
        // Необходимо : Прокинуть в storage новую картинку, затем сохранить ее урл иназвание,
        // получить нашего юзера и в него пропихнуть новые данные урла картинки и название и отправить в бд
        // и затем удалить старую фотографию
        String oldPhotoName = oldUser.getPhotoName();
        String nameFile = oldUser.getNickname() + "_chatId_"+chatId +"_time_"+ System.currentTimeMillis();
        RxFirebaseStorage.putFile(mStorageReference.child(nameFile), filePathUri)
                .subscribe(taskSnapshot -> {
                    mView.showUrlPhoto(taskSnapshot.getDownloadUrl().toString());
                },this::onError,this::onComplete);
    }


}
