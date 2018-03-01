package moran_company.honestgram.fragments.navigation_drawer_seller;


import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.ProductsActivity;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.activities.map.MapActivity;
import moran_company.honestgram.adapters.MenuAdapter;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.ItemMenu;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.data.enums.TYPE_USER;


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
/*
        if (baseActivity.getHonestApplication().getRightMenuAdapter() != null) {
            baseActivity.getHonestApplication().getRightMenuAdapter().
                    setOnItemClickListener((itemView, item) -> mView.onItemClicked(item, true));
            mView.setAdapter(getRightMenuAdapter(getTypeMenu()));
        }
*/
        mView.setAdapter(getRightMenuAdapter(getTypeMenu()));
        mRightMenuAdapter.setOnItemClickListener((itemView, item) -> {
            mView.onItemClicked(item, true);
        });
        /*getRightMenuAdapter(getTypeMenu()).
                setOnItemClickListener((itemView, item) -> mView.onItemClicked(item, true));*/
    }

    public MenuAdapter getRightMenuAdapter(ItemMenu.MENU_TYPE menuType) {
        if (mRightMenuAdapter == null)
            initRightDrawerAdapter(menuType);
        return mRightMenuAdapter;
    }

    private void initRightDrawerAdapter(ItemMenu.MENU_TYPE menuType) {
        List<ItemMenu> menu = new ArrayList<>();
        Users users = PreferencesData.INSTANCE.getUser();
        TYPE_USER userType = TYPE_USER.Companion.getTypeUserById(users.getStatusId().intValue());
        switch (menuType) {
            case PRODUCTS:
                switch (userType) {
                    case ADMIN:
                        menu.add(new ItemMenu(R.string.add_product, 0, ItemMenu.MENU_TYPE.ADD_PRODUCT));
                        break;
                }
                menu.add(new ItemMenu(R.string.filter_by_city, 0, ItemMenu.MENU_TYPE.FILTER_BY_CITY));
                menu.add(new ItemMenu(R.string.filter_by_price, 0, ItemMenu.MENU_TYPE.FILTER_BY_PRICE));
                break;
            case MAP:
                menu.add(new ItemMenu(R.string.tracking, 0, ItemMenu.MENU_TYPE.TRACKING));
                switch (userType) {
                    case ADMIN:
                        menu.add(new ItemMenu(R.string.shipped_orders,0, ItemMenu.MENU_TYPE.SHIPPED_ORDERS));
                        /*getProductsByUserId(users.getId())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(goods -> {
                                    menu.addAll(goods);
                                    mRightMenuAdapter.setItems(menu);
                                },this::onError);*/
                        break;
                }
                break;
        }
        mRightMenuAdapter = new MenuAdapter(R.layout.list_item_menu);
        mRightMenuAdapter.setItems(menu);
    }

    private Flowable<List<ItemMenu>> getProductsByUserId(long userId) {
        return dbInstance.getProductsDao().getProductByOwnerId(userId)
                .take(1)
                .flatMapIterable(goods -> goods)
                .map(goods -> new ItemMenu(goods.getTitle(), goods.getId(), ItemMenu.MENU_TYPE.LIST_OF_GOODS))
                .toList().toFlowable();
    }

    @Override
    public void onStop() {
        mView = null;
    }

    /*@Override
    public void onItemClicked(ItemMenu itemMenu, boolean fromMenu) {

        if (mView != null)
            mView.closeDrawer();
        if (itemMenu == null)
            return;
        DebugUtility.logTest(TAG, itemMenu.getMenuType() + "");
        Handler h = new Handler();
        h.postDelayed(() -> {
            //BaseActivity.showActivity(baseActivity, itemMenu);
            if (itemMenu.getMenuType()== ItemMenu.MENU_TYPE.CHAT_DETAIL){

            }
        }, fromMenu ? 250 : 0);

    }*/

    private ItemMenu.MENU_TYPE getTypeMenu() {
        ItemMenu.MENU_TYPE menuType = ItemMenu.MENU_TYPE.NONE;
        String name = baseActivity.getClass().getName();
        if (ProductsActivity.TAG == name)
            menuType = ItemMenu.MENU_TYPE.PRODUCTS;
        if (MapActivity.TAG == name)
            menuType = ItemMenu.MENU_TYPE.MAP;
        return menuType;
    }


}
