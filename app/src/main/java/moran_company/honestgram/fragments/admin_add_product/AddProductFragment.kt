package moran_company.honestgram.fragments.admin_add_product

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import moran_company.honestgram.R
import moran_company.honestgram.fragments.base.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_add_product.*
import moran_company.honestgram.GlideApp
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.utility.ImageFilePath
import java.io.File


/**
 * Created by roman on 09.02.2018.
 */
class AddProductFragment : BaseMvpFragment<AddProductMvp.Presenter>(), AddProductMvp.View {

    var photoName: String? = null
    var photoUrl: String? = null

    override fun createPresenter(): AddProductMvp.Presenter = AddProductPresenter(this)

    override fun getLayoutResId(): Int = R.layout.fragment_add_product

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val citiesList = PreferencesData.loadCities()
        var listCities = ArrayList<String>()
        /*for (user in list) {
            listCities.add(user.city)
        }*/
        if (citiesList != null) {
            citiesList.mapTo(listCities) { it.city }
            val categoryAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, listCities)

            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cities.adapter = categoryAdapter
        }

        photo.setOnClickListener({
            CropImage.activity()
                    //.setRequestedSize(Constants.PROFILE_WIDTH, Constants.PROFILE_HEIGHT)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(context!!, this)
        })

        addProduct.setOnClickListener({
            var price = price.text.toString()
            var description = description.text.toString()
            var title = title.text.toString()
            if (TextUtils.isEmpty(photoName) && TextUtils.isEmpty(photoUrl)) {
                when (TextUtils.isEmpty(price) && TextUtils.isEmpty(description) && TextUtils.isEmpty(title)) {
                    true -> showToast(R.string.empty_fields)
                    false -> showToast(R.string.empty_photo)
                }
            } else {
                mPresenter.pushProduct(title,photoName!!, photoUrl!!, price.toLong(), description)
            }

        })
    }

    override fun successAddedProduct() {
        showToast(R.string.success_add_product)
        onBackPressed()
    }

    override fun showPhotoNameAndUrl(photoName: String, photoUrl: String) {
        this.photoName = photoName
        this.photoUrl = photoUrl
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val selectedImagePath: String?
                val imageUri = result.uri

                //MEDIA GALLERY
                selectedImagePath = ImageFilePath.getPath(context, imageUri)
                mPresenter.loadPhoto(selectedImagePath)
                GlideApp.with(context!!)
                        .load(Uri.fromFile(File(selectedImagePath)))
                        .into(photo)
                Log.i("Image File Path", "" + selectedImagePath!!)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

}