package moran_company.honestgram.fragments.main

import moran_company.honestgram.base_mvp.BaseMvp

/**
 * Created by roman on 12.01.2018.
 */
interface MainMvp {

    interface View : BaseMvp.View{

    }

    interface Presenter : BaseMvp.Presenter{
        fun initUsers()
    }

}