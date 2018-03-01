package moran_company.honestgram.utility;

import org.json.JSONObject;

import io.reactivex.Flowable;
import kotlinx.coroutines.experimental.Deferred;
import moran_company.honestgram.data.SenderNotification;
import moran_company.honestgram.fcm.PushNotifictionHelper;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by roman on 15.01.2018.
 */

public interface ApiService {


    @POST(FirebaseContants.API_URL_FCM)
    Flowable<Object> sendNotification(@Header("Authorization") String authorization, @Body SenderNotification json);


    //Call<Object> sendNotificationCoroutine(@Header("Authorization") String authorization, @Body SenderNotification json);

}
