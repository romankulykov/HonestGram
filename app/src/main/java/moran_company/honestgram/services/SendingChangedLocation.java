package moran_company.honestgram.services;

import android.location.Location;
import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.data.CustomLocation;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.ApiClient;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 20.01.2018.
 */

public class SendingChangedLocation {

    private static final String TAG = SendingChangedLocation.class.getName();
    private DatabaseReference mDatabaseReference = FirebaseDatabase
            .getInstance()
            .getReference()
            .child("database");

    private DatabaseReference mUsersReference = mDatabaseReference.child("users");
    private DatabaseReference mUnregistersUsersReference = mDatabaseReference.child("unregisters");

    private ApiClient apiClient = ApiClient.getInstance();

    private Users user;

    public SendingChangedLocation(Users user) {
        this.user = user;
    }

    public void sendLocation(Location/*CustomLocation*/ newLocation) {
        if (newLocation.getLongitude() == 0f && newLocation.getLatitude() == 0f)
            return;
        Users user = PreferencesData.INSTANCE.getUser();
        Log.d(TAG, "----------------------------------");
        Log.d(TAG, user != null ? user.toString() : "User null");
        Log.d(TAG, "----------------------------------");
        Log.d(TAG, newLocation.toString());
        Flowable.just(newLocation)
                .map(location -> {
                    float latitude = (float) location.getLatitude();
                    float longitude = (float) location.getLongitude();
                    return new CustomLocation(longitude, latitude);
                })
                .flatMap(customLocation -> getUser(user, customLocation),
                        (customLocation, newUser) -> {
                            if (newUser != null) {
                                newUser.setLocation(customLocation);
                                Log.d(TAG, "after flatMap USer = " + newUser.getLocation().toString());
                            }
                            return newUser;
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(newUser -> {
                    Log.d(TAG, "userConcr = " + newUser.toString());
                    if (newUser != null)
                        if (newUser.getId() != -1) {
                            setNewCoordinates(newUser, mUsersReference, newUser.getLocation());
                        }
                }, this::onError, () -> {

                });
    }

  /*  private Flowable<Pair<Users, DataSnapshot>> getUser(Users oldUser, CustomLocation customLocation) {

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
                        return registerUnregisterUser(customLocation);
                    } else {
                        dataSnapshotPair = pairs.get(0);
                        return Flowable.just(dataSnapshotPair);
                    }
                });
    }*/

    private Flowable<Users> getUser(Users oldUser, CustomLocation customLocation) {
        return RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable()
                .flatMapIterable(usersList -> usersList)
                .filter(users -> users.getId() == (oldUser != null ? oldUser.getId() : -1))
                //.map(dataSnapshot -> Pair.create(Utility.toUser2(dataSnapshot.getValue()), dataSnapshot))
                //.filter(pairUserDatasnap -> pairUserDatasnap.first.getId() == (oldUser != null ? oldUser.getId() : -1))
                .toList().toFlowable()
                .flatMap(pairs -> {
                    if (pairs.isEmpty()) {
                        //TODO registerUnregister
                        return registerUnregisterUser(customLocation);
                    } else {
                        return Flowable.just(pairs.get(0));
                    }
                });
    }

    private DataSnapshot mDataSnapshot;

    private Flowable<Users> registerUnregisterUser(CustomLocation customLocation) {

        return RxFirebaseDatabase.observeSingleValueEvent(mUnregistersUsersReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable()
                .flatMapIterable(dataSnapshots -> dataSnapshots)
                .map(Users::getId).toList()
                .map(Collections::max).toFlowable()
                .map(lastId -> {
                    Users user = new Users();
                    if (PreferencesData.INSTANCE.getUserUnregister() == null) {
                        user = new Users
                                (lastId + 1, "",
                                        "", "time" + System.currentTimeMillis(),
                                        android.os.Build.MODEL, "",
                                        "", "",
                                        FirebaseInstanceId.getInstance().getToken(), customLocation);
                        mUnregistersUsersReference.push().setValue(user);
                        PreferencesData.INSTANCE.saveUserUnregister(user);
                    } else {
                        long id = PreferencesData.INSTANCE.getUserUnregister().getId();
                        Log.d(TAG, "id = " + id);

                        Flowable.zip(apiClient.getLocationsById(id, mUnregistersUsersReference), apiClient.getKeyById(id, mUnregistersUsersReference),
                                (customLocations, key) -> {
                                    Log.d(TAG, "Flowable.zip");
                                    long lastIdLocations = Utility.getLastLocationId(customLocations);
                                    CustomLocation location = new CustomLocation(lastIdLocations + 1, System.currentTimeMillis(), customLocation.getLongitude(), customLocation.getLatitude());
                                    customLocations.add(location);
                                    return Pair.create(customLocations, key);
                                }).subscribe(arrayListStringPair -> {
                            Log.d(TAG, "subscribe");
                            ArrayList<CustomLocation> customLocations = arrayListStringPair.first;
                            String key = arrayListStringPair.second;
                            mUnregistersUsersReference.child(key).child("locations").setValue(customLocations);
                        });
                    }
                    // Костыль
                    user.setId(-1);
                    return user;
                });
    }

    private void setNewCoordinates(Users users, DatabaseReference databaseReference, CustomLocation customLocation) {
        long id = users.getId();
        Log.d(TAG, "id = " + id);
        Flowable.zip(apiClient.getLocationsById(id, databaseReference), apiClient.getKeyById(id, databaseReference),
                (customLocations, key) -> {
                    Log.d(TAG, "Flowable.zip");
                    long lastIdLocations = Utility.getLastLocationId(customLocations);
                    CustomLocation location = new CustomLocation(lastIdLocations + 1, System.currentTimeMillis(), customLocation.getLongitude(), customLocation.getLatitude());
                    customLocations.add(location);
                    return Pair.create(customLocations, key);
                }).subscribe(arrayListStringPair -> {
            Log.d(TAG, "subscribe");
            ArrayList<CustomLocation> customLocations = arrayListStringPair.first;
            String key = arrayListStringPair.second;
            databaseReference.child(key).child("location").setValue(customLocation);
            databaseReference.child(key).child("locations").setValue(customLocations);
        });
    }

    // Задачи :
    // Сделать Id незарегистрированного юзера
    // СОхранить ключ - идентификатор  и затем обновлять по ключу

    // По ID получить ключ чтобы добавлять в конкретную запись

    // Получить список locations из id чтобы добавлять новые координаты


    private void onError(Throwable throwable) {
        Utility.showToast(HonestApplication.getInstance().getApplicationContext(), throwable.toString());
    }


}
