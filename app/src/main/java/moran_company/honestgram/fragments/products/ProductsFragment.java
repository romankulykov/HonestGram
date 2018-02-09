package moran_company.honestgram.fragments.products;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.products_detail.ProductDetailActivity;
import moran_company.honestgram.adapters.ProductsAdapter;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.fragments.base.BaseMvpFragment;

/**
 * Created by roman on 24.01.2018.
 */

public class ProductsFragment extends BaseMvpFragment<ProductsMvp.Presenter> implements ProductsMvp.View {

    private ProductsAdapter productsAdapter = new ProductsAdapter();

    @BindView(R.id.products)
    RecyclerView products;

    @Override
    protected ProductsMvp.Presenter createPresenter() {
        return new ProductsPresenter(this);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_products;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.loadProducts();

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        products.setLayoutManager(staggeredGridLayoutManager);
        products.setAdapter(productsAdapter);
        productsAdapter.setOnBasketClickLister(this::addToCart);
        productsAdapter.setOnItemClickListener((itemView, item) -> {
            //mBaseActivity.showProductDetailFragment(item);
            ProductDetailActivity.newInstance(getContext(),item);
            //mBaseActivity.showProductDetailFragment(item);
        });
    }

    @Override
    public void showProducts(List<Goods> productList) {
        productsAdapter.setItems(productList);
    }

    @Override
    public void successAddToCart() {
        showToast(R.string.success_add_to_cart);
        mBaseActivity.setBasketCounter(PreferencesData.INSTANCE.getUser());
    }

    private void addToCart(Goods goods){
        mPresenter.addToCart(goods);
    }

}
