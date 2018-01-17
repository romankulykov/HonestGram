package moran_company.honestgram.activities.login

import moran_company.honestgram.base_mvp.BaseMvp
import moran_company.honestgram.data.Users

/**
 * Created by roman on 12.01.2018.
 */
interface LoginMvp {

    interface View : BaseMvp.View{
        fun successLogin(user : Users?)
    }

    interface Presenter : BaseMvp.Presenter{
        fun signIn(login : String,password : String)
        fun registration(login: String,password: String)
        fun forgotPassword()
    }

}