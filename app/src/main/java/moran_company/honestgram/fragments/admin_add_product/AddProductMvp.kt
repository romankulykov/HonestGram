package moran_company.honestgram.fragments.admin_add_product

import moran_company.honestgram.base_mvp.BaseMvp

/**
 * Created by roman on 09.02.2018.
 */
interface AddProductMvp {
    interface View : BaseMvp.View{
        fun showPhotoNameAndUrl(photoName : String,photoUrl : String)
        fun successAddedProduct()
    }

    interface Presenter : BaseMvp.Presenter{
        fun loadPhoto(path : String)
        fun pushProduct(title : String,photoName: String, photoUrl: String, price: Long, description: String)
    }
}