package moran_company.honestgram;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import moran_company.honestgram.adapters.MenuAdapter;
import moran_company.honestgram.data.ItemMenu;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.utility.DebugUtility;

/**
 * Created by roman on 11.01.2018.
 */

public final class HonestApplication extends Application {

    private static final String TAG = HonestApplication.class.getName();

    private static HonestApplication sInstance;

    public static HonestApplication getInstance() {
        return sInstance;
    }

    private MenuAdapter mMenuAdapter;


    public static boolean hasNetwork() {
        boolean hasNetwork = sInstance.checkIfHasNetwork();
        DebugUtility.logTest(TAG, "hasNetwork " + hasNetwork);
        return hasNetwork;
    }

    private boolean checkIfHasNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initDrawerAdapter() {
        List<ItemMenu> menu = new ArrayList<>();
        if (PreferencesData.INSTANCE.getUser() != null)
            menu.add(new ItemMenu(R.string.menu_profile, android.R.color.white, ItemMenu.MENU_TYPE.PROFILE));
        else
            menu.add(new ItemMenu(R.string.menu_authorization, android.R.color.white, ItemMenu.MENU_TYPE.LOGIN));

        menu.add(new ItemMenu(R.string.menu_main_page, android.R.color.white, ItemMenu.MENU_TYPE.MAIN));
        menu.add(new ItemMenu(R.string.menu_map, android.R.color.white, ItemMenu.MENU_TYPE.MAP));
        menu.add(new ItemMenu(R.string.product, android.R.color.white, ItemMenu.MENU_TYPE.PRODUCTS));
        menu.add(new ItemMenu(R.string.cities_and_hoods, android.R.color.white, ItemMenu.MENU_TYPE.CITIES_AND_HOODS));
        menu.add(new ItemMenu(R.string.share, android.R.color.white, ItemMenu.MENU_TYPE.SHARE));
        menu.add(new ItemMenu(R.string.about, android.R.color.white, ItemMenu.MENU_TYPE.ABOUT));

        mMenuAdapter = new MenuAdapter(R.layout.list_item_menu);
        mMenuAdapter.setItems(menu);
    }

    public MenuAdapter getMenuAdapter() {
        if (mMenuAdapter == null)
            initDrawerAdapter();
        return mMenuAdapter;
    }


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        FirebaseApp.initializeApp(this);

        sInstance = this;
        FlowManager.init(new FlowConfig.Builder(this).build());
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateProfile(Object updateProfileEvent) {
        DebugUtility.logTest(TAG, "onUpdateProfile");
      /*  User mUser = updateProfileEvent.getUser();
        if (mUser == null)
            return;*/
        initDrawerAdapter();


    }
}
