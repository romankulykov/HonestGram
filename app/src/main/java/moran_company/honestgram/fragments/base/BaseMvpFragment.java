package moran_company.honestgram.fragments.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import moran_company.honestgram.base_mvp.BaseMvp;


/**
 * Created by Kulykov Anton on 9/8/17.
 */


public abstract class BaseMvpFragment<P extends BaseMvp.Presenter> extends BaseFragment {

    protected P mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
    }
}
