package moran_company.honestgram.fragments.products;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.data.enums.FILTER;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 24.01.2018.
 */

public class ProductsPresenter extends BasePresenterImpl<ProductsMvp.View> implements ProductsMvp.Presenter {

    public ProductsPresenter(ProductsMvp.View view) {
        super(view);
    }


    @Override
    public void loadProducts() {
        /*RxFirebaseDatabase
                .observeSingleValueEvent(mGoodsReference, DataSnapshotMapper.listOf(Goods.class))
                .subscribe(goods -> {
                    if (!goods.isEmpty() && isExistsView())
                        mView.showProducts(goods);
                });*/
        dbInstance.getProductsDao().getAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goods -> {
                    if (!goods.isEmpty() && isExistsView())
                        mView.showProducts(goods);
                }, this::onError, this::onComplete);
    }

    @Override
    public void addToCart(Goods goods) {
        Users user = PreferencesData.INSTANCE.getUser();

        RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable()
                .flatMapIterable(users -> users)
                .filter(userInList -> userInList.getId()==user.getId())
                .zipWith(apiClient.getKeyById(user.getId(), mUsersReference),(users, key) -> {
                    ArrayList<Goods> newCart = new ArrayList<>();
                    ArrayList<Goods> cartList = users.getCartList();
                    if (cartList == null) {
                        newCart.add(goods);
                    } else {
                        newCart = cartList;
                        newCart.add(goods);
                    }
                    return mUsersReference.child(key).child("cart").setValue(new Gson().toJson(newCart)).isSuccessful();
                })
                .flatMap(isSuccess -> apiClient.getUserById(user.getId(), mUsersReference))
                .subscribe(users -> {
                    PreferencesData.INSTANCE.saveUser(users);
                    mView.successAddToCart();
                });

        /*ArrayList<Goods> cartList = user.getCart();
        apiClient.getKeyById(user.getId(), mUsersReference)
                .map(key -> {
                    ArrayList<Goods> newCart = new ArrayList<>();
                    if (cartList == null) {
                        newCart.add(goods);
                    } else {
                        newCart = cartList;
                        newCart.add(goods);
                    }
                    return mUsersReference.child(key).child("cart").setValue(newCart).isSuccessful();
                })
                .flatMap(isSuccess -> apiClient.getUserById(user.getId(), mUsersReference))
                .subscribe(users -> {
                    PreferencesData.INSTANCE.saveUser(users);
                    mView.successAddToCart();
                });*/
    }

    @Override
    public void filterBy(FILTER filterByPrice) {
        Flowable<List<Goods>> goodsFlowable = null;
        switch (filterByPrice) {
            case FILTER_BY_CITY:
                goodsFlowable = dbInstance.getProductsDao().getFilteredByCityDesc();
                break;
            case FILTER_BY_PRICE:
                goodsFlowable = dbInstance.getProductsDao().getFilteredByPriceDesc();
                break;
        }
        goodsFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(goods -> {
                    if (!goods.isEmpty() && isExistsView())
                        mView.showProducts(goods);
                }, this::onError, this::onComplete);
    }
}
