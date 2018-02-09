package moran_company.honestgram.activities.registration

import android.annotation.SuppressLint
import com.google.firebase.iid.FirebaseInstanceId
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moran_company.honestgram.HonestApplication
import moran_company.honestgram.R
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.CustomLocation
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.data.Users
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.*


/**
 * Created by roman on 13.01.2018.
 */
class RegistrationPresenter : BasePresenterImpl<RegistrationMvp.View>, RegistrationMvp.Presenter {

    private var lastId: Long? = 0

    constructor(view: RegistrationMvp.View) : super(view) {

        RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users::class.java))
                .toFlowable()
                .flatMapIterable { users -> users }
                .map({ user -> user.id })
                .toList()
                .subscribe({ users ->
                    if (!users.isEmpty()) lastId = Collections.max(users)
                }, { t -> mView.showToast(t.toString()) })
    }


    @SuppressLint("MissingPermission")
    override fun registration(nickname: String, login: String, password: String, location: Boolean) {
        //mUsersReference.orderByChild("id").equalTo(1).
        var locationUser = CustomLocation(0f, 0f)

        var users = RxFirebaseDatabase.observeSingleValueEvent(mUsersReference, DataSnapshotMapper.listOf(Users::class.java))
                .toFlowable()
                .flatMapIterable({ it -> it })
                .filter({ user -> user.login == login })
                .toList().toFlowable()

        var flowable: Flowable<List<Users>>

        if (location) {
            flowable = ReactiveLocationProvider(HonestApplication.getInstance().getApplicationContext())
                    .lastKnownLocation
                    .map({ locationNew ->
                        locationUser.latitude = locationNew.latitude.toFloat()
                        locationUser.longitude = locationNew.longitude.toFloat()
                    }).doOnError(this::onError)
                    .toFlowable(BackpressureStrategy.LATEST)
                    .flatMap({ item -> users })
                    //.flatMap({ users -> users })
        } else {
            flowable = users
        }

        flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ users ->
                    if (!users.isEmpty()) {
                        //PreferencesData.saveUser(users.get(0))
                        mView.showToast(R.string.already_exist)
                    } else {
                        var token: String? = FirebaseInstanceId.getInstance().getToken()
                        var user = Users(lastId!!.inc(), "Kyiv", "Pecherskiy", nickname, login, password, "", "", token, locationUser,null,null,4)
                        mUsersReference?.push()?.setValue(user)
                        mView.successRegistration(user)
                    }
                }, this::onError, this::onComplete)

    }
}