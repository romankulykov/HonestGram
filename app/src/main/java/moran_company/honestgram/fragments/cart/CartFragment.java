package moran_company.honestgram.fragments.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import moran_company.honestgram.R;
import moran_company.honestgram.fragments.base.BaseMvpFragment;

/**
 * Created by roman on 24.01.2018.
 */

public class CartFragment extends BaseMvpFragment<CartMvp.Presenter> implements CartMvp.View {

    @Override
    protected CartMvp.Presenter createPresenter() {
        return new CartPresenter(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_cart;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


}
