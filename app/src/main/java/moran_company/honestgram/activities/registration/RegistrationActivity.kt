package moran_company.honestgram.activities.registration

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_registration.*
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.activities.base.BaseMvpActivity
import moran_company.honestgram.activities.login.LoginActivity
import moran_company.honestgram.data.Users
import moran_company.honestgram.utility.DialogUtility
import moran_company.honestgram.utility.Utility


/**
 * Created by roman on 13.01.2018.
 */
class RegistrationActivity : BaseMvpActivity<RegistrationMvp.Presenter>(), RegistrationMvp.View {

    override fun createPresenter(): RegistrationMvp.Presenter = RegistrationPresenter(this)

    override fun getLayoutResId(): Int = R.layout.activity_registration

    private val LOCATION_PERMISSIONS_CODE = 1079


    override fun onResume() {
        super.onResume()
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            registration(true)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enter.setOnClickListener({ view ->
            if (!Utility.checkLocationPermissions(this)) {
                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    statusCheck()
                } else {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSIONS_CODE)
                }
            }else{
                statusCheck()
            }
        })

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSIONS_CODE) {
            var granted = false
            for (grantResult in grantResults)
                granted = granted or (grantResult == PackageManager.PERMISSION_GRANTED)
            if (granted) {
                statusCheck()
            } else {
                Utility.showToast(this, R.string.necessary_gps)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun statusCheck(){
        val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            DialogUtility.buildAlertMessageNoGps(this, object : DialogUtility.OnDialogButtonsClickListener {
                override fun onPositiveClick() {

                }

                override fun onNegativeClick() {
                    registration(false)
                }
            }).show()
        } else registration(true)
    }

    fun registration(isLocation: Boolean) {
        val nick = nickname.text.toString()
        val log = login.text.toString()
        val pass = password.text.toString()
        if (TextUtils.isEmpty(log) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(nick))
            showToast(R.string.empty_fields)
        else
            mPresenter.registration(nick, log, pass, isLocation)
    }

    override fun successRegistration(user: Users) {
        showToast(R.string.success_registration)
        BaseActivity.newInstance(this, LoginActivity::class.java, false)
    }


}