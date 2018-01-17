package moran_company.honestgram.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import moran_company.honestgram.fcm.PushNotifictionHelper;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Dmitry Dashkevich on 06.10.2016.
 */
public class RetrofitUtils {
    // public static final String DOMAIN_URL = "http://devsp6.isate.kz";

    private static final String TAG = RetrofitUtils.class.getName();

    public static final String API_BASE_URL = "_layouts/15/RESTService.ashx/";
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static RetrofitUtils instance;
    private ApiService apiService;



    private RetrofitUtils() {
        apiService = provideRetrofit().create(ApiService.class);
    }

    public static RetrofitUtils getInstance() {
        if (instance == null) instance = new RetrofitUtils();
        return instance;
    }

    public static Retrofit provideRetrofit() {

        return new Retrofit.Builder()
                .baseUrl(FirebaseContants.API_URL_FCM+"/")
                .client(provideOkHttpClient())
               // .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }



    public static OkHttpClient provideOkHttpClient() {
        return provideOkHttpBuilder().build();
    }

    private static OkHttpClient.Builder provideOkHttpBuilder() {
             return new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(provideHttpLoggingInterceptor());
    }

    public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    public static Gson getGson() {
        return new GsonBuilder()

                .create();
    }

    public ApiService getApiService() {
        return apiService;
    }
}
