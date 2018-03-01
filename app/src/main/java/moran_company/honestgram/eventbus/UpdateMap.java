package moran_company.honestgram.eventbus;

/**
 * Created by roman on 15.02.2018.
 */

public class UpdateMap {
    public UpdateMap.TYPE_ACTION typeAction;

    public UpdateMap(UpdateMap.TYPE_ACTION typeAction) {
        this.typeAction = typeAction;
    }

    public UpdateMap.TYPE_ACTION isUpdate() {
        return typeAction;
    }

    public void setTypeAction(UpdateMap.TYPE_ACTION typeAction) {
        this.typeAction = typeAction;
    }

    public enum TYPE_ACTION {
        TRACKING,PEOPLE_ORDERS,UPDATE
    }

}
