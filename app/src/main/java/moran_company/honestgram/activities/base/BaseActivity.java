package moran_company.honestgram.activities.base;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.MainActivity;
import moran_company.honestgram.activities.ProductsActivity;
import moran_company.honestgram.activities.dialogs.DialogsActivity;
import moran_company.honestgram.activities.map.MapActivity;
import moran_company.honestgram.activities.profile.ProfileActivity;
import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.ItemMenu;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fragments.ZoomPhotoFragment;
import moran_company.honestgram.fragments.base.BaseFragment;
import moran_company.honestgram.fragments.chat.ChatFragment;
import moran_company.honestgram.fragments.chat_available.ChatAvailableFragment;
import moran_company.honestgram.fragments.dialogs.DialogsFragment;
import moran_company.honestgram.fragments.main.MainFragment;
import moran_company.honestgram.fragments.map.MapFragment;
import moran_company.honestgram.fragments.navigation_drawer.NavigationDrawerFragment;
import moran_company.honestgram.fragments.navigation_drawer_seller.NavigationDrawerFragmentSeller;
import moran_company.honestgram.fragments.product_detail.ProductDetailFragment;
import moran_company.honestgram.fragments.products.ProductsFragment;
import moran_company.honestgram.fragments.profile.ProfileFragment;
import moran_company.honestgram.services.MyService;
import moran_company.honestgram.utility.DebugUtility;
import moran_company.honestgram.utility.DialogUtility;
import moran_company.honestgram.utility.Utility;


/**
 * Created by Kulykov Anton on 9/8/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseMvp.View {
    private static final String TAG = BaseActivity.class.getName();
    private static final int LOCATION_PERMISSIONS_CODE = 1379;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public NavigationDrawerFragment mNavigationDrawerFragment;
    public NavigationDrawerFragmentSeller mNavigationDrawerFragmentSeller;
    @Nullable
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @Nullable
    @BindView(R.id.activityContainer)
    View mActivityContainer;
    @Nullable
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @Nullable
    @BindView(R.id.titleBar)
    TextView titleBar;

    @Nullable
    @BindView(R.id.secondToolbar)
    Toolbar secondToolbar;
    @Nullable
    @BindView(R.id.secondBarTitle)
    TextView secondBarTitle;
  /*  @Nullable
    @BindView(R.id.toolBarTitle)
    TextView mToolBarTitle;*/

    @Nullable
    @BindView(R.id.cart)
    RelativeLayout cart;
    @Nullable
    @BindView(R.id.basketCountTextView)
    TextView mBasketCounter;
    @Nullable
    @BindView(R.id.secondMenuBackImageView)
    ImageView mSecondMenuBackImageView;

    private Intent intent;

    private boolean open = false;

    private ItemMenu.MENU_TYPE mTypeMenu;


    private FragmentManager mCurrentFragmentManager;
    private Dialog mDialog;
    private long mBackPressed;
    private float lastTranslate = 0.0f;
    // private boolean rightSide = false;

    public static boolean newInstance(Context context, final Class<? extends AppCompatActivity> activityClass) {
        return newInstance(context, activityClass, false);
    }

    public static boolean newInstance(Context context, final Class<? extends AppCompatActivity> activityClass, boolean clearBackStack) {
       /* if (activityClass == context.getClass()) {
            return false;
        }*/

        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (clearBackStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityCompat.startActivity(context, intent, null);
        return true;
    }

    public static void showActivity(Context context, ItemMenu itemMenu) {
        DebugUtility.logTest(TAG, "showActivity " + itemMenu.getMenuType());
        switch (itemMenu.getMenuType()) {
           /* case LOGIN:
                BaseActivity.newInstance(context, LoginActivity.class, true);
                break;*/
            case PRODUCTS:
                BaseActivity.newInstance(context, ProductsActivity.class, false);
                break;
            case PROFILE:
                BaseActivity.newInstance(context, ProfileActivity.class, false);
                break;
            case MAIN:
                BaseActivity.newInstance(context, MainActivity.class, false);
                break;
            case MAP:
                BaseActivity.newInstance(context, MapActivity.class, false);
                //BaseActivity.newInstance(context, MapActivity.class, false);
                break;
            /*case COMPANIES:
                BaseActivity.newInstance(context, CompaniesActivity.class, false);
                break;
            case INFRASTRUCTURE:
                BaseActivity.newInstance(context, PlacesActivity.class, false);
                break;
            case EVENTS:
                BaseActivity.newInstance(context, EventsActivity.class, false);
                break;
            case BUSINESS_MEETINGS:
                BaseActivity.newInstance(context, BusinessMeetingsActivity.class, false);
                break;
            case ABOUT:
                BaseActivity.newInstance(context, AboutActivity.class, false);
                break;
            case FRIENDS:
                BaseActivity.newInstance(context, FriendsActivity.class, false);
                break;*/
            case CHATS:
                BaseActivity.newInstance(context, DialogsActivity.class, false);
                break;
            case NONE:
                break;
        }
    }

    public abstract int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutResId = getLayoutResId();
        if (layoutResId != 0) {
            setContentView(layoutResId);
            ButterKnife.bind(this);

        }
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);
            //get primary theme color
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int color = typedValue.data;
            mToolBar.setBackgroundColor(color);
        }
        mDialog = DialogUtility.getWaitDialog(this, this.getString(R.string.wait_loading), false);
        getCurrentFragmentManager();
        initNavigationDrawer();
        initNavigationDrawerSecond();
        if (!Utility.checkLocationPermissions(this)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, LOCATION_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSIONS_CODE) {
            boolean granted = false;
            for (int grantResult : grantResults)
                granted |= grantResult == PackageManager.PERMISSION_GRANTED;

            if (granted) {

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateProfile(Object updateProfileEvent) {
        DebugUtility.logTest(TAG, "onUpdateProfile");
          /*  if ((updateProfileEvent.checkLogin() == null && !(this instanceof MainActivity)))
                finish();*/
    }

    @Override
    protected void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBasketCounter(PreferencesData.INSTANCE.getUser());
        //EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {

        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        overridePendingTransition(0, 0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Optional
    @OnClick(R.id.cart)
    public void clickCart() {

    }

    @Optional
    @OnClick(R.id.secondMenuImageView)
    public void menuSeller() {
        open = !open;
        if (!mNavigationDrawerFragmentSeller.isDrawerOpen())
            ((DrawerLayout) findViewById(R.id.drawerLayout)).openDrawer(Gravity.RIGHT);
        else
            ((DrawerLayout) findViewById(R.id.drawerLayout)).closeDrawer(Gravity.RIGHT);
    }

    @Optional
    @OnClick(R.id.secondMenuBackImageView)
    public void hideSecond() {
        hideToolbarSecond();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard();
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

  /*  @Optional
    @OnTouch(R.id.activityContainer)
    boolean touch(View view, MotionEvent motionEvent) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        int width = 0;
        int height = 0;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                display.getSize(size);
                width = size.x;
                height = size.y;
                rightSide = motionEvent.getX()>width/2;
                return true;
            case MotionEvent.ACTION_UP:

                return true;
            case MotionEvent.ACTION_MOVE:
                display.getSize(size);
                width = size.x;
                height = size.y;
                rightSide = motionEvent.getX()>width/2;
                return true;
        }
        return false;
    }*/

    protected void initNavigationDrawer() {
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigationDrawer);
        if (mNavigationDrawerFragment == null)
            return;
        mNavigationDrawerFragment.setUp(
                R.id.navigationDrawer,
                mDrawerLayout, mToolBar);
        this.mNavigationDrawerFragment.setOnDrawerSlideListener(moveFactor -> {
            //if (!open)
            //moveFactor = rightSide?moveFactor:-moveFactor;
            //    lastTranslate = Utility.translateContainer(mActivityContainer, moveFactor, lastTranslate);
        });
        HonestApplication application = getHonestApplication();
        if (application.getMenuAdapter() != null)
            mNavigationDrawerFragment.setAdapter(application.getMenuAdapter());

    }


   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateNavigation(UpdateNavigation updateNavigation) {
        DebugUtility.logTest(TAG, "onUpdateProfile");
        if (updateNavigation != null) {
            //this.open = updateNavigation.isFoo();
        }
          *//*  if ((updateProfileEvent.checkLogin() == null && !(this instanceof MainActivity)))
                finish();*//*
    }*/

    protected void initNavigationDrawerSecond() {
        mNavigationDrawerFragmentSeller = (NavigationDrawerFragmentSeller)
                getSupportFragmentManager().findFragmentById(R.id.navigationDrawerSeller);
        if (mNavigationDrawerFragmentSeller == null)
            return;
        mNavigationDrawerFragmentSeller.setUp(
                R.id.navigationDrawerSeller,
                mDrawerLayout, secondToolbar);
        Users user = PreferencesData.INSTANCE.getUser();
        if (user != null && user.getStatusId() != null){
            if (user.getStatusId() != 1) {
                mNavigationDrawerFragmentSeller.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                hideToolbarSecond();
            }
        }else {
            mNavigationDrawerFragmentSeller.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            hideToolbarSecond();
        }
    }

    public FragmentManager getCurrentFragmentManager() {
        if (mCurrentFragmentManager == null)
            mCurrentFragmentManager = getSupportFragmentManager();
        return mCurrentFragmentManager;
    }

    public boolean checkFragment(String tag, boolean withVisible) {
        Fragment fragment = getCurrentFragmentManager().findFragmentByTag(tag);
        if (withVisible)
            return fragment != null && fragment.isVisible();
        else
            return fragment != null;
    }

    public Fragment getVisibleFragmentByTag(String tag) {
        Fragment fragment = getCurrentFragmentManager().findFragmentByTag(tag);
        return fragment != null && fragment.isVisible() ? fragment : null;
    }

    public Fragment getFragmentByTag(String tag) {
        Fragment fragment = getCurrentFragmentManager().findFragmentByTag(tag);
        return fragment != null ? fragment : null;
    }

    public void showDialog() {
        if (!isFinishing()) {
            if (mDialog != null) {
                hideDialog();
                mDialog.show();
            }
        }
    }

    public void hideDialog() {
        DialogUtility.closeDialog(mDialog);
    }

    @Override
    public void onNetworkDisable() {
        showToast(R.string.network_disable_error);
    }

    @Override
    public void onUnauthorized() {
//        Utility.showToast(this, R.string.unauthorized);
        //PreferencesData.resetProfile();
        //BaseActivity.newInstance(this, LoginActivity.class, true);
    }

    @Override
    public void showToast(String text) {
        runOnUiThread(() -> Utility.showToast(this, text));
    }

    @Override
    public void showToast(int textId) {
        runOnUiThread(() -> Utility.showToast(this, textId));
    }

    @Override
    public void setProgressIndicator(boolean active) {
        runOnUiThread(() -> {
            if (active)
                showDialog();
            else
                hideDialog();
        });
    }

    public boolean popBackStack(int count) {
        if (count != -1)
            for (int i = 0; i < count; i++)
                if (haveFragmentInBackStack())
                    mCurrentFragmentManager.popBackStack();
                else
                    break;
        else if (haveFragmentInBackStack())
            mCurrentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        return haveFragmentInBackStack();
    }

    public boolean haveFragmentInBackStack() {
        return mCurrentFragmentManager != null ? mCurrentFragmentManager.getBackStackEntryCount() > 0 : false;
    }

    public boolean haveActivityInBackStack() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(10);
        if (taskList.get(0).numActivities == 1 &&
                taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        BaseFragment currentFragment = (BaseFragment) getCurrentFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (currentFragment != null && !currentFragment.checkCanBackPressed())
            return;

        if (!haveFragmentInBackStack() && !haveActivityInBackStack()) {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
//                doBeforeDestroy();
                finish();
            } else
                Utility.showToast(getBaseContext(), R.string.go_exit);
            mBackPressed = System.currentTimeMillis();
        } else
            super.onBackPressed();
    }

    public void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    public void showMainFragment() {
        String tag = MainFragment.class.getName();
        MainFragment fragment = new MainFragment();
        showFragment(R.id.fragmentContainer, fragment, tag, false);
    }

    public void showZoomPhotoFragment(String url) {
        String tag = ZoomPhotoFragment.class.getName();
        ZoomPhotoFragment fragment = ZoomPhotoFragment.newInstance(url);
        showFragment(R.id.fragmentContainer, fragment, tag, true);
    }

    public void showProductDetailFragment(Goods good) {
        String tag = ProductDetailFragment.class.getName();
        ProductDetailFragment fragment = ProductDetailFragment.newInstance(good);
        showFragment(R.id.fragmentContainer, fragment, tag, false);
    }

    public void showProductsFragment() {
        String tag = ProductsFragment.class.getName();
        ProductsFragment fragment = new ProductsFragment();
        showFragment(R.id.fragmentContainer, fragment, tag, false);
    }

    public void showMapFragment() {
        String tag = MapFragment.class.getName();
        MapFragment fragment = new MapFragment();
        showFragment(R.id.fragmentContainer, fragment, tag, false);
    }

    public void showMapFragment(int statusId) {
        String tag = MapFragment.class.getName();
        MapFragment fragment = MapFragment.newInstance(statusId);
        showFragment(R.id.fragmentContainer, fragment, tag, false);
    }


    public void showProfileFragment() {
        String tag = ProfileFragment.class.getName();
        ProfileFragment fragment = new ProfileFragment();
        showFragment(R.id.fragmentContainer, fragment, tag, false);
    }

    public void showDialogsFragment() {
        String tag = DialogsFragment.class.getName();
        DialogsFragment fragment = new DialogsFragment();
        showFragment(R.id.fragmentContainer, fragment, tag, false);
    }

    public void showChatFragment(Chats chat) {
        String tag = ChatFragment.class.getName();
        ChatFragment fragment = ChatFragment.newInstance(chat);
        showFragment(R.id.fragmentContainer, fragment, tag, true);
    }

    public void showAvailableContacts(List<Chats> dialogs) {
        ChatAvailableFragment dialog = ChatAvailableFragment.newInstance(dialogs);
        dialog.show(getCurrentFragmentManager(), dialog.getTag());
    }


    public void showFragment(int container, Fragment fragment, String tag, boolean addToBackStack) {
        FragmentManager fragmentManager = getCurrentFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(container, fragment, tag);
        if (addToBackStack) ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }


    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null)
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

/*    public void showBackControl(boolean show) {
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setDisplayShowHomeEnabled(show);
    }*/

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
       /* super.setTitle(null);
        if (mToolBarTitle != null) mToolBarTitle.setText(title);*/
    }

    public void setTitleBar(CharSequence title) {
        super.setTitle(null);
        titleBar.setText(title);
    }

    public void setTitleBar(int title) {
        super.setTitle(null);
        titleBar.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
      /*  super.setTitle(null);
        if (mToolBarTitle != null) mToolBarTitle.setText(titleId);*/
    }

    public void showToolbar() {
        if (mToolBar != null) mToolBar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        if (mToolBar != null) mToolBar.setVisibility(View.GONE);
    }


    public void showToolbarSecond() {
        if (secondToolbar != null) secondToolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbarSecond() {
        if (secondToolbar != null) secondToolbar.setVisibility(View.GONE);
    }

    public HonestApplication getHonestApplication() {
        return (HonestApplication) getApplication();
    }


    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }

    public NavigationDrawerFragmentSeller getNavigationDrawerFragmentSeller() {
        return mNavigationDrawerFragmentSeller;
    }

    public void setBasketCounter(Users user) {
        if (user != null) {
            int count = user.getCart() == null ? 0 : user.getCart().size();
            if (mBasketCounter != null)
                if (count > 0) {
                    mBasketCounter.setVisibility(View.VISIBLE);
                    mBasketCounter.setText(Integer.toString(count));
                } else mBasketCounter.setVisibility(View.GONE);
        }
    }


    public void stopLocationService() {
        stopService(new Intent(this, MyService.class));
    }

    public void startLocationService() {
        Users users = PreferencesData.INSTANCE.getUser();
        intent = new Intent(this, MyService.class);
        intent.putExtra("users", (Parcelable) users);
        startService(new Intent(this, MyService.class));
    }

    public void restartLocationService() {
        stopLocationService();
        startLocationService();
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ItemMenu.MENU_TYPE getTypeMenu() {
        ItemMenu.MENU_TYPE menuType = ItemMenu.MENU_TYPE.NONE;
        String name = this.getClass().getName();
        if (ProductsActivity.TAG == name)
            menuType = ItemMenu.MENU_TYPE.PRODUCTS;
        if (MapActivity.TAG == name)
            menuType = ItemMenu.MENU_TYPE.MAP;
        return menuType;
    }
}
