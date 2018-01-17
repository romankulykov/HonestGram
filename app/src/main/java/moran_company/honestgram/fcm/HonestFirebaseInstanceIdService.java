package moran_company.honestgram.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by valer on 26.09.2017.
 */

public class HonestFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //TODO se tnew Token
        // ApiClient.getInstance().setToken();

    }
}