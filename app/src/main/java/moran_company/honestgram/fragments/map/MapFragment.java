package moran_company.honestgram.fragments.map;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import moran_company.honestgram.R;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.fragments.base.BaseMvpFragment;
import moran_company.honestgram.utility.PersonRenderer;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 03.11.2017.
 */

//@FragmentWithArgs
public class MapFragment extends BaseMvpFragment<MapMvp.Presenter> implements MapMvp.View, OnMapReadyCallback {

    public static final String TAG = MapFragment.class.getName();

    private GoogleMap mMap;
    private ClusterManager<Users> mClusterManager;

    private List<Users> usersList;


    private static final int LOCATION_PERMISSIONS_CODE = 1379;


    @Override
    protected MapMvp.Presenter createPresenter() {
        return new MapPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_map;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMap();
        mPresenter.loadUsers();
    }


    private void setUpMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG, "setUpMap");
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        startDemo();
    }

    protected void startDemo() {
        mClusterManager = new ClusterManager<Users>(getContext(), mMap);
        mClusterManager.setRenderer(new PersonRenderer(mBaseActivity, mMap, mClusterManager));
        mMap.setOnCameraIdleListener(() -> asynchronousAddMarkers());
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);

    }


    @Override
    public void showUsers(List<Users> usersList) {
        this.usersList = usersList;
        if (mMap != null) {
            asynchronousAddMarkers();
        }

    }

    private void asynchronousAddMarkers() {
        Flowable.just(mMap.getProjection().getVisibleRegion().latLngBounds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onNext, throwable -> showToast(throwable.toString()),
                        this::onComplete);
    }

    public void onNext(LatLngBounds latLngBounds) {
        if (mClusterManager != null) mClusterManager.clearItems();
        if (usersList != null)
            if (usersList.size() > 0) {
                for (Users currentUser : usersList) {
                    if (latLngBounds.contains(currentUser.getPosition())) {
                        mClusterManager.addItem(currentUser);
                    }
                }
            }
    }

    public void onComplete() {
        mClusterManager.cluster();
    }


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
