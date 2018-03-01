package moran_company.honestgram.fragments.cart;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 24.01.2018.
 */

public interface CartMvp {

    interface View extends BaseMvp.View{

        void showCart(ArrayList<Goods> goods);

        void successDelete(Users users);

        void successIssue(Users users);
    }

    interface Presenter extends BaseMvp.Presenter{
        void remove(long id);

        void getCart();

        void pushOrder(List<Goods> items, Context context);
    }

}
