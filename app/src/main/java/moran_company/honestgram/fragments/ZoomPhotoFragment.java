package moran_company.honestgram.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.github.chrisbanes.photoview.PhotoView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.hannesdorfmann.fragmentargs.bundler.ParcelerArgsBundler;

import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.fragments.base.BaseFragment;

@FragmentWithArgs
public class ZoomPhotoFragment extends BaseFragment {
    public static final String TAG = ZoomPhotoFragment.class.getName();
    private static final String EXTRA_URL = "url";
    private static final String EXTRA_TYPE = "type";

    private PhotoView imgSpare;
    @Arg(bundler = ParcelerArgsBundler.class)
    String url;

    public static ZoomPhotoFragment newInstance(String url) {
        ZoomPhotoFragment fragment = new ZoomPhotoFragmentBuilder(url).build();
        return fragment;
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_zoom_image;
    }


    @Override
    public void onResume() {
        super.onResume();
        //activity.updateMenu(ItemMenu.MENU_TYPE.NONE, null, null);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgSpare = view.findViewById(R.id.FPhotoImage);
        if (!TextUtils.isEmpty(url)) {
            GlideApp.with(this).load(url).into(imgSpare);
        } else {
            imgSpare.setImageResource(R.drawable.unknown);
        }


    }
}
