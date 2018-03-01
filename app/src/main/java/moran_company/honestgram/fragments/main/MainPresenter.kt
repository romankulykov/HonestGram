package moran_company.honestgram.fragments.main

import android.util.Log
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.Deferred
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.City
import moran_company.honestgram.data.Goods
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.data.Users
import java.util.*

/**
 * Created by roman on 12.01.2018.
 */
class MainPresenter(view: MainMvp.View?) : BasePresenterImpl<MainMvp.View>(view), MainMvp.Presenter {


    override fun init() {
        RxFirebaseDatabase
                .observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users::class.java))
                .subscribe({ list ->
                    if (!list.isEmpty() && isExistsView) {

                        for (user in list)
                            dbInstance.userDao.insertTask(user)


                        PreferencesData.saveUsers(list as ArrayList<Users>)
                    }
                }, this::onError)
    }

    override fun loadNecessaryData() {
        var hasCities = dbInstance.citiesDao.hasData().map { it > 0 }

        /*hasCities.zipWith(hasGoods, { cities, goods ->
            Log.d(TAG, "cities = " + cities)
        })

        var r = Flowable.zip(hasCities, hasGoods,
                BiFunction<Boolean, Boolean, Flowable> { existCities, existGoods ->
                    if (!existCities && !existGoods) return
                })*/
        hasCities.subscribe {
            when (it) {
                true -> {
                    Log.d(TAG, it.toString())
                }
                else -> {
                    Log.d(TAG, it.toString())
                    loadCities()
                }
            }
        }

        loadProducts()
    }


    fun loadCities() =
            RxFirebaseDatabase
                    .observeSingleValueEvent(mCitiesReference, DataSnapshotMapper.listOf(City::class.java))
                    .map { list ->
                        if (!list.isEmpty() && isExistsView) {
                            PreferencesData.saveCities(list as ArrayList<City>)
                            for (city in list) {
                                dbInstance.citiesDao.insertTask(city)
                            }
                        }else{
                            dbInstance.citiesDao.delete()
                        }
                        true
                    }.toFlowable().subscribe { t: Boolean? -> t }


    override fun loadProducts() {
        var hasGoods = dbInstance.productsDao.hasData().map { it > 0 }


        var flowable: Flowable<List<Goods>>

        hasGoods
                .take(1)
                .flatMap { Flowable.just(fromServer()) }
                .flatMap {
                    it
                            .flatMapIterable { it }
                            .filter { t -> t.specialPrice > 0 }
                            .toList().toFlowable()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    if (!list.isEmpty() && isExistsView)
                        mView.showOffers(list)
                }, this::onError, this::onComplete)


    }

    fun fromCache() = dbInstance.productsDao.getAllTasks()

    fun fromServer() = RxFirebaseDatabase
            .observeSingleValueEvent(mGoodsReference, DataSnapshotMapper.listOf(Goods::class.java))
            .toFlowable()
            .map { list ->
                PreferencesData.saveProducts(list as ArrayList<Goods>)
                for (goods in list) {
                    dbInstance.productsDao.insertTask(goods)
                }
                list as List<Goods>
            }

    /*
    override fun loadCities() {
        RxFirebaseDatabase
                .observeSingleValueEvent(mCitiesReference, DataSnapshotMapper.listOf(City::class.java))
                .subscribe { list ->
                    if (!list.isEmpty() && isExistsView) {
                        PreferencesData.saveCities(list as ArrayList<City>)
                    }
                }
    }*/

    /*override fun loadOffers() {
        RxFirebaseDatabase
                .observeSingleValueEvent(mGoodsReference, DataSnapshotMapper.listOf(Goods::class.java))
                .toFlowable().flatMapIterable({ t: MutableList<Goods> -> t })
                .filter({ t -> t.specialPrice > 0 })
                .toList()
                .subscribe({ list ->
                    if (!list.isEmpty() && isExistsView)
                        mView.showOffers(list)
                }, this::onError)
    }
*/

}