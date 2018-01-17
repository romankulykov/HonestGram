package moran_company.honestgram.fragments.dialogs;

import java.util.List;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Dialogs;

/**
 * Created by roman on 14.01.2018.
 */

public interface DialogsMvp {
    interface View extends BaseMvp.View{
        void showDialogs(List<List<Dialogs>> dialogsList);
        void addDialog(List<Dialogs> dialogs);

    }

    interface Presenter extends BaseMvp.Presenter{
        void loadMessages();

    }
}
