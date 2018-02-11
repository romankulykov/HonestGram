package moran_company.honestgram.fragments.admin_add_product

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import moran_company.honestgram.R
import moran_company.honestgram.fragments.base.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_add_product.*
import moran_company.honestgram.data.PreferencesData


/**
 * Created by roman on 09.02.2018.
 */
class AddProductFragment : BaseMvpFragment<AddProductMvp.Presenter>(), AddProductMvp.View {
    override fun createPresenter(): AddProductMvp.Presenter = AddProductPresenter(this)

    override fun getLayoutResId(): Int = R.layout.fragment_add_product

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
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
    }
}