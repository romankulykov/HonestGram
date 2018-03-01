package moran_company.honestgram.fragments.map

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import moran_company.honestgram.R
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.*
import moran_company.honestgram.utility.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by roman on 03.11.2017.
 */

class MapPresenter(view: MapMvp.View) : BasePresenterImpl<MapMvp.View>(view), MapMvp.Presenter {

    private var fromCache : Boolean = false

    override fun loadUsers() {
        val user = PreferencesData.getUser()
        RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users::class.java))
                .toFlowable().flatMapIterable { usersList -> usersList }
                .filter { users -> users.id != user!!.id }
                .map { users -> users as ItemUserOrder }
                .toList().toFlowable()
                .subscribe { list ->
                    if (isExistsView) {
                        if (!list.isEmpty() && isExistsView)
                            mView.showObjects(list)
                        else
                            mView.showToast(R.string.not_found)
                    }
                }

    }

    override fun loadShippedOrders() {
        //Users user = PreferencesData.INSTANCE.getUser();

        mView.setProgressIndicator(true)
        dbInstance.ordersDao.hasData()
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .flatMap({
                    //fromCache = it>0
                    when(fromCache){
                        false -> fromServer()
                        true -> fromCache()
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe ({ list ->
                    if (isExistsView) {
                        fromCache = true
                        if (!list.isEmpty() && isExistsView)
                            mView.showObjects(list)
                        else
                            mView.showToast(R.string.not_found)
                    }
                },this::onError,this::onComplete)
    }

    fun fromCache() = dbInstance.ordersDao
            .getAllTasks()
            .take(1)


    fun fromServer() = RxFirebaseDatabase.observeSingleValueEvent(mOrdersReference, DataSnapshotMapper.listOf(Orders::class.java))
            .observeOn(Schedulers.newThread())
            .toFlowable().flatMapIterable { usersList -> usersList }
            .filter { it -> it.isShipped!! }
            .map { orders ->
                dbInstance.ordersDao.insertTask(orders)
                orders as ItemUserOrder }
            .toList().toFlowable()
            /*.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe { list ->
                if (isExistsView) {
                    fromCache = true
                    if (!list.isEmpty() && isExistsView)
                        mView.showObjects(list)
                    else
                        mView.showToast(R.string.not_found)
                }
            }
*/

}
