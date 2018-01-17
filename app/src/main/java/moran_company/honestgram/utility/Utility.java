package moran_company.honestgram.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.MainActivity;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.Users;

/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class Utility {
    private final static String TAG = Utility.class.getName();

    public static void showToast(Context context, int textId) {
        String text;
        try {
            text = context.getString(textId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        showToast(context, text);
    }

    public static Comparator<Dialogs> comparator = (left, right) -> {
        return (int)left.getMessage_id() - (int)right.getMessage_id(); // use your logic
    };

    public static Users getUserById(long idUser,ArrayList<Users> users){
        Users userConcr = null;
        for (Users user:users) {
            if (user.getId() == idUser)
                userConcr = user;
        }
        return userConcr;
    }

    public static Users toUser(Map<String, Object> stringWordsMap) {
        long id = (Long.valueOf(String.valueOf(stringWordsMap.get("id"))));
        String cityId = (String.valueOf(stringWordsMap.get("city")));
        String distr = (String.valueOf(stringWordsMap.get("district")));
        String nickname = (String.valueOf(stringWordsMap.get("nickname")));
        String login = (String.valueOf(stringWordsMap.get("login")));
        String password = (String.valueOf(stringWordsMap.get("password")));
        String photoName = (String.valueOf(stringWordsMap.get("photoName")));
        String photoURL = (String.valueOf(stringWordsMap.get("photoURL")));
        String token = (String.valueOf(stringWordsMap.get("token")));
        Users words = new Users(id, cityId, distr, nickname, login, password, photoName, photoURL,token);
        return words;

    }

    public static Dialogs toDialog(Map<String, Object> stringWordsMap) {
        long dialogId = (Long.valueOf(String.valueOf(stringWordsMap.get("dialog_id"))));
        String message = (String.valueOf(stringWordsMap.get("message")));
        long message_id = (Long.valueOf(String.valueOf(stringWordsMap.get("message_id"))));
        long timestamp = (Long.valueOf(String.valueOf(stringWordsMap.get("timestamp"))));
        long userId = (Long.valueOf(String.valueOf(stringWordsMap.get("user_id"))));
        Dialogs dialogs = new Dialogs(dialogId, message, message_id, timestamp, userId);
        return dialogs;
    }

    public static Dialogs toDialog2(Object objectMap) {
        Map<String, Object> stringWordsMap = new LinkedHashMap<String, Object>();
        stringWordsMap = (Map<String, Object>) objectMap;
        //Map<String, Object> stringWordsMap = (HashMap<String, Object>)objectMap;
        long dialogId = (Long.valueOf(String.valueOf(stringWordsMap.get("dialog_id"))));
        String message = (String.valueOf(stringWordsMap.get("message")));
        long message_id = (Long.valueOf(String.valueOf(stringWordsMap.get("message_id"))));
        long timestamp = (Long.valueOf(String.valueOf(stringWordsMap.get("timestamp"))));
        long userId = (Long.valueOf(String.valueOf(stringWordsMap.get("user_id"))));
        Dialogs dialogs = new Dialogs(dialogId, message, message_id, timestamp, userId);
        return dialogs;
    }

    public static void showNotification(Context context, String text) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"1")//
                .setContentTitle(context.getString(R.string.app_name))//
                .setContentText(text)//
                //.setDefaults(Notification.DEFAULT_ALL)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))//
                .setSmallIcon(R.mipmap.ic_launcher)//
                .setTicker(context.getString(R.string.app_name))//
                .setContentIntent(startActivityPendingIntent)//
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }

    public static ArrayList<Dialogs> removeDuplicates(List<Dialogs> list) {

        // Store unique items in result.
        ArrayList<Dialogs> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<Long> set = new HashSet<>();

        // Loop over argument list.
        for (Dialogs item : list) {
            // If String is not in set, add it to the list and the set.
            if (!set.contains(item.getDialog_id())) {
                result.add(item);
                set.add(item.getDialog_id());
            }
        }
        return result;
    }

    public static HashSet<Long> removeListsDuplicates(List<List<Dialogs>> list,long idUser) {
        HashSet<Long>dialIdCoincidence = new HashSet<>();
        HashSet<Long>idsAnotherUsers = new HashSet<>();
        for (int i = 0; i <list.size() ; i++) {
            for (int j = 0; j <list.get(i).size() ; j++) {
                if (list.get(i).get(j).getUser_id()==idUser){
                    dialIdCoincidence.add(list.get(i).get(j).getDialog_id());
                    break;
                }
            }
        }
        for (int i = 0; i <list.size() ; i++) {
            for (int j = 0; j <list.get(i).size() ; j++) {
                if (dialIdCoincidence.contains(list.get(i).get(j).getDialog_id())){
                    if (list.get(i).get(j).getUser_id()!=idUser)
                        idsAnotherUsers.add(list.get(i).get(j).getUser_id());
                }

            }
        }
      return idsAnotherUsers;
    }


    public static boolean pingURL(String url) {
        url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(1000);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }

    public static void setColorDrawable(View view, String color) {
        Drawable backgroundDrawable = DrawableCompat.wrap(view.getBackground()).mutate();
        DrawableCompat.setTint(backgroundDrawable, Color.parseColor(color));
    }

    public static void showToast(Context context, String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public static float translateContainer(View view, float moveFactor, float lastTranslate) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setTranslationX(moveFactor);
            return 0;
        } else {
            TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
            anim.setDuration(0);
            anim.setFillAfter(true);
            view.startAnimation(anim);
            return moveFactor;
        }
    }


    public static boolean checkLocationPermissions(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    public static float pxToDp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float dpToPx(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) return true;
        }

        return false;
    }

    public static Spanned getSpannedPlurals(Context context, int pluralsId, int count) {
        String string = context.getResources().getQuantityString(pluralsId, count, count);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= 24)
            spanned = Html.fromHtml(string, 0);
        else
            spanned = Html.fromHtml(string);
        return spanned;
    }

    public static Spanned getSpannedString(Context context, int stringId, String value) {
        String string = context.getString(stringId, value);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= 24)
            spanned = Html.fromHtml(string, 0);
        else
            spanned = Html.fromHtml(string);
        return spanned;
    }

    public static Spanned getSpannedString(Context context, int stringId) {
        String string = context.getString(stringId);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= 24)
            spanned = Html.fromHtml(string, 0);
        else
            spanned = Html.fromHtml(string);
        return spanned;
    }

    public static Point getDisplaySize() {
        Context context = HonestApplication.getInstance();
        Point point = null;
        if (Build.VERSION.SDK_INT >= 13) {
            point = getDisplaySizeAFTER13(context);
        } else {
            point = getDisplaySizeBEFORE13(context);
        }
        DebugUtility.logTest(TAG, "DisplaySize x = " + point.x + " y = " + point.y);
        return point;
    }

    @SuppressWarnings("deprecation")
    private static Point getDisplaySizeBEFORE13(Context context) {
        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();
        return new Point(display.getWidth(), display.getHeight());

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private static Point getDisplaySizeAFTER13(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static Spanned fromHtml(String html) {
        if (html == null || html.isEmpty())
            return null;
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static String dateToDateString(Date date) {
        if (date == null) return "";
        return DateFormat.getDateInstance(DateFormat.LONG).format(date);
    }

    public static String dateToMonthAndDayString(Context context, Date date) {
        if (context == null || date == null) return "";
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
        return DateUtils.formatDateTime(context, date.getTime(), flags);
    }

    public static String dateToTimeString(Date date) {
        if (date == null) return "";
        return DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
    }

    public static void callPhone(Context context, String phone) {

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        context.startActivity(intent);
    }

    public static void openUrl(Context context, String url) {
        if (!url.contains("http://"))
            url = "http://" + url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }


    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
