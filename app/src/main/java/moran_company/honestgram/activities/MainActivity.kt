package moran_company.honestgram.activities

import android.os.Bundle
import android.os.PersistableBundle
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.data.ItemMenu

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
        honestApplication.menuAdapter.setItemChecked(ItemMenu.MENU_TYPE.MAIN)
    }


}
