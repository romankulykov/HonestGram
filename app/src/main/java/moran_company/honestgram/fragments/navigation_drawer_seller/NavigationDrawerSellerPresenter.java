package moran_company.honestgram.fragments.navigation_drawer_seller;


import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.ProductsActivity;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.activities.map.MapActivity;
import moran_company.honestgram.adapters.MenuAdapter;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.ItemMenu;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fragments.navigation_drawer.NavigationDrawerMvp;
import moran_company.honestgram.utility.DebugUtility;


/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class NavigationDrawerSellerPresenter extends BasePresenterImpl<NavigationDrawerSellerMvp.View> implements NavigationDrawerSellerMvp.Presenter {
    public static final String TAG = NavigationDrawerSellerPresenter.class.getName();
    private BaseActivity baseActivity;
    private NavigationDrawerSellerMvp.View mView;

    private MenuAdapter mRightMenuAdapter;

    public NavigationDrawerSellerPresenter(NavigationDrawerSellerMvp.View view) {
        super(view);
    }


    @Override
    public void onResume(BaseActivity baseActivity, NavigationDrawerSellerMvp.View view) {
        this.baseActivity = baseActivity;
        this.mView = view;
        if (baseActivity.getHonestApplication().getMenuAdapter() != null) {
            baseActivity.getHonestApplication().getMenuAdapter().
                    setOnItemClickListener((itemView, item) -> onItemClicked(item, true));
            mView.setAdapter(getRightMenuAdapter(getTypeMenu()));
        }
    }

    public MenuAdapter getRightMenuAdapter(ItemMenu.MENU_TYPE menuType) {
        if (mRightMenuAdapter == null)
            initRightDrawerAdapter(menuType);
        return mRightMenuAdapter;
    }

    private void initRightDrawerAdapter(ItemMenu.MENU_TYPE menuType) {
        List<ItemMenu> menu = new ArrayList<>();
        switch (menuType){
            case PRODUCTS:
                menu.add(new ItemMenu(R.string.add_product,0, ItemMenu.MENU_TYPE.ADD_PRODUCT));
                break;
            case MAP:
                menu.add(new ItemMenu(R.string.tracking,0, ItemMenu.MENU_TYPE.ADD_PRODUCT));
                break;
        }
        mRightMenuAdapter = new MenuAdapter(R.layout.list_item_menu);
        mRightMenuAdapter.setItems(menu);
    }



    @Override
    public void onStop() {
        mView = null;
    }

    @Override
    public void onItemClicked(ItemMenu itemMenu, boolean fromMenu) {

        if (mView != null)
            mView.closeDrawer();
        if (itemMenu == null)
            return;
        DebugUtility.logTest(TAG, itemMenu.getMenuType() + "");
        Handler h = new Handler();
        h.postDelayed(() -> {
            BaseActivity.showActivity(baseActivity, itemMenu);
        }, fromMenu ? 250 : 0);

    }

    public ItemMenu.MENU_TYPE getTypeMenu() {
        ItemMenu.MENU_TYPE menuType = ItemMenu.MENU_TYPE.NONE;
        String name = baseActivity.getClass().getName();
        if (ProductsActivity.TAG == name)
            menuType = ItemMenu.MENU_TYPE.PRODUCTS;
        if (MapActivity.TAG == name)
            menuType = ItemMenu.MENU_TYPE.MAP;
        return menuType;
    }


}
