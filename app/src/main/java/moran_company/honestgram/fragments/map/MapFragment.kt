package moran_company.honestgram.fragments.map

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.clustering.ClusterManager
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import moran_company.honestgram.R
import moran_company.honestgram.data.ItemUserOrder
import moran_company.honestgram.data.Orders
import moran_company.honestgram.data.PreferencesData
import moran_company.honestgram.data.Users
import moran_company.honestgram.data.enums.TYPE_USER
import moran_company.honestgram.eventbus.UpdateMap
import moran_company.honestgram.fragments.base.BaseMvpFragment
import moran_company.honestgram.utility.DebugUtility
import moran_company.honestgram.utility.PersonRenderer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by roman on 03.11.2017.
 */

//@FragmentWithArgs
class MapFragment : BaseMvpFragment<MapMvp.Presenter>(), MapMvp.View, OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mClusterManager: ClusterManager<ItemUserOrder>? = null


    private val usersList: List<Users>? = null
    private val ordersList: List<Orders>? = null
    private var objectsList: List<ItemUserOrder>? = null

    //  @Arg(required = false)
    var statusId: Long? = null


    override fun createPresenter(): MapMvp.Presenter {
        return MapPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.loadUsers()
    }

    override fun getLayoutResId(): Int {
        return R.layout.fragment_map
    }

    override fun onResume() {
        super.onResume()
        setUpMap()

        if (objectsList != null) {
            if (objectsList!![0] is Users)
                mPresenter.loadUsers()
            else
                mPresenter.loadShippedOrders()
        }
    }

    private fun setUpMap() {
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        Log.d("MapFragment", "setUpMap")
    }


    override fun onMapReady(googleMap: GoogleMap) {
        if (mMap != null) {
            return
        }
        mMap = googleMap
        val users = PreferencesData.getUser()
        if (users?.getTypeUser() === TYPE_USER.ADMIN)
            mMap?.setOnMapLongClickListener { latLng -> mBaseActivity.showGoodsToShipFragment(latLng) }
        val uiSettings = mMap?.uiSettings
        uiSettings?.isZoomControlsEnabled = true
        uiSettings?.isMyLocationButtonEnabled = true
        startDemo()
    }

    protected fun startDemo() {
        mClusterManager = ClusterManager(context!!, mMap)
        mClusterManager?.renderer = PersonRenderer(mBaseActivity, mMap, mClusterManager)
        mMap?.setOnCameraIdleListener { asynchronousAddMarkers() }
        mMap?.setOnMarkerClickListener(mClusterManager)
        mMap?.setOnInfoWindowClickListener(mClusterManager)

    }


    /*@Override
    public void showObjects(List<Users> usersList) {
        this.usersList = usersList;
        if (mMap != null) {
            asynchronousAddMarkers(usersList);
        }

    }

    @Override
    public void showShippedOrders(List<Orders> list) {
        this.ordersList = list;
        if (mMap != null) {
            asynchronousAddMarkers(ordersList);
        }
    }*/

    override fun showObjects(objectsList: List<ItemUserOrder>) {
        this.objectsList = objectsList
        if (mMap != null) {
            asynchronousAddMarkers()
        }
    }


    /*private fun asynchronousAddMarkers() {
       var latLngBounds = mMap?.projection?.visibleRegion?.latLngBounds
       DynamicallyAddMarkerTask().execute(latLngBounds!!)
    }*/

    /*private fun asynchronousAddMarkers() {
       var latLngBounds = mMap?.projection?.visibleRegion?.latLngBounds
       //DynamicallyAddMarkerTask().execute(latLngBounds!!)
        Flowable.just(objectsList)
                .map { it ->
                    mClusterManager!!.clearItems()
                    it }
                .flatMapIterable{it -> it}
                .filter { latLngBounds?.contains(it.position)!! }
                .map { it -> mClusterManager?.addItem(it)  }
                .observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({it -> it},this::onError,this::onComplete)

    }*/
    private fun asynchronousAddMarkers() {
        launch(UI) {
            val adding = launch(UI) {
                val latLngBounds = mMap?.projection?.visibleRegion?.latLngBounds
                if (mClusterManager != null) mClusterManager!!.clearItems()
                if (objectsList != null)
                    if (objectsList?.isNotEmpty()!!) {
                            for (currentObject in objectsList!!) {
                                if (latLngBounds!!.contains(currentObject.position)) {
                                    mClusterManager?.addItem(currentObject)
                                }
                            }
                    }
            }
            adding.join() // important since wait after adding Job is completed
            mClusterManager?.cluster()
        }
    }

    /*@SuppressLint("StaticFieldLeak")
    internal inner class DynamicallyAddMarkerTask : AsyncTask<LatLngBounds, Void, Boolean>() {
        override fun doInBackground(vararg bounds: LatLngBounds): Boolean {
            if (mClusterManager != null) mClusterManager!!.clearItems()
            if (objectsList != null)
                if (objectsList?.isNotEmpty()!!) {
                    for (currentObject in objectsList!!) {
                        if (bounds[0].contains(currentObject.position)) {
                            mClusterManager?.addItem(currentObject)
                        }
                    }
                }
            return true
        }

        override fun onPostExecute(result: Boolean) {
            mClusterManager?.cluster()
        }
    }*/


    private fun onError(throwable: Throwable) {
        showToast(throwable.toString())
        throwable.printStackTrace()
        DebugUtility.logE("MapFragment", throwable.toString())
    }

    fun onNext(latLngBounds: LatLngBounds?) {
        if (mClusterManager != null) mClusterManager!!.clearItems()
        if (objectsList != null)
            if (objectsList?.isNotEmpty()!!) {
                for (currentObject in objectsList!!) {
                    if (latLngBounds!!.contains(currentObject.position)) {
                        mClusterManager?.addItem(currentObject)
                    }
                }
            }
    }

    fun onComplete() {
        mClusterManager?.cluster()
    }

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onRightMenuClick(ItemMenu itemMenu) {
        if (itemMenu != null){
            // TODO not good idea
            mBaseActivity.setSecondBarTitle("Choise "+itemMenu.getTitle());
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUpdateProfile(update: UpdateMap?) {
        DebugUtility.logTest("MapFragment", "onUpdateProfile")
        if (update != null)
            when (update.typeAction) {
                UpdateMap.TYPE_ACTION.UPDATE -> onResume()
                UpdateMap.TYPE_ACTION.PEOPLE_ORDERS ->
                    //TODO
                    mPresenter.loadShippedOrders()
            }
        /*  if ((updateProfileEvent.checkLogin() == null && !(this instanceof MainActivity)))
                finish();*/
    }

    /*companion object {

        val TAG = MapFragment::class.java.name

        private val LOCATION_PERMISSIONS_CODE = 1379

        fun newInstance(statusId: Long) = MapFragmentBuilder().statusId(statusId);

    }*/


    /*class DynamicallyAddMarkerTask extends AsyncTask<LatLngBounds, Void, Void> {
        protected Void doInBackground(LatLngBounds... bounds) {
            if (mClusterManager != null) mClusterManager.clearItems();
            if (usersList != null)
                if (usersList.size() > 0) {
                    for (Users currentUser : usersList) {
                        if (bounds[0].contains(currentUser.getPosition())) {
                            mClusterManager.addItem(currentUser);
                        }
                    }
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mClusterManager.cluster();
        }
    }*/
}
