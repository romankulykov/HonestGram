package moran_company.honestgram.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.R;
import moran_company.honestgram.data.CustomLocation;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.Utility;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * Created by roman on 19.01.2018.
 */

public class LocationService extends Service {


    private static final String TAG = LocationService.class.getName();
    private DatabaseReference mDatabaseReference = FirebaseDatabase
            .getInstance()
            .getReference()
            .child("database");

    private DatabaseReference mUsersReference = mDatabaseReference.child("users");
    private DatabaseReference mUnregistersUsersReference = mDatabaseReference.child("unregisters");

    private CustomLocation coordinatesToPush;
    private CustomLocation lastId;


    public LocationService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ReactiveLocationProvider reactiveLocationProvider = new ReactiveLocationProvider(HonestApplication.getInstance().getApplicationContext());
        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(5)
                .setInterval(5);
        Users user = PreferencesData.INSTANCE.getUser();


        reactiveLocationProvider.getLastKnownLocation()
                //.repeatWhen(objectObservable -> objectObservable.delay(11, TimeUnit.SECONDS))
                .observeOn(Schedulers.newThread())
                //.delay(30,)
                //.repeat(10000)
                //.timeout(1600,TimeUnit.MILLISECONDS)
                //.repeatWhen(objectObservable -> objectObservable.delay(10, TimeUnit.SECONDS))
                .map(location -> {
                    float latitude = (float) location.getLatitude();
                    float longtitude = (float) location.getLongitude();
                    return new CustomLocation(latitude, longtitude);
                }).toFlowable(BackpressureStrategy.LATEST)
                .flatMap(customLocation -> getUser(user),
                        (customLocation, usersDataSnapshotPair) -> {
                            usersDataSnapshotPair.first.setLocation(customLocation);
                            return usersDataSnapshotPair;
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(usersDataSnapshotPair -> {
                    Users userConcr = usersDataSnapshotPair.first;
                    DataSnapshot dataSnapshot = usersDataSnapshotPair.second;
                    Log.d(TAG,"datasnap = "+dataSnapshot.toString());
                    Log.d(TAG,"userConcr = "+userConcr.toString());
                    if (userConcr.getId() != -1) dataSnapshot.getRef().setValue(userConcr);
                }, this::onError, () -> {

                });

        return Service.START_STICKY;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {


    }

    protected Flowable<Pair<Users, DataSnapshot>> getUser(Users oldUser) {

        return RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshot::getChildren)
                .toFlowable()
                .flatMapIterable(dataSnapshots -> dataSnapshots)
                .map(dataSnapshot -> Pair.create(Utility.toUser2(dataSnapshot.getValue()), dataSnapshot))
                .filter(pairUserDatasnap -> pairUserDatasnap.first.getId() == (oldUser != null ? oldUser.getId() : -1))
                .toList().toFlowable()
                .flatMap(pairs -> {
                    Pair<Users, DataSnapshot> dataSnapshotPair = null;
                    if (pairs.isEmpty()) {
                        //TODO registerUnregister
                        return registerUnregisterUser();
                    } else {
                        dataSnapshotPair = pairs.get(0);
                        return Flowable.just(dataSnapshotPair);
                    }
                })
                //.flatMapIterable(pairs -> pairs)
                .map(usersDataSnapshotPair -> usersDataSnapshotPair)
                .map(pairUserData -> pairUserData);
    }

    private DataSnapshot mDataSnapshot;

    public Flowable<Pair<Users, DataSnapshot>> registerUnregisterUser() {

        return RxFirebaseDatabase.observeSingleValueEvent(mUnregistersUsersReference, DataSnapshot::getChildren)
                .toFlowable()
                .flatMapIterable(dataSnapshots -> dataSnapshots)
                .map(dataSnapshot -> {
                    mDataSnapshot = dataSnapshot;
                    return dataSnapshot;
                }).toList()
                /*.map(user -> user.getId())
                .toList()
                .map(Collections::max)*/.toFlowable()
                .map(dataSnapshotList -> {
                    Users user = new Users
                            (-1, "",
                                    "", "time" + System.currentTimeMillis(),
                                    "noname", "",
                                    "", "",
                                    FirebaseInstanceId.getInstance().getToken(), new CustomLocation(0f, 0f));
                    mUnregistersUsersReference.push().setValue(user);
                    return Pair.create(user, dataSnapshotList.get(0));
                });
    }

    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }


}
