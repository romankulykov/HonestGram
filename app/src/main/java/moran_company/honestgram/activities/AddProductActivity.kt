package moran_company.honestgram.activities

import android.os.Bundle
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity

/**
 * Created by roman on 09.02.2018.
 */

class AddProductActivity : BaseActivity() {
    override fun getLayoutResId(): Int = R.layout.activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showAddProductFragment()
    }

}
