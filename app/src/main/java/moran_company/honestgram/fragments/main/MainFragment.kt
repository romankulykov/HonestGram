package moran_company.honestgram.fragments.main

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_main.*
import moran_company.honestgram.R
import moran_company.honestgram.activities.base.BaseActivity
import moran_company.honestgram.adapters.MenuAdapter
import moran_company.honestgram.data.Goods
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
        mPresenter.init()
        initMenuAdapter()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.loadOffers()
        /*var user = PreferencesData.getUser()
        var sizeCart = user?.cart?.size?:0
        mBaseActivity.setBasketCounter(sizeCart)*/

    }

    private fun initMenuAdapter(){
        mainMenuList.isNestedScrollingEnabled = false
        var menu : MutableList<ItemMenu> = ArrayList()
        //mBaseActivity.navigationDrawerFragment.setProfile(PreferencesData.getUser())

        menu.add(ItemMenu(R.string.product,android.R.color.white,ItemMenu.MENU_TYPE.PRODUCTS))
        menu.add(ItemMenu(R.string.about,android.R.color.white,ItemMenu.MENU_TYPE.ABOUT))
        menu.add(ItemMenu(R.string.cities_and_hoods,android.R.color.white,ItemMenu.MENU_TYPE.CITIES_AND_HOODS))
        menu.add(ItemMenu(R.string.menu_map,android.R.color.white,ItemMenu.MENU_TYPE.MAP))
        menu.add(ItemMenu(R.string.chats,android.R.color.white,ItemMenu.MENU_TYPE.CHATS))
        menu.add(ItemMenu(R.string.share,android.R.color.white,ItemMenu.MENU_TYPE.SHARE))

        mMenuAdapter.setOnItemClickListener({view,item ->
            //BaseActivity.showActivity(activity,item)
            BaseActivity.showActivity(context, item)

            //mBaseActivity.showDialogsFragment()

        })

        /*pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(i: Int) {
                //    PhotoFragment myFragment = (PhotoFragment) pagerAdapter.instantiateItem(pager, i);
                //    url = myFragment.getUrl();
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })*/




        mMenuAdapter.items = menu
        mainMenuList.adapter = mMenuAdapter
    }

    override fun showOffers(goods: List<Goods>) {
       /* var adapter = SpecialOfferAdapter(goods as ArrayList,{clickPosition ->

        })
        pager.adapter = adapter
        indicator.setViewPager(pager)
        adapter.registerDataSetObserver(indicator.dataSetObserver)*/
        specialOffer.setmGoods(goods as java.util.ArrayList<Goods>)


        //specialOffer.setmGoods(goods as ArrayList<Goods>)

    }



}