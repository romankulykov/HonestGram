package moran_company.honestgram.activities.products_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.activities.base.BaseMvpActivity;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.Urls;

/**
 * Created by roman on 24.01.2018.
 */

public class ProductDetailActivity extends BaseActivity {

    public static final String TAG = ProductDetailActivity.class.getName();
    private static final String EXTRA_GOOD = "good";
    private static final String EXTRA_URLS = "urls";

    private Goods mGood;
    private ArrayList<Urls> mUrls;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_product_detail;
    }

    public static void newInstance(Context context, Goods good) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_GOOD, Parcels.wrap(good));
        if (good.getUrls() != null)
            intent.putParcelableArrayListExtra(EXTRA_URLS, good.getUrls());
        ActivityCompat.startActivity(context, intent, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            mGood = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_GOOD));
            mUrls = getIntent().getParcelableArrayListExtra(EXTRA_URLS);
        }
        if (mUrls != null && mGood != null) mGood.setUrls(mUrls);
        if (mGood != null) {
            showProductDetailFragment(mGood);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getNavigationDrawerFragment().setBackControl();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //HonestApplication.getInstance().getMenuAdapter().setItemChecked(ItemMenu.MENU_TYPE.PRODUCTS);
    }


}

