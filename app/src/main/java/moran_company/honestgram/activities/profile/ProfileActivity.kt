package moran_company.honestgram.activities.profile

import android.os.Bundle
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.data.ItemMenu

/**
 * Created by roman on 23.01.2018.
 */
class ProfileActivity : BaseActivity() {

    override fun getLayoutResId(): Int = R.layout.activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showProfileFragment()
    }

    override fun onResume() {
        super.onResume()
        honestApplication.menuAdapter.setItemChecked(ItemMenu.MENU_TYPE.PROFILE)

    }
}