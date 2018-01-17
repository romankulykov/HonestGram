package moran_company.honestgram.activities.registration

import android.os.Bundle
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_registration.*
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.activities.base.BaseMvpActivity
import moran_company.honestgram.activities.login.LoginActivity
import moran_company.honestgram.data.Users


/**
 * Created by roman on 13.01.2018.
 */
class RegistrationActivity : BaseMvpActivity<RegistrationMvp.Presenter>(), RegistrationMvp.View {

    override fun createPresenter(): RegistrationMvp.Presenter = RegistrationPresenter(this)

    override fun getLayoutResId(): Int = R.layout.activity_registration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        registration.setOnClickListener({view ->
            val nickname = nickname.text.toString()
            val login = login.text.toString()
            val password = password.text.toString()
            if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password))
                showToast(R.string.empty_fields)
            else
                mPresenter.registration(nickname,login, password)
        })

    }

    override fun successRegistration(user: Users) {
        BaseActivity.newInstance(this, LoginActivity::class.java, true)
    }



}