package moran_company.honestgram.activities.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.internal.WebDialog
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
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
import com.google.firebase.internal.FirebaseAppHelper.getUid
import com.google.firebase.auth.FirebaseUser


/**
 * Created by roman on 12.01.2018.
 */
open class LoginActivity : BaseMvpActivity<LoginMvp.Presenter>(), LoginMvp.View {

    private var count: Int = 0

    private var mAuth: FirebaseAuth? = null

    var mCallbackManager: CallbackManager? = null
// ...
// Initialize Firebase Auth

    override fun createPresenter(): LoginMvp.Presenter = LoginPresenter(this)

    //private val sendingChangedLocation = SendingChangedLocation()


    override fun getLayoutResId(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startLocationService()

        mAuth = FirebaseAuth.getInstance()

        if (PreferencesData.getUser() != null) {
            var users = PreferencesData.getUser()
            BaseActivity.newInstance(this, MainActivity::class.java, true)
        }
        enter.setOnClickListener({ view ->
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

        forgotPasswordTextView.setOnClickListener({ view ->
            mPresenter.forgotPassword()
        })

        mCallbackManager = CallbackManager.Factory.create()
        loginButton.setReadPermissions("email", "public_profile")
        loginButton.registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("TAG", "facebook:onSuccess:" + loginResult)
                handleFacebookAccessToken(loginResult.accessToken)
            }

            override fun onCancel() {
                Log.d("TAG", "facebook:onCancel")

            }

            override fun onError(error: FacebookException) {
                Log.d("TAG", "facebook:onError", error)

            }
        })


    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("TAG", "handleFacebookAccessToken:" + token)

        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        val user = mAuth?.getCurrentUser()
                        updateUI(user)
                        Log.d("TAG", "signInWithCredential:success")
                    } else {
                        Log.d("TAG", "signInWithCredential:false")
                        updateUI(null)
                    }

                }
        //
        /*?.addOnCompleteListener(object : OnCompleteListener<AuthResult>() {
            fun onComplete(@NonNull task: Task<AuthResult>) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(BaseMvpActivity.TAG, "signInWithCredential:success")
                    val user = mAuth.getCurrentUser()
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(BaseMvpActivity.TAG, "signInWithCredential:failure", task.getException())
                    Toast.makeText(this@FacebookLoginActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }

                // ...
            }
        })*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
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
        mCallbackManager?.onActivityResult(requestCode, resultCode, data);
    }

    override fun successLogin(user: Users?) {
        var resId: Int
        if (user != null) {
            showToast(R.string.found)
            BaseActivity.newInstance(this, MainActivity::class.java, true)
            restartLocationService()
        } else
            showToast(R.string.not_found)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
        } else {

        }
    }


}