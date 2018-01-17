package moran_company.honestgram.fragments.main

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.adapters.MenuAdapter
import moran_company.honestgram.data.ItemMenu
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.fragments.base.BaseMvpFragment

/**
 * Created by roman on 12.01.2018.
 */
class MainFragment : BaseMvpFragment<MainMvp.Presenter>(),MainMvp.View{

    private var mMenuAdapter = MenuAdapter(R.layout.list_item_main_menu)

    override fun createPresenter():
            MainMvp.Presenter = MainPresenter(this)

    override fun getLayoutResId(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.initUsers()
        initMenuAdapter()
    }

    private fun initMenuAdapter(){
        mainMenuList.isNestedScrollingEnabled = false
        var menu : MutableList<ItemMenu> = ArrayList()
        mBaseActivity.navigationDrawerFragment.setProfile(PreferencesData.getUser())

        menu.add(ItemMenu(R.string.product,android.R.color.white,ItemMenu.MENU_TYPE.PRODUCTS))
        menu.add(ItemMenu(R.string.about,android.R.color.white,ItemMenu.MENU_TYPE.ABOUT))
        menu.add(ItemMenu(R.string.cities_and_hoods,android.R.color.white,ItemMenu.MENU_TYPE.CITIES_AND_HOODS))
        menu.add(ItemMenu(R.string.contacts,android.R.color.white,ItemMenu.MENU_TYPE.CONTACTS))
        menu.add(ItemMenu(R.string.share,android.R.color.white,ItemMenu.MENU_TYPE.SHARE))

        mMenuAdapter.setOnItemClickListener({view,item ->
            //BaseActivity.showActivity(activity,item)
            mBaseActivity.showDialogsFragment()

        })

        mMenuAdapter.items = menu
        mainMenuList.adapter = mMenuAdapter
    }

}