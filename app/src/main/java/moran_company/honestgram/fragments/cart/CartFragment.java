package moran_company.honestgram.fragments.cart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import moran_company.honestgram.R;
import moran_company.honestgram.adapters.CartAdapter;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fragments.base.BaseMvpFragment;

/**
 * Created by roman on 24.01.2018.
 */

public class CartFragment extends BaseMvpFragment<CartMvp.Presenter> implements CartMvp.View {


    @BindView(R.id.products)
    RecyclerView products;

    CartAdapter cartAdapter = new CartAdapter();

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
        products.setAdapter(cartAdapter);
        cartAdapter.setOnRemoveClickListener(position -> {
            mPresenter.remove(position);
        });
        mPresenter.getCart();

    }

    @OnClick(R.id.checkout)
    void checkout(){
        mPresenter.pushOrder(cartAdapter.getItems(),getContext());
    }


    @Override
    public void showCart(ArrayList<Goods> goods) {
        cartAdapter.setItems(goods);
    }

    @Override
    public void successDelete(Users users) {
        mBaseActivity.setBasketCounter(users);
        cartAdapter.setItems(users.getCartList());
    }

    @Override
    public void successIssue(Users users) {
        cartAdapter.clear();
        mBaseActivity.setBasketCounter(users);
    }
}
