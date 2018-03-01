package moran_company.honestgram.fragments.admin_add_product

import android.net.Uri
import com.google.android.gms.tasks.OnCompleteListener
import durdinapps.rxfirebase2.DataSnapshotMapper
import durdinapps.rxfirebase2.RxFirebaseDatabase
import durdinapps.rxfirebase2.RxFirebaseStorage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moran_company.honestgram.base_mvp.BasePresenterImpl
import moran_company.honestgram.data.Goods
import moran_company.honestgram.data.PreferencesData
import java.io.File
import java.util.*

/**
 * Created by roman on 09.02.2018.
 */
class AddProductPresenter : BasePresenterImpl<AddProductMvp.View>, AddProductMvp.Presenter {

    private var lastId: Long = 0


    constructor(view: AddProductMvp.View) : super(view) {

        RxFirebaseDatabase.observeSingleValueEvent(mGoodsReference, DataSnapshotMapper.listOf(Goods::class.java))
                .toFlowable()
                .flatMapIterable { users -> users }
                .map({ user -> user.id })
                .toList()
                .subscribe({ users ->
                    if (!users.isEmpty()) lastId = Collections.max(users)
                }, this::onError)
    }


    override fun loadPhoto(path: String) {
        val filePathUri = Uri.fromFile(File(path))
        val nameFile = filePathUri.lastPathSegment + "_product_" + System.currentTimeMillis()
        RxFirebaseStorage.putFile(mStorageReference.child(nameFile), filePathUri)
                .subscribe({ it ->
                    mView.showPhotoNameAndUrl(nameFile, it.downloadUrl.toString())
                },
                        this::onError)
    }

    override fun pushProduct(title: String, photoName: String, photoUrl: String, price: Long, description: String) {
        mGoodsReference.push()
                .setValue(Goods(lastId + 1, title, price, photoUrl, 1, description, PreferencesData.getUser()?.id, 0))
                .addOnCompleteListener({
                    fromServer()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({}, this::onError, {
                                mView.successAddedProduct()
                            })

                })
    }

    fun fromServer() = RxFirebaseDatabase
            .observeSingleValueEvent(mGoodsReference, DataSnapshotMapper.listOf(Goods::class.java))
            .toFlowable()
            .map { list ->
                PreferencesData.saveProducts(list as ArrayList<Goods>)
                for (goods in list) {
                    dbInstance.productsDao.insertTask(goods)
                }
                list as List<Goods>
            }

}