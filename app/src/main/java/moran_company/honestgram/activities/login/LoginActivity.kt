package moran_company.honestgram.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.activity_login.*
import moran_company.honestgram.R
import moran_company.honestgram.activities.MainActivity
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.activities.base.BaseMvpActivity
import moran_company.honestgram.activities.registration.RegistrationActivity
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.data.Users
import moran_company.honestgram.utility.ImageFilePath


/**
 * Created by roman on 12.01.2018.
 */
open class LoginActivity : BaseMvpActivity<LoginMvp.Presenter>(), LoginMvp.View {

    override fun createPresenter(): LoginMvp.Presenter = LoginPresenter(this)

    override fun getLayoutResId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (PreferencesData.getUser()!=null) {
            var users = PreferencesData.getUser()
            BaseActivity.newInstance(this, MainActivity::class.java, true)
        }
        registration.setOnClickListener({ view ->
            mPresenter.signIn(login.text.toString(), password.text.toString())
        })

        registrationTextView.setOnClickListener({ view ->
            /*val login = login.text.toString()
            val password = password.text.toString()
            if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password))
                showToast(R.string.empty_fields)
            else
                mPresenter.registration(login, password)*/
            BaseActivity.newInstance(this, RegistrationActivity::class.java, true)

        })

        forgotPasswordTextView.setOnClickListener({view ->
            /*CropImage.activity()
                    //.setRequestedSize(Constants.PROFILE_WIDTH, Constants.PROFILE_HEIGHT)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(132, 170)
                    .start(this)*/
            mPresenter.forgotPassword()
        })
    }

    override fun  onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val selectedImagePath: String
                val imageUri = result.uri

                //MEDIA GALLERY
                selectedImagePath = ImageFilePath.getPath(this, imageUri)
                Log.i("Image File Path", "" + selectedImagePath)
                mPresenter.forgotPassword()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    override fun successLogin(user: Users?) {
        var resId: Int
        if (user != null) {
            showToast(R.string.found)
            PreferencesData.saveUser(user)
            BaseActivity.newInstance(this, MainActivity::class.java,true)
        } else
            showToast(R.string.not_found)
    }


}