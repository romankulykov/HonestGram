package moran_company.honestgram.fragments.profile

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import moran_company.honestgram.R
import moran_company.honestgram.data.City
import moran_company.honestgram.fragments.base.BaseMvpFragment
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_profile.*


/**
 * Created by roman on 23.01.2018.
 */
class ProfileFragment : BaseMvpFragment<ProfileMvp.Presenter>(), ProfileMvp.View {
    override fun createPresenter(): ProfileMvp.Presenter = ProfilePresenter(this)

    override fun getLayoutResId(): Int = R.layout.fragment_profile

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.loadCities()

    }

    override fun showCities(list: ArrayList<City>) {
        var listCities = ArrayList<String>()
        /*for (user in list) {
            listCities.add(user.city)
        }*/
        if (list != null) {
            list.mapTo(listCities) { it.city }
            val categoryAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, listCities)

            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            cities.adapter = categoryAdapter
        }
    }




}