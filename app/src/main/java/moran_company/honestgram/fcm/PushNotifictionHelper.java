package moran_company.honestgram.fcm;


import android.os.AsyncTask;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import io.reactivex.subscribers.DisposableSubscriber;
import moran_company.honestgram.data.NotificationBody;
import moran_company.honestgram.data.SenderNotification;
import moran_company.honestgram.utility.ApiClient;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static moran_company.honestgram.utility.FirebaseContants.API_URL_FCM;
import static moran_company.honestgram.utility.FirebaseContants.AUTH_KEY_FCM;

/**
 * Created by roman on 15.01.2018.
 */

public class PushNotifictionHelper {

    public static String sendPushNotification(String deviceToken,String message)
            throws IOException {


        ApiClient apiClient = ApiClient.getInstance();
        SenderNotification senderNotification = new SenderNotification(deviceToken,new NotificationBody(message),1);
        apiClient.notify(senderNotification)
                .subscribe(new DisposableSubscriber<Object>() {
                    @Override
                    public void onNext(Object response) {

                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return "";
    }

    /*public static void another(String deviceToken){
        try {
            HttpURLConnection httpcon = (HttpURLConnection) ((new URL("https://fcm.googleapis.com/fcm/send").openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Authorization", "key="+deviceToken);
            httpcon.setRequestMethod("POST");
            httpcon.connect();
            System.out.println("Connected!");

            byte[] outputBytes = "{\"notification\":{\"title\": \"My title\", \"text\": \"My text\", \"sound\": \"default\"}, \"to\": \"fUesj2YgB0A:APA91bGDfQHUdmJAjcWPElO7lIBl_eVu7U0dbnjhplnqS0NclPwzyafoi-T1z9YzVEoZGc-nsrKsBp9X0cQcv7fzp38RlS3tGyANgNL9YWi9gS6_BqMvcdvzVNE69_0Ez48QO9QmWAK4\"}".getBytes("UTF-8");
            OutputStream os = httpcon.getOutputStream();
            os.write(outputBytes);
            os.close();

            // Reading response
            InputStream input = httpcon.getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
                for (String line; (line = reader.readLine()) != null;) {
                    System.out.println(line);
                }
            }

            System.out.println("Http POST request sent!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

  /*  static class RetrieveFeedTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... deviceToken) {
            String result = "";
            URL url = null;
            try {
                url = new URL(API_URL_FCM);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            try {
                conn.setRequestMethod("POST");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setRequestProperty("Authorization", "key=" + AUTH_KEY_FCM);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            try {
                json.put("to", deviceToken[0].trim());
                JSONObject info = new JSONObject();
                info.put("title", "notification title"); // Notification title
                info.put("body", "message body"); // Notification
                // body
                json.put("notification", info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OutputStreamWriter wr = new OutputStreamWriter(
                        conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
                result = "SUCCESS";
            } catch (Exception e) {
                e.printStackTrace();
                result = "FALSE";
            }
            System.out.println("GCM Notification is sent successfully");

            return result;
        }

        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }*/

}