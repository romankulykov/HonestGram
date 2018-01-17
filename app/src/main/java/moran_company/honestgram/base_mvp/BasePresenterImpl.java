package moran_company.honestgram.base_mvp;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.utility.DebugUtility;
import retrofit2.HttpException;


/**
 * Created by Kulykov Anton on 9/8/17.
 */


public abstract class BasePresenterImpl<V extends BaseMvp.View> implements BaseMvp.Presenter, BaseMvp.InteractorFinishedListener {
    public static final String TAG = BasePresenterImpl.class.getName();
    protected V mView;


    private DatabaseReference mReference = FirebaseDatabase
            .getInstance()
            .getReference()
            .child("database");

    protected DatabaseReference mUsersReference = mReference.child("users");
    protected DatabaseReference mDialogsReference = mReference.child("dialogs");
    protected DatabaseReference mGoodsReference = mReference.child("goods");

    protected StorageReference storageReference = FirebaseStorage.getInstance().getReference();

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
