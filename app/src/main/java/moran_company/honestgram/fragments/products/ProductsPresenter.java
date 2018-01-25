package moran_company.honestgram.fragments.products;

import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 24.01.2018.
 */

public class ProductsPresenter extends BasePresenterImpl<ProductsMvp.View> implements ProductsMvp.Presenter {

    public ProductsPresenter(ProductsMvp.View view) {
        super(view);
    }


    @Override
    public void loadProducts() {
        RxFirebaseDatabase
                .observeSingleValueEvent(mGoodsReference, DataSnapshotMapper.listOf(Goods.class))
                .subscribe(goods -> {
                    if (!goods.isEmpty() && isExistsView())
                        mView.showProducts(goods);
                });
    }

    @Override
    public void addToCart(Goods goods) {
        Users user = PreferencesData.INSTANCE.getUser();
        ArrayList<Goods> cartList = user.getCart();
        apiClient.getKeyById(user.getId(),mUsersReference)
                .map(key -> {
                    ArrayList<Goods> newCart = new ArrayList<>();
                    if (cartList==null){
                        newCart.add(goods);
                    }else {
                        newCart = cartList;
                        newCart.add(goods);
                    }
                    return mUsersReference.child(key).child("cart").setValue(newCart).isSuccessful();
                })
                .flatMap(isSuccess -> apiClient.getUserById(user.getId(),mUsersReference))
                .subscribe(users -> {
                    PreferencesData.INSTANCE.saveUser(users);
                    mView.successAddToCart();
                });
    }
}
