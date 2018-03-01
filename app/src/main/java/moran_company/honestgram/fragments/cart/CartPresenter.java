package moran_company.honestgram.fragments.cart;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.CustomLocation;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.Orders;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 24.01.2018.
 */

public class CartPresenter extends BasePresenterImpl<CartMvp.View> implements CartMvp.Presenter {

    private long lastId = 0;

    public CartPresenter(CartMvp.View view) {
        super(view);
        RxFirebaseDatabase.observeSingleValueEvent(mOrdersReference, DataSnapshotMapper.listOf(Orders.class))
                .toFlowable()
                .flatMapIterable(ordersList -> ordersList)
                .map(orders -> {
                    Log.d(TAG, orders.toString());
                    return orders.getId();
                })
                .toList()
                .subscribe(ordersIds -> {
                            Log.d(TAG, ordersIds.toString());
                            if (!ordersIds.isEmpty()) lastId = Collections.max(ordersIds);
                        }
                        , this::onError);
    }

    @Override
    public void remove(long id) {
        long idUser = PreferencesData.INSTANCE.getUser().getId();
        /*Query applesQuery = mUsersReference.orderByChild("id").equalTo(idUser);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                Log.d(TAG, "Success deleted in web");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });*/

        apiClient.getKeyById(idUser, mUsersReference)
                .flatMap(key -> {
                    ArrayList<Goods> goods = PreferencesData.INSTANCE.getUser().getCartList();
                    goods.remove((int) id);

                    // mUsersReference.child(key).child("cart").child("" + id).removeValue();
                    mUsersReference.child(key).child("cart").setValue(new Gson().toJson(goods));
                    return apiClient.getUserById(idUser, mUsersReference);
                })
                .subscribe(users -> {
                    PreferencesData.INSTANCE.saveUser(users);
                    mView.successDelete(users);
                });


    }

    @Override
    public void getCart() {
        ArrayList<Goods> goods = PreferencesData.INSTANCE.getUser().getCartList();
        mView.showCart(goods);
    }

    @Override
    public void pushOrder(List<Goods> items, Context context) {
        /*mOrdersReference.push().setValue(new Orders())
                .addOnSuccessListener(aVoid -> {
                    //mView.successIssue(users);
                });*/
        long idSeller = items.get(0).getOwnerId();
        long idCustomer = PreferencesData.INSTANCE.getUser().getId();

        apiClient.getKeyById(idCustomer, mUsersReference)
                .zipWith(Flowable.zip(apiClient.getUserById(idCustomer, mUsersReference), apiClient.getBitmap(items, context),
                        Pair::create),
                        (key, pair) -> {
                            Users users = pair.first;
                            List<Bitmap> bitmapList = pair.second;
                            //users.getCartList().clear();
                            users.setCart(null);
                            mUsersReference.child(key).getRef().setValue(users);
                            PreferencesData.INSTANCE.saveUser(users);
                            return Pair.create(users, bitmapList);
                        })
                //.subscribeOn(Schedulers.newThread())
                .flatMap(pair -> {
                    Users users = pair.first;
                    List<Bitmap> listBitmap = pair.second;
                    Bitmap[] parts = new Bitmap[listBitmap.size()];
                    for (int i = 0; i < listBitmap.size(); i++) {
                        parts[i] = listBitmap.get(i);
                    }

                    Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 2, parts[0].getHeight() * 2, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint();
                    for (int i = 0; i < parts.length; i++) {
                        canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
                    }
                    Bitmap bitmap = result;
                    Uri filePathUri = getImageUri(context, bitmap);
                    String nameFile = users.getNickname() + "_orderId_" + lastId + 1 + "_time_" + System.currentTimeMillis();
                    return Flowable.zip(RxFirebaseStorage.putFile(mStorageReference.child(nameFile), filePathUri).toFlowable(), Flowable.just(users),
                            (taskSnapshot, users1) -> {
                                Orders order = new Orders(lastId + 1, idSeller, idCustomer, items,
                                        false, System.currentTimeMillis(), nameFile, taskSnapshot.getDownloadUrl().toString(), new CustomLocation());
                                return Pair.create(order, users1);
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    Users users = pair.second;
                    Orders order = pair.first;
                    mOrdersReference.push().setValue(order)
                            .addOnSuccessListener(aVoid -> {
                                mView.successIssue(users);
                            })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        onError(e.toString());
                    });
                }, this::onError);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
