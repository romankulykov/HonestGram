package moran_company.honestgram.fragments.chat_available;

import java.util.List;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 14.01.2018.
 */

public interface ChatAvailableMvp {

    interface View extends BaseMvp.View{
        void showUsers(List<Users> usersList);

        void successCreate();
    }

    interface Presenter extends BaseMvp.Presenter{
        void getAvailableContacts(List<List<Dialogs>> dialogs);

        void createDialog(Users user, Users selectedUser);
    }

}
