package moran_company.honestgram.fragments.base;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hannesdorfmann.fragmentargs.FragmentArgs;

import org.jetbrains.annotations.NotNull;

import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.utility.DialogUtility;

/**
 * Created by roman on 14.01.2018.
 */

public abstract class BaseDialogFragment<P extends BaseMvp.Presenter> extends DialogFragment implements BaseMvp.View {


    protected P mPresenter;
    protected BaseActivity mBaseActivity;
    private Dialog mWaitingDialog;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        mBaseActivity = (BaseActivity) getActivity();
        mPresenter = createPresenter();

    }

    protected abstract P createPresenter();

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showToast(@NotNull String text) {

    }

    @Override
    public void showToast(int textId) {

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
                        mBaseActivity.getApplicationContext(), BaseDialogFragment.this.getString(R.string.wait_loading), false);
                mWaitingDialog.show();

            } else {
                DialogUtility.closeDialog(mWaitingDialog);
            }
        });

    }
}
