package moran_company.honestgram.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Goods;

/**
 * Created by roman on 15.02.2018.
 */

public class CartAdapter extends BaseAdapter<Goods, CartAdapter.ViewHolder> {

    private OnRemoveClick onRemoveClick;

    public interface OnRemoveClick {
        void removeClick(int good);
    }

    public void setOnRemoveClickListener(OnRemoveClick onRemoveClick) {
        this.onRemoveClick = onRemoveClick;
    }


    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_cart;
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        if (items.get(position) instanceof Goods) {
            Goods goods = items.get(position);
            if (goods != null)
                holder.bind(goods);
            else {
                holder.itemView.getLayoutParams().height = 0;
                holder.itemView.getLayoutParams().width = 0;
            }
        }
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.photoProduct)
        ImageView photoProduct;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.removeFromCart)
        ImageView removeFromCart;

        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);

            removeFromCart.setOnClickListener(view -> onRemoveClick.removeClick(getAdapterPosition()));
        }

        public void bind(Goods goods) {
            GlideApp.with(context)
                    .load(goods.getUrl())
                    .into(photoProduct);
            title.setText(goods.getTitle());
            price.setText(context.getString(R.string.concrete_price, goods.getPrice()));

        }
    }


}
