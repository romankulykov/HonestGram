package moran_company.honestgram.base_mvp;


import android.util.Pair;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.ApiClient;
import moran_company.honestgram.utility.DebugUtility;
import moran_company.honestgram.utility.Utility;
import retrofit2.HttpException;


/**
 * Created by Kulykov Anton on 9/8/17.
 */


public abstract class BasePresenterImpl<V extends BaseMvp.View> implements BaseMvp.Presenter, BaseMvp.InteractorFinishedListener {
    public static final String TAG = BasePresenterImpl.class.getName();
    protected V mView;


    protected ApiClient apiClient = ApiClient.getInstance();

    private DatabaseReference mReference = FirebaseDatabase
            .getInstance()
            .getReference()
            .child("database");

    protected DatabaseReference mUsersReference = mReference.child("users");
    protected DatabaseReference mDialogsReference = mReference.child("dialogs");
    protected DatabaseReference mGoodsReference = mReference.child("Goods");
    protected DatabaseReference mCitiesReference = mReference.child("cities");

    protected Flowable<Pair<Pair<Users,DataSnapshot>,UploadTask.TaskSnapshot>> getUser(UploadTask.TaskSnapshot taskSnapshot, Users oldUser) {

    return RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshot::getChildren)
                .toFlowable()
                .flatMapIterable(dataSnapshots -> dataSnapshots)
                .map(dataSnapshot -> Pair.create(Utility.toUser2(dataSnapshot.getValue()), dataSnapshot))
                .filter(pairUserDatasnap -> pairUserDatasnap.first.getId() == oldUser.getId())
                .map(pairUserData -> Pair.create(pairUserData, taskSnapshot));
    }

    protected StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    //private ApiClient apiClient = ApiClient.getInstance();

    public BasePresenterImpl(V view) {
        this.mView = view;
    }

    //public ApiClient getApiClient() {return apiClient;}

    protected boolean isExistsView() {
        return mView != null;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String text) {
        DebugUtility.logTest(TAG, "onError " + text);
        if (mView != null) {
            mView.setProgressIndicator(false);
            mView.showToast(text);
        }
    }

    @Override
    public void onError(int textId) {
        DebugUtility.logTest(TAG, "onError " + textId);
        if (mView != null) {
            mView.setProgressIndicator(false);
            mView.showToast(textId);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable != null) {
           /* if (throwable instanceof ApiException) {
                ApiException apiException = (ApiException) throwable;
                if (apiException.isUnauthorized())
                {
                    onUnauthorized();
                    return;
                }
            }
            else */
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.code() == 401) {
                    onUnauthorized();
                    return;
                }
            }
            if (throwable.getMessage().contains("No address associated") && !HonestApplication.hasNetwork()) {
                onNetworkDisable();
                return;
            }
            onError(throwable.getMessage());
        }

    }

    @Override
    public void onComplete() {
        setProgressIndicator(false);
    }


    @Override
    public void setProgressIndicator(boolean active) {
        if (mView != null)
            mView.setProgressIndicator(active);
    }


    @Override
    public void onUnauthorized() {
        if (mView != null) {
            mView.setProgressIndicator(false);
            mView.onUnauthorized();
        }
    }

    @Override
    public void onNetworkDisable() {
        if (mView != null) {
            mView.setProgressIndicator(false);
            mView.onNetworkDisable();
        }
    }


}
