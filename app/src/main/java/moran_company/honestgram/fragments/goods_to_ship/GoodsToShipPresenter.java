package moran_company.honestgram.fragments.goods_to_ship;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Random;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.CustomLocation;
import moran_company.honestgram.data.Orders;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 14.01.2018.
 */

public class GoodsToShipPresenter extends BasePresenterImpl<GoodsToShipMvp.View> implements GoodsToShipMvp.Presenter {

    private long lastDialogId;

    public GoodsToShipPresenter(GoodsToShipMvp.View view) {
        super(view);

    }

    @Override
    public void getOrders() {
        RxFirebaseDatabase.observeSingleValueEvent(mOrdersReference, DataSnapshotMapper.listOf(Orders.class))
                .subscribeOn(Schedulers.newThread())
                .toFlowable()
                .flatMapIterable(orders -> orders)
                .filter(orders -> !orders.isShipped())
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orders -> {
                    if (isExistsView()) mView.showOrders(orders);
                }, this::onError);


    }

    @Override
    public void shipOrder(LatLng latLng, Orders order, String commentText, String mPath) {
        //Chats chat = new Chats(lastDialogId+1,user.getId(),selectedUser.getId(),new ArrayList<>());
        // mChatsReference.push().setValue(chat);
        order.setShipped(true);
        order.setLocation(new CustomLocation((float) latLng.longitude, (float) latLng.latitude));
        //TODO check not selected image
        Uri filePathUri = Uri.fromFile(new File(mPath));
        Users oldUser = PreferencesData.INSTANCE.getUser();

        // TODO comment
        order.setTimestampShipped(System.currentTimeMillis());

        String nameFile = oldUser.getNickname() + "_photoPlaceId_" + order.getId() + "_time_" + System.currentTimeMillis();

        RxFirebaseStorage.putFile(mStorageReference.child(nameFile), filePathUri).toFlowable()
                .zipWith(apiClient.getKeyById(order.getId(), mOrdersReference), (taskSnapshot, key) -> {
                    order.setPhotoPlace(taskSnapshot.getDownloadUrl().toString());
                    /*for (int i = 1; i < 2000; i++) {
                        float leftLimit = 36F;
                        float rightLimit = 49F;
                        float generatedFloat = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
                        float generatedFloat2 = leftLimit + new Random().nextFloat() * (rightLimit - leftLimit);
                        order.setId(order.getId()+i);
                        order.setLocation(new CustomLocation(generatedFloat,generatedFloat2));
                        mOrdersReference.push().setValue(order);
                    }*/
                    return mOrdersReference.child(key).setValue(order).isSuccessful();
                })
                .subscribe(aBoolean -> {
                    mView.successShipped();

                }, this::onError);

    }
}
