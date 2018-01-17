package moran_company.honestgram.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Collection;

import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.utility.Utility;

/**
 * Created by valer on 26.09.2017.
 */

public class HonestFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Collection<String> data1 = remoteMessage.getData().values();
        Object[] data = remoteMessage.getData().values().toArray();
        if (data.length != 0)
            Utility.showNotification(HonestApplication.getInstance(), data[0].toString());
    }
}

