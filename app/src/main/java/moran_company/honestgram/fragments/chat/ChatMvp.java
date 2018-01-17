package moran_company.honestgram.fragments.chat;

import java.util.List;

import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.data.Dialogs;

/**
 * Created by roman on 14.01.2018.
 */

public interface ChatMvp {

    interface View extends BaseMvp.View{
        void showDialogs(List<Dialogs> dialogsList);
    }

    interface Presenter extends BaseMvp.Presenter{
        void loadMessages(long dialogId);

        void sendMessage(String s,long dialogId,long messageId);
    }

}
