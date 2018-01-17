package moran_company.honestgram.eventbus;

/**
 * Created by roman on 16.01.2018.
 */

public class UpdateDialogs {
    public boolean update;

    public UpdateDialogs(boolean update) {
        this.update = update;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
