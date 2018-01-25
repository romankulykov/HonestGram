package moran_company.honestgram.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Goods;

/**
 * Created by roman on 24.01.2018.
 */

public class SpecialOfferAdapter extends PagerAdapter {

    private List<Goods> mItems;
    private OnBannerClick mOnBannerClick;

    public SpecialOfferAdapter(List<Goods> mItems, OnBannerClick mOnBannerClick) {
        this.mItems = mItems;
        this.mOnBannerClick = mOnBannerClick;
    }


    public void setmItems(List<Goods> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems!=null?mItems.size():0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ConstraintLayout view = (ConstraintLayout) View.inflate(container.getContext(), R.layout.item_offer,null);
        ImageView offerImage = view.findViewById(R.id.offerImageView);
        TextView specialPrice = view.findViewById(R.id.specialPrice);
        GlideApp
                .with(offerImage)
                .load(mItems.get(position).getUrl())
                .apply(RequestOptions.bitmapTransform(new MultiTransformation(
                        new CenterCrop(),
                        new RoundedCornersTransformation(offerImage.getResources().getDimensionPixelSize(R.dimen.default_image_rounded_corners), 0))))
                .into(offerImage);
        specialPrice.setText(container.getContext().getString(R.string.concrete_price,mItems.get(position).getPrice()));

        offerImage.setOnClickListener(view1 -> {
            if (mOnBannerClick != null)
                mOnBannerClick.onBannerClick(position);
        });

        container.addView(view,0);
        return view;
    }

    public interface OnBannerClick {
        void onBannerClick(int position);
    }


}
