package moran_company.honestgram.fragments.product_detail;

import java.util.ArrayList;
import java.util.Collections;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 26.01.2018.
 */

public class ProductDetailPresenter extends BasePresenterImpl<ProductDetailMvp.View> implements ProductDetailMvp.Presenter {
    private long lastDialogId;

    public ProductDetailPresenter(moran_company.honestgram.fragments.product_detail.ProductDetailMvp.View view) {
        super(view);
        RxFirebaseDatabase.observeSingleValueEvent(mChatsReference, DataSnapshotMapper.listOf(Chats.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .map(chats -> chats.getId())
                .toList()
                .subscribe(list -> {
                    if (!list.isEmpty()) lastDialogId = Collections.max(list);
                });
    }

    @Override
    public void checkChat(Goods good, String message) {
        long productId = good.getId();
        long ownerId = good.getOwnerId();
        Users user = PreferencesData.INSTANCE.getUser();

        apiClient.getChatsByProductId(productId,user.getId())
                .subscribe(chats -> {
                    if (chats.getId()==-1) {
                        Dialogs dialog = new Dialogs(lastDialogId + 1, message, 1, System.currentTimeMillis(), user.getId(),"");
                        Chats chat = new Chats(productId, lastDialogId + 1, user.getId(), ownerId, Collections.singletonList(dialog));
                        mChatsReference.push().setValue(chat);
                        mView.showChat(chat);
                    } else {
                        ArrayList<Long> ids = new ArrayList<>();
                        for (int i = 0; i < chats.getDialogs().size(); i++) {
                            ids.add(chats.getDialogs().get(i).getMessage_id());
                        }
                        long lastMessageId = Collections.max(ids);
                        Dialogs dialog = new Dialogs(chats.getId(), message, lastMessageId + 1, System.currentTimeMillis(), user.getId(),"");
                        chats.getDialogs().add(dialog);
                        apiClient.getKeyById(chats.getId(), mChatsReference)
                                .subscribe(key -> {
                                    mChatsReference.child(key).setValue(chats);
                                    mView.showChat(chats);
                                });
                    }
                });
    }
}
