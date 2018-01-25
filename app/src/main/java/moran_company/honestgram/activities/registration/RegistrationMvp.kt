package moran_company.honestgram.activities.registration

import android.location.Location
import moran_company.honestgram.base_mvp.BaseMvp
import moran_company.honestgram.data.Users

/**
 * Created by roman on 13.01.2018.
 */
interface RegistrationMvp {
    interface View : BaseMvp.View {
        fun successRegistration(user : Users)
    }

    interface Presenter : BaseMvp.Presenter {
        fun registration(nickname: String, login: String, password: String,location : Boolean)
    }

}