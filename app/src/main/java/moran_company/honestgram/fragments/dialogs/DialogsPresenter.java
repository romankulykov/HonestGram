package moran_company.honestgram.fragments.dialogs;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import durdinapps.rxfirebase2.DataSnapshotMapper;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.Utility;
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider;

/**
 * Created by roman on 14.01.2018.
 */

public class DialogsPresenter extends BasePresenterImpl<DialogsMvp.View> implements DialogsMvp.Presenter {

    public DialogsPresenter(DialogsMvp.View view) {
        super(view);
    }

    @Override
    public void loadMessages() {
        Users users = PreferencesData.INSTANCE.getUser();
        RxFirebaseDatabase.observeSingleValueEvent(mDialogsReference, DataSnapshotMapper.listOf(Dialogs.class))
                .toFlowable().flatMapIterable(dialogs -> dialogs)
                .filter(dialogs -> dialogs.getUser_id()==users.getId())
                .toList()
                .map(Utility::removeDuplicates).toFlowable()
                .flatMapIterable(dialogs -> dialogs)
                .flatMap(dialogs -> RxFirebaseDatabase
                        .observeSingleValueEvent(mDialogsReference, DataSnapshotMapper.listOf(Dialogs.class))
                        .toFlowable()
                        .flatMapIterable(dialogs1 -> dialogs1)
                        .filter(dialogs1 -> dialogs1.getDialog_id()==dialogs.getDialog_id())
                        .toList().toFlowable()
                )
                .subscribe(list -> {
                    if (!list.isEmpty() && isExistsView()) mView.addDialog(list);
                });
    }
}
