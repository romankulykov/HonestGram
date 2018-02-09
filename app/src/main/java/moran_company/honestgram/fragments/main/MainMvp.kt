package moran_company.honestgram.fragments.main

import moran_company.honestgram.base_mvp.BaseMvp
import moran_company.honestgram.data.Goods

/**
 * Created by roman on 12.01.2018.
 */
interface MainMvp {

    interface View : BaseMvp.View{
        fun showOffers(goods : List<Goods>)
    }

    interface Presenter : BaseMvp.Presenter{
        fun init()
        fun loadOffers()
        fun loadCities()
        fun loadProducts()
    }

}