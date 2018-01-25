package moran_company.honestgram.activities.map;

import android.os.Bundle;
import android.support.annotation.Nullable;

import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.data.ItemMenu;

/**
 * Created by roman on 18.01.2018.
 */

public class MapActivity extends BaseActivity {
    public static final String TAG = MapActivity.class.getName();

    @Override
    public int getLayoutResId() {
        return R.layout.activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showMapFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HonestApplication.getInstance().getMenuAdapter().setItemChecked(ItemMenu.MENU_TYPE.MAP);

    }

}
