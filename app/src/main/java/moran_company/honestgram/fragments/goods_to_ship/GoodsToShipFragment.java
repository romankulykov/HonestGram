package moran_company.honestgram.fragments.goods_to_ship;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.MagicalPermissions;
import com.google.android.gms.maps.model.LatLng;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.activities.MainActivity;
import moran_company.honestgram.adapters.OrdersAdapter;
import moran_company.honestgram.data.Orders;
import moran_company.honestgram.eventbus.UpdateDialogs;
import moran_company.honestgram.eventbus.UpdateMap;
import moran_company.honestgram.fragments.base.BaseDialogFragment;
import moran_company.honestgram.utility.DialogUtility;

/**
 * Created by roman on 14.01.2018.
 */

@FragmentWithArgs
public class GoodsToShipFragment extends BaseDialogFragment<GoodsToShipMvp.Presenter> implements GoodsToShipMvp.View {


    @BindView(R.id.orders)
    RecyclerView orders;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.takePhoto)
    ImageView takePhoto;
    @BindView(R.id.flipper)
    ViewFlipper flipper;

    private MagicalPermissions magicalPermissions;
    private MagicalCamera magicalCamera;

    private final static int FLIPPER_LIST = 0;
    private final static int FLIPPER_TAKE_PHOTO = 1;

    private OrdersAdapter ordersAdapter = new OrdersAdapter();

    @Arg(bundler = ParcelerArgsBundler.class)
    LatLng latLng;
    private Orders mOrder;
    private String mPath;


    @Override
    protected GoodsToShipMvp.Presenter createPresenter() {
        return new GoodsToShipPresenter(this);
    }

    public static GoodsToShipFragment newInstance(LatLng latLng) {
        GoodsToShipFragment fragment = new GoodsToShipFragmentBuilder(latLng).build();
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_goods_to_ship, null);
        ButterKnife.bind(this, v);
        Dialog dialog = DialogUtility
                .getAlertDialogWithoutButtons(getContext(), v, getString(R.string.attention), getString(R.string.choose_companion));
        flipper.setDisplayedChild(FLIPPER_LIST);

        orders.setAdapter(ordersAdapter);
        ordersAdapter.setOnItemClickListener((itemView, order) -> {
            this.mOrder = order;
            //mPresenter.shipOrder(latLng, order);
            flipper.setDisplayedChild(FLIPPER_TAKE_PHOTO);
        });
        mPresenter.getOrders();

        return dialog;
    }

    @OnClick(R.id.takePhoto)
    void takePhoto(){
        String[] permissions = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        magicalPermissions = new MagicalPermissions(this, permissions);

        magicalCamera = new MagicalCamera(mBaseActivity,0, magicalPermissions);

        //take photo
        magicalCamera.takeFragmentPhoto(this);

        //select picture
        //magicalCamera.selectFragmentPicture(this, "My Header Example");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //CALL THIS METHOD EVER
        //magicalCamera.resultPhoto(requestCode, resultCode, data);

        //this is for rotate picture in this method
        magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_90);

        //with this form you obtain the bitmap (in this example set this bitmap in image view)
        //takePhoto.setImageBitmap(magicalCamera.getPhoto());
        GlideApp.with(getContext())
                .load(magicalCamera.getPhoto())
                .override(takePhoto.getWidth(),takePhoto.getHeight())
                .into(takePhoto);

        //if you need save your bitmap in device use this method and return the path if you need this
        //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
        String path = magicalCamera.savePhotoInMemoryDevice(magicalCamera.getPhoto(),"myPhotoName","myDirectoryName", MagicalCamera.JPEG, true);
        this.mPath = path;
        if(path != null){
            Toast.makeText(getContext(), "The photo is save in device, please check this path: " + path, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Sorry your photo dont write in devide, please contact with fabian7593@gmail and say this error", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Map<String, Boolean> map = magicalPermissions.permissionResult(requestCode, permissions, grantResults);
        for (String permission : map.keySet()) {
            Log.d("PERMISSIONS", permission + " was: " + map.get(permission));
        }
        //Following the example you could also
        //locationPermissions(requestCode, permissions, grantResults);
    }
    
    @OnClick(R.id.okChoice)
    void okChoice(){
        String commentText = comment.getText().toString();
        mPresenter.shipOrder(latLng, mOrder,commentText,mPath);
        EventBus.getDefault().post(new UpdateMap(UpdateMap.TYPE_ACTION.UPDATE));
        dismiss();

    }
    
    @OnClick(R.id.cancelChoice)
    void cancelChoice(){
        dismiss();
    }

    @Override
    public void showOrders(List<Orders> orders) {
        ordersAdapter.setItems(orders);
    }

    @Override
    public void successShipped() {
        //mBaseActivity.showChatFragment();
        EventBus.getDefault().post(new UpdateMap(UpdateMap.TYPE_ACTION.UPDATE));

        dismiss();
    }

}
