package moran_company.honestgram.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;

/**
 * Created by roman on 24.01.2018.
 */

public class CartActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showCartFragment();

    }
}
