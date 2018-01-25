package moran_company.honestgram.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;

import butterknife.BindView;
import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.data.ItemMenu;

/**
 * Created by roman on 24.01.2018.
 */

public class ProductsActivity extends BaseActivity {


    public static final String TAG = ProductsActivity.class.getName();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_products;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showProductsFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HonestApplication.getInstance().getMenuAdapter().setItemChecked(ItemMenu.MENU_TYPE.PRODUCTS);
    }



}

