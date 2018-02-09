package moran_company.honestgram.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.Urls;

/**
 * Created by roman on 24.01.2018.
 */

public class SpecialOfferAdapter<T> extends PagerAdapter {

    private List<T> mItems;
    private List<String> urls;
    private List<Goods> goods;
    private OnBannerClick mOnBannerClick;

    private boolean mWithRoundedCorners;

    public SpecialOfferAdapter(List<T> mItems, OnBannerClick mOnBannerClick) {
        this.mItems = mItems;
        this.mOnBannerClick = mOnBannerClick;
    }

    public void setItems(List<T> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public void setGoods(List<Goods> goods){
        this.goods = goods;
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public void setWithRoundedCorners(boolean withRoundedCorners) {
        mWithRoundedCorners = withRoundedCorners;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ConstraintLayout view = (ConstraintLayout) View.inflate(container.getContext(), R.layout.item_offer, null);
        ImageView offerImage = view.findViewById(R.id.offerImageView);
        TextView specialPrice = view.findViewById(R.id.specialPrice);
        LinearLayout specialPriceLayout = view.findViewById(R.id.specialPriceLayout);
        if (mItems.get(position) instanceof Goods) {
            Goods product = (Goods) mItems.get(position);
            GlideApp
                    .with(offerImage)
                    .load(product.getUrl())
                    .apply(
                            mWithRoundedCorners ? RequestOptions.bitmapTransform(new MultiTransformation(
                                    new CenterCrop(),
                                    new RoundedCornersTransformation(offerImage.getResources().getDimensionPixelSize(R.dimen.default_image_rounded_corners), 0)))
                                    : RequestOptions.centerCropTransform())
                    .into(offerImage);
            if (product.getSpecialPrice() != 0) {
                specialPriceLayout.setVisibility(View.VISIBLE);
                specialPrice.setText(container.getContext().getString(R.string.concrete_price, product.getSpecialPrice()));
            } else specialPriceLayout.setVisibility(View.GONE);
        }
        if (mItems.get(position) instanceof Urls){
            String url = ((Urls) mItems.get(position)).getUrl();
            GlideApp
                    .with(offerImage)
                    .load(url)
                    .apply(
                            mWithRoundedCorners ? RequestOptions.bitmapTransform(new MultiTransformation(
                                    new CenterCrop(),
                                    new RoundedCornersTransformation(offerImage.getResources().getDimensionPixelSize(R.dimen.default_image_rounded_corners), 0)))
                                    : RequestOptions.centerCropTransform())
                    .into(offerImage);
        }

        offerImage.setOnClickListener(view1 -> {
            if (mOnBannerClick != null)
                mOnBannerClick.onBannerClick(position);
        });

        container.addView(view, 0);
        return view;
    }

    public interface OnBannerClick {
        void onBannerClick(int position);
    }


}
