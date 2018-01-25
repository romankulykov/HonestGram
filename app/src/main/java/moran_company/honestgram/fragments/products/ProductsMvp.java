package moran_company.honestgram.fragments.products;

import java.util.List;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Goods;

/**
 * Created by roman on 24.01.2018.
 */

public interface ProductsMvp {
    interface View extends BaseMvp.View{
        void showProducts(List<Goods> productList);

        void successAddToCart();
    }

    interface Presenter extends BaseMvp.Presenter{
        void loadProducts();

        void addToCart(Goods goods);
    }
}
