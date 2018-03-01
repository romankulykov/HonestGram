package moran_company.honestgram.fragments.map


import moran_company.honestgram.base_mvp.BaseMvp
import moran_company.honestgram.data.ItemUserOrder

/**
 * Created by roman on 03.11.2017.
 */

interface MapMvp {

    interface View : BaseMvp.View {
        fun showObjects(usersList: List<ItemUserOrder>)

        //void showUsers(List<Users> usersList);

        //void showShippedOrders(List<Orders> list);
    }

    interface Presenter : BaseMvp.Presenter {
        fun loadUsers()

        fun loadShippedOrders()

    }

}