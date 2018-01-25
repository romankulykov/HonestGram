package moran_company.honestgram.fragments.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.fragmentargs.FragmentArgs;

import butterknife.ButterKnife;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.fragments.navigation_drawer.NavigationDrawerFragment;
import moran_company.honestgram.fragments.navigation_drawer_seller.NavigationDrawerFragmentSeller;
import moran_company.honestgram.utility.DialogUtility;


/**
 * Created by Kulykov Anton on 9/8/17.
 */


public abstract class BaseFragment extends Fragment implements BaseMvp.View {

    protected BaseActivity mBaseActivity;
    private Dialog mWaitingDialog;
    private CharSequence mPreviousTitle;

    public abstract int getLayoutResId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        mBaseActivity = (BaseActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        if (mPreviousTitle != null) getActivity().setTitle(mPreviousTitle);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(), null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (mBaseActivity == null || mBaseActivity.getNavigationDrawerFragment() == null|| mBaseActivity.getNavigationDrawerFragmentSeller() == null)
            return;
        if (this instanceof NavigationDrawerFragment || this instanceof NavigationDrawerFragmentSeller)
            return;
        if (mBaseActivity.haveFragmentInBackStack())
            mBaseActivity.getNavigationDrawerFragment().setBackControl();
        else
            mBaseActivity.getNavigationDrawerFragment().setHamburgerControl();
    }

    @Override
    public void onNetworkDisable() {
        if (getActivity() instanceof BaseMvp.View) {
            ((BaseMvp.View) getActivity()).onNetworkDisable();
        }
    }

    @Override
    public void onUnauthorized() {
        if (getActivity() instanceof BaseMvp.View) {
            ((BaseMvp.View) getActivity()).onUnauthorized();
        }
    }

    @Override
    public void setProgressIndicator(boolean active) {
        mBaseActivity.runOnUiThread(() -> {
            if (active) {
                if (mWaitingDialog == null) mWaitingDialog = DialogUtility.getWaitDialog(
                        getContext(), BaseFragment.this.getString(R.string.wait_loading), false);
                mWaitingDialog.show();

            } else {
                DialogUtility.closeDialog(mWaitingDialog);
            }
        });

    }


    public void showToolbar() {
        if (getActivity() instanceof BaseMvp.View) {
            ((BaseActivity) getActivity()).showToolbar();
        }
    }

    public void hideToolbar() {
        if (getActivity() instanceof BaseMvp.View) {
            ((BaseActivity) getActivity()).hideToolbar();
        }
    }

    @Override
    public void showToast(String text) {
        if (getActivity() instanceof BaseMvp.View) {
            ((BaseMvp.View) getActivity()).showToast(text);
        }
    }

    @Override
    public void showToast(int textId) {
        if (getActivity() instanceof BaseMvp.View) {
            ((BaseMvp.View) getActivity()).showToast(textId);
        }
    }


    public void setTitle(CharSequence title) {
        mPreviousTitle = getActivity().getTitle();
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null)
            baseActivity.setTitle(title);
    }

    public void setTitle(int titleId) {
        mPreviousTitle = getActivity().getTitle();
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null)
            baseActivity.setTitle(titleId);
    }

    public void setTitle(String title) {
        mPreviousTitle = getActivity().getTitle();
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null)
            baseActivity.setTitle(title);
    }

    public void onBackPressed() {
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null)
            baseActivity.onBackPressed();
    }

    public boolean checkCanBackPressed() {
        return true;
    }

}
