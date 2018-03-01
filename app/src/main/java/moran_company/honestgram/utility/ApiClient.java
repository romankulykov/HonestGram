package moran_company.honestgram.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import durdinapps.rxfirebase2.RxFirebaseQuery;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.CustomLocation;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.SenderNotification;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fcm.PushNotifictionHelper;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by roman on 15.01.2018.
 */

public class ApiClient {
    private ApiService mApiService = RetrofitUtils.getInstance().getApiService();
    private static ApiClient instance;

    private DatabaseReference mReference = FirebaseDatabase
            .getInstance()
            .getReference()
            .child("database");

    protected DatabaseReference mUsersReference = mReference.child("users");
    protected DatabaseReference mDialogsReference = mReference.child("dialogs");
    protected DatabaseReference mChatsReference = mReference.child("chats");
    protected DatabaseReference mGoodsReference = mReference.child("Goods");
    protected DatabaseReference mCitiesReference = mReference.child("cities");

    public static ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    public Flowable<Object> notify(SenderNotification jsonObject) {
        return mApiService.sendNotification("key=" + FirebaseContants.AUTH_KEY_FCM, jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<ArrayList<CustomLocation>> getLocationsById(long id, DatabaseReference databaseReference) {
        return RxFirebaseDatabase.observeSingleValueEvent(databaseReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable().flatMapIterable(usersList -> usersList)
                .filter(users -> users.getId() == id)
                .toList().toFlowable()
                .map(list -> {
                    ArrayList<CustomLocation> customLocations;
                    Users users = list.get(0);
                    customLocations = users.getLocations();
                    if (customLocations == null) customLocations = new ArrayList<>();
                    return customLocations;
                })
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Chats> getChatsByProductId(long productId, long userId) {
        return RxFirebaseDatabase.observeSingleValueEvent(mChatsReference, DataSnapshotMapper.listOf(Chats.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .filter(dialogs -> dialogs.getProductId() == productId && (dialogs.getOwnerId() == userId || dialogs.getCompanionId() == userId))
                .defaultIfEmpty(new Chats(-1));
    }

    public Flowable<String> getKeyById(long id, DatabaseReference databaseReference) {
        return RxFirebaseDatabase.observeSingleValueEvent(databaseReference, DataSnapshot::getChildren)
                .toFlowable()
                .flatMapIterable(dataSnapshots -> dataSnapshots)
                .filter(dataSnapshot -> Utility.toUser2(dataSnapshot.getValue()).getId() == id)
                .map(DataSnapshot::getKey)
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Users> getUserById(long id, DatabaseReference databaseReference) {
        return RxFirebaseDatabase.observeSingleValueEvent(databaseReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable().flatMapIterable(usersList -> usersList)
                .filter(users -> users.getId() == id)
                .toList().toFlowable()
                .map(list -> {
                    Users users = list.get(0);
                    return users;
                })
                .subscribeOn(Schedulers.io());
    }

    /*public Observable<Void> get(final String url) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(final Subscriber<? super Void> subscriber) {
                GlideApp
                        .with(context)
                        .load(url)
                        .downloadOnly(2000, 2000);

                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }*/


    public Flowable<List<Bitmap>> getBitmap(List<Goods> items, Context context) {
        return Flowable.just(items)
                .flatMapIterable(goods -> goods)
                .map(good -> {
                    FutureTarget<Bitmap> futureTarget = GlideApp.with(context)
                            .asBitmap()
                            .load(good.getUrl())
                            .into(250, 250);
                    return futureTarget.get();
                })
                .toList().toFlowable()
                .subscribeOn((Schedulers.io()));

    }


}
