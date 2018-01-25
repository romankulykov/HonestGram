package moran_company.honestgram.fragments.navigation_drawer_seller;

import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.adapters.MenuAdapter;
import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.ItemMenu;
import moran_company.honestgram.data.Users;

/**
 * Created by Kulykov Anton on 9/8/17.
 */

public interface NavigationDrawerSellerMvp {

    interface View extends BaseMvp.View {

        void closeDrawer();

        void setAdapter(MenuAdapter menuAdapter);


    }

    interface Presenter extends BaseMvp.Presenter {

        void onItemClicked(ItemMenu itemMenu, boolean fromMenu);

        void onStop();

        void onResume(BaseActivity baseActivity, View view);

    }


}
