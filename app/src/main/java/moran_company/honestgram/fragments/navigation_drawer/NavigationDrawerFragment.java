package moran_company.honestgram.fragments.navigation_drawer;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.activities.login.LoginActivity;
import moran_company.honestgram.adapters.MenuAdapter;
import moran_company.honestgram.custom.DividerItemDecoration;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fragments.base.BaseFragment;
import moran_company.honestgram.utility.DebugUtility;
import moran_company.honestgram.utility.ImageFilePath;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class NavigationDrawerFragment extends BaseFragment implements NavigationDrawerMvp.View {

    public static final String TAG = NavigationDrawerFragment.class.getName();

    @BindView(R.id.navigationDrawerView)
    View mNavigationDrawerView;
    @BindView(R.id.navigationDrawerList)
    RecyclerView mNavigationDrawerList;
    @BindView(R.id.avatarImageView)
    ImageView mAvatarImageView;
    @BindView(R.id.exitTextView)
    TextView mExitTextView;
    @BindView(R.id.topView)
    View mTopView;
    @BindView(R.id.avatarProfile)
    CircleImageView mAvatarProfile;
    @BindView(R.id.nickname)
    TextView nickname;


    private ActionBarDrawerToggle actionBarDrawerToggle;

    private DrawerLayout drawerLayout;
    private OnDrawerSlideListener onDrawerSlideListener;

    private View fragmentContainerView;

    private NavigationDrawerPresenter presenter;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_navigation_drawer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity = (BaseActivity) getActivity();
        presenter = new NavigationDrawerPresenter(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onResume(mBaseActivity, this);
        if (PreferencesData.INSTANCE.getUser() != null)
            setProfile(PreferencesData.INSTANCE.getUser());
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
      /*  User mUser = updateProfileEvent.getUser();
        if (mUser == null)
            return;*/
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                R.drawable.menu_list_divider,
                0,
                0);

        mNavigationDrawerList.addItemDecoration(dividerItemDecoration);


        //updateView();
    }

   /* public void setTopViewBackgroundColor(int) {
        mTopView = topView;
    }*/


    @Override
    public void setProfile(Users users) {
        mAvatarProfile.setVisibility(View.VISIBLE);
        nickname.setVisibility(View.VISIBLE);
        if (users != null) {
            GlideApp.with(getContext())
                    .load(users.getPhotoURL())
                    .placeholder(R.drawable.user_add)
                    .into(mAvatarProfile);
            nickname.setText(users.getNickname());
            PreferencesData.INSTANCE.saveUser(users);
        } else resetProfile();
    }

    public void resetProfile() {
        mAvatarProfile.setVisibility(View.GONE);
        nickname.setVisibility(View.GONE);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        setUp(fragmentId, drawerLayout, null);
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolBar) {
        this.fragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;
        this.drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        //this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        if (toolBar != null) {
            // mBaseActivity.setSupportActionBar(toolBar);
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar,
                        0, 0) {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        //super.onDrawerSlide(drawerView, slideOffset);
                        float moveFactor = (mNavigationDrawerView.getWidth() * slideOffset);
                        if (onDrawerSlideListener != null)
                            onDrawerSlideListener.onTranslateContainer(moveFactor);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //super.onDrawerClosed(drawerView);
                        if (!isAdded()) {
                            return;
                        }
//                        mViewFlipper.setDisplayedChild(VIEW_FLIPPER_MAIN_MENU);

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
                this.drawerLayout.post(() -> actionBarDrawerToggle.syncState());
                this.drawerLayout.addDrawerListener(actionBarDrawerToggle);
            }
        }


    }

    @OnClick(R.id.closeImageView)
    void closeClick() {
        closeDrawer();
    }

    @OnClick(R.id.exitTextView)
    void exitClick() {
        PreferencesData.INSTANCE.resetProfile();
        mBaseActivity.stopLocationService();
        BaseActivity.newInstance(getContext(), LoginActivity.class, true);
    }

    @OnClick(R.id.avatarProfile)
    void changeAvatar() {
        CropImage.activity()
                //.setRequestedSize(Constants.PROFILE_WIDTH, Constants.PROFILE_HEIGHT)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(132, 170)
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                String selectedImagePath;
                Uri imageUri = result.getUri();

                //MEDIA GALLERY
                selectedImagePath = ImageFilePath.getPath(getContext(), imageUri);
                Log.i("Image File Path", "" + selectedImagePath);
                presenter.newImage(selectedImagePath);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    @Override
    public void closeDrawer() {
        if (drawerLayout != null)
            drawerLayout.closeDrawer(fragmentContainerView);
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null ? drawerLayout.isDrawerOpen(fragmentContainerView) : false;
    }

    @Override
    public void setAdapter(MenuAdapter menuAdapter) {
        if (mNavigationDrawerList != null)
            mNavigationDrawerList.setAdapter(menuAdapter);
    }


    public NavigationDrawerPresenter getPresenter() {
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

    private void setDrawerLockMode(int lockMode, int edgeGravity) {
        if (drawerLayout != null)
            drawerLayout.setDrawerLockMode(lockMode, edgeGravity);
    }

    private void setDrawerIndicatorEnabled(boolean enable) {
        if (actionBarDrawerToggle != null) actionBarDrawerToggle.setDrawerIndicatorEnabled(enable);
        setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
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



