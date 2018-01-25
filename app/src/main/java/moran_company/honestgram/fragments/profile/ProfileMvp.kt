package moran_company.honestgram.fragments.profile

import moran_company.honestgram.base_mvp.BaseMvp
import moran_company.honestgram.data.City

/**
 * Created by roman on 23.01.2018.
 */
interface ProfileMvp {

    interface View : BaseMvp.View{
        fun showCities(list: ArrayList<City>)

    }

    interface Presenter : BaseMvp.Presenter{
        fun loadCities()

    }

}