package moran_company.honestgram.fragments.navigation_drawer_seller;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.AddProductActivity;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.adapters.MenuAdapter;
import moran_company.honestgram.custom.DividerItemDecoration;
import moran_company.honestgram.data.ItemMenu;
import moran_company.honestgram.fragments.base.BaseFragment;
import moran_company.honestgram.utility.DebugUtility;

/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class NavigationDrawerFragmentSeller extends BaseFragment implements NavigationDrawerSellerMvp.View {

    public static final String TAG = NavigationDrawerFragmentSeller.class.getName();

    @BindView(R.id.navigationDrawerView)
    View mNavigationDrawerView;
    @BindView(R.id.mNavigationDrawerRightList)
    RecyclerView mNavigationDrawerRightList;
    /*@BindView(R.id.avatarImageView)
    ImageView mAvatarImageView;
    @BindView(R.id.exitTextView)
    TextView mExitTextView;
    @BindView(R.id.topView)
    View mTopView;
    @BindView(R.id.avatarProfile)
    CircleImageView mAvatarProfile;
    @BindView(R.id.nickname)
    TextView nickname;*/


    private ActionBarDrawerToggle actionBarDrawerToggle;

    private DrawerLayout drawerLayout;
    private OnDrawerSlideListener onDrawerSlideListener;

    private View fragmentContainerView;

    private NavigationDrawerSellerPresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_navigation_drawer_seller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = (BaseActivity) getActivity();
        presenter = new NavigationDrawerSellerPresenter(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onResume(mBaseActivity, this);

    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (presenter != null) presenter.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateProfile(Object updateProfileEvent) {
        DebugUtility.logTest(TAG, "onUpdateProfile");

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                R.drawable.menu_list_divider,
                0,
                0);

        mNavigationDrawerRightList.addItemDecoration(dividerItemDecoration);


        //updateView();
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        setUp(fragmentId, drawerLayout, null);
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolBar) {
        this.fragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar,
                0, 0) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //super.onDrawerSlide(drawerView, slideOffset);
                float moveFactor = (mNavigationDrawerView.getWidth() * slideOffset);
                if (onDrawerSlideListener != null)
                    onDrawerSlideListener.onTranslateContainer(moveFactor);
                //EventBus.getDefault().post(new UpdateNavigation(true));

                mBaseActivity.setOpen(true);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }
//                        mViewFlipper.setDisplayedChild(VIEW_FLIPPER_MAIN_MENU);
                mBaseActivity.setOpen(false);
                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                        super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }
                // getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };
        this.drawerLayout.addDrawerListener(actionBarDrawerToggle);

    }

    @OnClick(R.id.closeImageView)
    void closeClick() {
        closeDrawer();
    }

    @Override
    public void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(fragmentContainerView);
        }
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null ? drawerLayout.isDrawerOpen(fragmentContainerView) : false;
    }

    @Override
    public void setAdapter(MenuAdapter menuAdapter) {
        if (mNavigationDrawerRightList != null)
            mNavigationDrawerRightList.setAdapter(menuAdapter);
    }


    public NavigationDrawerSellerPresenter getPresenter() {
        return presenter;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(ItemMenu itemMenu, boolean fromMenu) {
        closeDrawer();
        if (itemMenu == null)
            return;
        DebugUtility.logTest(TAG, itemMenu.getMenuType() + "");
        Handler h = new Handler();
        h.postDelayed(() -> {
            switch (itemMenu.getMenuType()){
                case CHAT_DETAIL:
                    mBaseActivity.showMapFragment();
                    break;
                case ADD_PRODUCT:
                    BaseActivity.newInstance(getContext(), AddProductActivity.class,false);
                    break;
            }
            //BaseActivity.showActivity(baseActivity, itemMenu);
        }, fromMenu ? 250 : 0);

    }

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    public void setBackControl() {
        setDrawerIndicatorEnabled(false);
        final Drawable upArrow = ContextCompat.getDrawable(getActivity(), R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        actionBarDrawerToggle.setHomeAsUpIndicator(upArrow);
        actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
            mBaseActivity.closeKeyboard();
            mBaseActivity.onBackPressed();
        });
    }

    public void setHamburgerControl() {
        setDrawerIndicatorEnabled(true);
    }

    public void setDrawerLockMode(int lockMode, int edgeGravity) {
        if (drawerLayout != null)
            drawerLayout.setDrawerLockMode(lockMode, edgeGravity);
    }

    private void setDrawerIndicatorEnabled(boolean enable) {
        if (actionBarDrawerToggle != null) actionBarDrawerToggle.setDrawerIndicatorEnabled(enable);
        setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
    }

    public void setOnDrawerSlideListener(OnDrawerSlideListener onDrawerSlideListener) {
        this.onDrawerSlideListener = onDrawerSlideListener;
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateProfile(UpdateProfileEvent updateProfileEvent) {
        DebugUtility.logTest(TAG, "onUpdateProfile");
        Profile profile = updateProfileEvent.getProfile();
        if (profile == null)
            return;
        mProfile = profile;
        updateView();


    }*/

    /*private void updateView() {
        if (mProfile != null) {
            GlideApp.with(this)
                    .load(mProfile.getAvatar())
                    .placeholder(R.drawable.ic_friends_sidebar)
                    *//*.apply(new RequestOptions()
                            .placeholder(R.drawable.ic_friends_sidebar)
                            .error(R.drawable.ic_friends_sidebar))*//*
                    .into(mAvatarImageView);
        } else {
            mAvatarImageView.setVisibility(View.INVISIBLE);
            mExitTextView.setVisibility(View.INVISIBLE);
        }
    }*/

    public interface OnDrawerSlideListener {
        void onTranslateContainer(float moveFactor);
    }


}



