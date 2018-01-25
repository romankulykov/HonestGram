package moran_company.honestgram.fragments.cart;

import moran_company.honestgram.base_mvp.BasePresenterImpl;

/**
 * Created by roman on 24.01.2018.
 */

public class CartPresenter extends BasePresenterImpl<CartMvp.View> implements CartMvp.Presenter {
    public CartPresenter(CartMvp.View view) {
        super(view);
    }
}
