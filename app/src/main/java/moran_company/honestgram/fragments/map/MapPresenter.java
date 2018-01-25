package moran_company.honestgram.fragments.map;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import moran_company.honestgram.R;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 03.11.2017.
 */

public class MapPresenter extends BasePresenterImpl<MapMvp.View> implements MapMvp.Presenter {


    public MapPresenter(MapMvp.View view) {
        super(view);
    }


    @Override
    public void loadUsers() {
        Users user = PreferencesData.INSTANCE.getUser();
        RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users.class))
                .toFlowable().flatMapIterable(usersList -> usersList)
                .filter(users -> users.getId()!=user.getId())
                .toList().toFlowable()
                .subscribe(list -> {
                    if (isExistsView()) {
                        if (!list.isEmpty() && isExistsView()) mView.showUsers(list);
                        else mView.showToast(R.string.not_found);
                    }
                });

    }
}
