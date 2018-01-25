package moran_company.honestgram.fragments.navigation_drawer;


import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.function.BiFunction;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.subscribers.DisposableSubscriber;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.ItemMenu;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.DebugUtility;


/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class NavigationDrawerPresenter extends BasePresenterImpl<NavigationDrawerMvp.View> implements NavigationDrawerMvp.Presenter {
    public static final String TAG = NavigationDrawerPresenter.class.getName();
    private BaseActivity baseActivity;
    private NavigationDrawerMvp.View mView;


    public NavigationDrawerPresenter(NavigationDrawerMvp.View view) {
        super(view);
    }


    @Override
    public void onResume(BaseActivity baseActivity, NavigationDrawerMvp.View view) {
        this.baseActivity = baseActivity;
        this.mView = view;
        if (baseActivity.getHonestApplication().getMenuAdapter() != null) {
            baseActivity.getHonestApplication().getMenuAdapter().
                    setOnItemClickListener((itemView, item) -> onItemClicked(item, true));
            mView.setAdapter(baseActivity.getHonestApplication().getMenuAdapter());
        }
    }

    @Override
    public void newImage(String path) {
        Uri filePathUri = Uri.fromFile(new File(path));
        Users oldUser = PreferencesData.INSTANCE.getUser();
        // Необходимо : Прокинуть в storage новую картинку, затем сохранить ее урл иназвание,
        // получить нашего юзера и в него пропихнуть новые данные урла картинки и название и отправить в бд
        // и затем удалить старую фотографию
        String nameFile = oldUser.getNickname() + "_avatar_" + System.currentTimeMillis();
        RxFirebaseStorage.putFile(mStorageReference.child(nameFile), filePathUri)
                .toFlowable()
                .flatMap(taskSnapshot ->
                        getUser(taskSnapshot,oldUser))
                .map(pairUserTask -> {
                    Users user = pairUserTask.first.first;
                    DataSnapshot dataSnapshot = pairUserTask.first.second;
                    UploadTask.TaskSnapshot taskSnapshot = pairUserTask.second;
                    user.setPhotoName(nameFile);
                    user.setPhotoURL(taskSnapshot.getDownloadUrl().toString());
                    dataSnapshot.getRef().setValue(user);
                    return user;
                })
                .subscribe(new DisposableSubscriber<Users>() {
                    @Override
                    public void onNext(Users users) {
                        mView.showToast(R.string.success);
                        mView.setProfile(users);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mView.showToast(t.toString());
                    }

                    @Override
                    public void onComplete() {
                        if (!TextUtils.isEmpty(oldUser.getPhotoName()))
                            RxFirebaseStorage.delete(mStorageReference.child(oldUser.getPhotoName()))
                                    .subscribe(new DisposableCompletableObserver() {
                                        @Override
                                        public void onComplete() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            mView.showToast(e.toString());
                                        }
                                    });
                    }
                });
    }

    @Override
    public void onStop() {
        mView = null;
    }

    @Override
    public void onItemClicked(ItemMenu itemMenu, boolean fromMenu) {

        if (mView != null)
            mView.closeDrawer();
        if (itemMenu == null)
            return;
        DebugUtility.logTest(TAG, itemMenu.getMenuType() + "");
        Handler h = new Handler();
        h.postDelayed(() -> {
            BaseActivity.showActivity(baseActivity, itemMenu);
        }, fromMenu ? 250 : 0);


    }


}
