package moran_company.honestgram.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 24.01.2018.
 */

public class ProductsAdapter extends BaseAdapter<Goods, ProductsAdapter.ViewHolder> {
    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_product;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    private OnBasketClickLister onBasketClickLister;

    public interface OnBasketClickLister {
        void addBasketClick(Goods goods);
    }

    public void setOnBasketClickLister(OnBasketClickLister onBasketClickLister) {
        this.onBasketClickLister = onBasketClickLister;
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.photoProduct)
        ImageView photoProduct;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.toFavourite)
        ImageView toFavourite;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.addToCart)
        ImageView addToCart;

        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }

        public void bind(Goods good) {
            GlideApp
                    .with(context)
                    .load(good.getUrl())
                    .into(photoProduct);

            title.setText(good.getTitle());
            price.setText(context.getString(R.string.concrete_price, good.getPrice()));
            city.setText(Utility.getCityById(good.getCityId()));

        }

        @OnClick(R.id.addToCart)
        void addToCartClick(){
            onBasketClickLister.addBasketClick(items.get(getAdapterPosition()));
        }

    }
}
