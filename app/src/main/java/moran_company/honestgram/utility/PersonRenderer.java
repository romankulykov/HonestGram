package moran_company.honestgram.utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.cloudipsp.android.Order;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.ItemUserOrder;
import moran_company.honestgram.data.Orders;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 03.08.2017.
 */

public class PersonRenderer extends DefaultClusterRenderer<ItemUserOrder> {
    private ImageView mImageViewFrame;
    private CircleImageView mImageViewProfile;
    private View customMarkerView;
    private int mSizeX;
    private int mSizeH;
    private Activity activity;

    private Polyline polyline;

    private int count = 1;


    public PersonRenderer(Activity activity, GoogleMap mMap, ClusterManager<ItemUserOrder> mClusterManager) {
        super(activity, mMap, mClusterManager);
        this.activity = activity;
        mClusterManager.setOnClusterItemInfoWindowClickListener(item -> {
            count++;
            if (item instanceof Users) {
                if (count % 2 == 0) {
                    Users users = (Users) item;
                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    if (users.getLocationsList() != null && !users.getLocationsList().isEmpty()) {
                        for (int i = 0; i < users.getLocationsList().size(); i++) {
                            LatLng point = users.getLocationsList().get(i).getLatLng();
                            options.add(point);
                        }
                    }
                    polyline = mMap.addPolyline(options);
                } else polyline.remove();
            } else {
                Orders orders = (Orders) item;
                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_show_additional_info, null);

                ImageView imageView = ButterKnife.findById(dialogView, R.id.photo);

                GlideApp.with(activity)
                        .load(orders.getPhotoPlace())
                        .into(imageView);
                DialogUtility.getAlertDialogWithoutButtons(activity,dialogView,"tit'e","text").show();
                /*DialogUtility.getAlertDialogTwoButtons(activity, "title", "text", new DialogUtility.OnDialogButtonsClickListener() {
                    @Override
                    public void onPositiveClick() {

                    }

                    @Override
                    public void onNegativeClick() {

                    }
                }, 0, 0).show();*/
            }
        });
    }

    @Override
    protected void onBeforeClusterItemRendered(ItemUserOrder item, MarkerOptions markerOptions) {
        customMarkerView = activity.getLayoutInflater().inflate(R.layout.profile, null);
        mImageViewFrame = (ImageView) customMarkerView.findViewById(R.id.background_marker);
        mImageViewProfile = (CircleImageView) customMarkerView.findViewById(R.id.main_marker);
        mSizeX = (int) mImageViewProfile.getLayoutParams().width;
        mSizeH = (int) mImageViewProfile.getLayoutParams().height;
        //initUser(item);

        markerOptions.icon(Utility.getBitmap(customMarkerView)).title(item instanceof Users ? ((Users) item).getNickname() : "Order# = " + item.getId());
    }

    @Override
    protected void onClusterItemRendered(final ItemUserOrder item, final Marker marker) {
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
                                marker.setTitle(item instanceof Users ? ((Users) item).getNickname() : "Order# = " + item.getId());
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
    protected boolean shouldRenderAsCluster(Cluster<ItemUserOrder> cluster) {
        return cluster.getSize() > 1;
    }
}
