package moran_company.honestgram.utility;

import org.json.JSONObject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.data.SenderNotification;
import moran_company.honestgram.fcm.PushNotifictionHelper;
import retrofit2.Response;

/**
 * Created by roman on 15.01.2018.
 */

public class ApiClient {
    private ApiService mApiService = RetrofitUtils.getInstance().getApiService();
    private static ApiClient instance;

    public static ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    public Flowable<Object> notify(SenderNotification jsonObject) {
        return mApiService.sendNotification("key="+FirebaseContants.AUTH_KEY_FCM,jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
