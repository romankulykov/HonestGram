package moran_company.honestgram.fragments.main

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.Dialogs
import moran_company.honestgram.data.Goods
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.data.Users
import moran_company.honestgram.utility.Utility
import java.util.HashMap
import java.util.LinkedHashMap

/**
 * Created by roman on 12.01.2018.
 */
class MainPresenter(view: MainMvp.View?) : BasePresenterImpl<MainMvp.View>(view), MainMvp.Presenter {

    override fun init() {
        RxFirebaseDatabase
                .observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users::class.java))
                .subscribe { list ->
                    if (!list.isEmpty() && isExistsView)
                        PreferencesData.saveUsers(list as ArrayList<Users>) }
    }


    override fun loadOffers() {
        RxFirebaseDatabase
                .observeSingleValueEvent(mGoodsReference, DataSnapshotMapper.listOf(Goods::class.java))
                .subscribe { list ->
                    if (!list.isEmpty() && isExistsView)
                        mView.showOffers(list) }
    }




}