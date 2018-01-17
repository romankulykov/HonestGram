package moran_company.honestgram.fragments.navigation_drawer;


import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import moran_company.honestgram.activities.base.BaseActivity;
import moran_company.honestgram.adapters.MenuAdapter;
import moran_company.honestgram.base_mvp.BasePresenterImpl;
import moran_company.honestgram.data.ImageUploadInfo;
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
        Users users = PreferencesData.INSTANCE.getUser();
        //String nameFile = storageReference.child(path).getName();
        String nameFile = users.getNickname()+"_avatar_"+System.currentTimeMillis();
        storageReference.child(nameFile).putFile(filePathUri)
                .addOnSuccessListener(taskSnapshot -> {
                    ImageUploadInfo imageUploadInfo = new ImageUploadInfo(path, taskSnapshot.getDownloadUrl().toString());

                    // Getting image upload ID.

                    if (!TextUtils.isEmpty(users.getPhotoName())) {
                        storageReference.child(users.getPhotoName()).delete().addOnSuccessListener(aVoid -> {

                        }).addOnFailureListener(e -> {
                            e.printStackTrace();
                        });
                    }
                    mUsersReference
                            .orderByChild("id")
                            .equalTo(users.getId())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                                        users.setPhotoName(nameFile);
                                        users.setPhotoURL(taskSnapshot.getDownloadUrl().toString());
                                        data.getRef().setValue(users);
                                        mView.setProfile(users);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                }).addOnFailureListener(e -> {

        });
    }

    @Override
    public void onStop() {
        mView = null;
    }

    @Override
    public void onItemClicked(ItemMenu itemMenu, boolean fromMenu) {

//        baseActivity.getNgseApplication().getMenuAdapter().setItemChecked(itemMenu.getMenuType());
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
