package moran_company.honestgram.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.data.ItemMenu
import moran_company.honestgram.data.NotificationBody
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.data.SenderNotification
import moran_company.honestgram.utility.ApiCoroutineTest
import moran_company.honestgram.utility.FirebaseContants
import moran_company.honestgram.utility.RetrofitUtils.provideOkHttpClient
import moran_company.honestgram.utility.Utility
import okhttp3.internal.Util
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : BaseActivity() {

    override fun getLayoutResId(): Int {
        return R.layout.activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showMainFragment()
    }

    override fun onResume() {
        super.onResume()
        if (!Utility.checkWriteExternalPermissions(this))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1239)
            }
        coroutines()
        honestApplication.menuAdapter.setItemChecked(ItemMenu.MENU_TYPE.MAIN)
    }


    fun coroutines() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://swapi.co/")
                .client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

        var apiService = retrofit.create(ApiCoroutineTest::class.java)

        launch {

            var c = launch(UI) {
                delay(2000)
                try {
                    val r : Any = apiService.listReposWithCoroutines().await()
                    Log.d("TEST", r.toString())
                    val r1 : Any = apiService.listPeopleWithCoroutines().await()
                    Log.d("TEST", r1.toString())
                    val sender = SenderNotification(PreferencesData.getUser()!!.token, NotificationBody("coroutines"), 1)
                    val notification : Any = apiService
                            .sendNotification("key=" + FirebaseContants.AUTH_KEY_FCM, sender).await()
                    Log.d("TEST", notification.toString())
                } catch (t: Throwable) {
                    t.printStackTrace()
                    showToast(t.toString())
                }
            }
            Log.d("TEST", "BEFORE JOIN")
            c.join()
            Log.d("TEST", "AFTER JOIN")
        }
        Log.d("TEST", "MAIN THREAD")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == 1239) {
            var granted = false
            for (grantResult in grantResults)
                granted = granted or (grantResult == PackageManager.PERMISSION_GRANTED)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
