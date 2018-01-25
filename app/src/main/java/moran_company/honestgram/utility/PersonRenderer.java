package moran_company.honestgram.utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import de.hdodenhof.circleimageview.CircleImageView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 03.08.2017.
 */

public class PersonRenderer extends DefaultClusterRenderer<Users> {
    private ImageView mImageViewFrame;
    private CircleImageView mImageViewProfile;
    private View customMarkerView;
    private int mSizeX;
    private int mSizeH;
    private Activity activity;

    private Polyline polyline;

    private int count = 1;


    public PersonRenderer(Activity activity, GoogleMap mMap, ClusterManager<Users> mClusterManager) {
        super(activity, mMap, mClusterManager);
        this.activity = activity;
        mClusterManager.setOnClusterItemInfoWindowClickListener(users -> {
            count++;
            if (count % 2 == 0) {
                PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                if (users.getLocations() != null && !users.getLocations().isEmpty()) {
                    for (int i = 0; i < users.getLocations().size(); i++) {
                        LatLng point = users.getLocations().get(i).getLatLng();
                        options.add(point);
                    }
                }
                polyline = mMap.addPolyline(options);
            } else polyline.remove();
        });
    }

    @Override
    protected void onBeforeClusterItemRendered(Users item, MarkerOptions markerOptions) {
        customMarkerView = activity.getLayoutInflater().inflate(R.layout.profile, null);
        mImageViewFrame = (ImageView) customMarkerView.findViewById(R.id.background_marker);
        mImageViewProfile = (CircleImageView) customMarkerView.findViewById(R.id.main_marker);
        mSizeX = (int) mImageViewProfile.getLayoutParams().width;
        mSizeH = (int) mImageViewProfile.getLayoutParams().height;
        //initUser(item);
        markerOptions.icon(Utility.getBitmap(customMarkerView)).title(item.getNickname());
    }

    @Override
    protected void onClusterItemRendered(final Users item, final Marker marker) {
        if (!TextUtils.isEmpty(item.getPhotoURL()))
            GlideApp.with(activity)
                    .asBitmap()
                    .load(item.getPhotoURL())
                    .fitCenter().centerCrop()
                    .placeholder(R.drawable.unknown)
                    .into(new SimpleTarget<Bitmap>(mSizeX, mSizeH) {
                        @Override
                        public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                            try {
                                mImageViewProfile.setImageBitmap(resource);
                                marker.setIcon(Utility.getBitmap(customMarkerView));
                                marker.setTitle(item.getNickname());
                            } catch (Exception exception) {
                            }
                        }
                    });
        else {
            mImageViewProfile.setImageResource(R.drawable.unknown);
            marker.setIcon(Utility.getBitmap(customMarkerView));
        }

    }



    /*private void initUser(User item) {

        if (!gender.equals(UNKNOWN)) {
            mImageViewFrame.setImageResource(gender.equals(FEMALE) ?
                    R.drawable.ic_pin_woman : R.drawable.ic_pin_man);
            mImageViewProfile.setImageResource(gender.equals(FEMALE) ?
                    R.drawable.ic_woman_user_image_stub : R.drawable.ic_man_user_image_stub);
        } else {
            mImageViewFrame.setImageResource(R.drawable.ic_pin_man);
            mImageViewProfile.setImageResource(R.drawable.stub_image_user);
        }
    }*/

    @Override
    protected boolean shouldRenderAsCluster(Cluster<Users> cluster) {
        return cluster.getSize() > 1;
    }
}
