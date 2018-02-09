package moran_company.honestgram.fragments.profile

import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.City
import moran_company.honestgram.data.PreferencesData

/**
 * Created by roman on 23.01.2018.
 */
class ProfilePresenter(view: ProfileMvp.View?) : BasePresenterImpl<ProfileMvp.View>(view), ProfileMvp.Presenter {

    override fun loadCities() {
        RxFirebaseDatabase
                .observeSingleValueEvent(mCitiesReference, DataSnapshotMapper.listOf(City::class.java))
                .subscribe { list ->
                    if (!list.isEmpty() && isExistsView) {
                        PreferencesData.saveCities(list as ArrayList<City>)
                        mView.showCities(list)
                    }
                }
    }



}