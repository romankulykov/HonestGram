package moran_company.honestgram.fragments.map;


import java.util.List;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 03.11.2017.
 */

public interface MapMvp {

    interface View extends BaseMvp.View {
        void showUsers(List<Users> usersList);
    }

    interface Presenter extends BaseMvp.Presenter {
        void loadUsers();
    }

}