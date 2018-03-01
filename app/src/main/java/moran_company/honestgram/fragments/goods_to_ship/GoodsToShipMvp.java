package moran_company.honestgram.fragments.goods_to_ship;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Orders;

/**
 * Created by roman on 14.01.2018.
 */

public interface GoodsToShipMvp {

    interface View extends BaseMvp.View{
        void showOrders(List<Orders> orders);

        void successShipped();
    }

    interface Presenter extends BaseMvp.Presenter{
        void getOrders();

        void shipOrder(LatLng latLng, Orders orders, String commentText, String mPath);
    }

}
