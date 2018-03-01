package moran_company.honestgram.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Orders;

/**
 * Created by roman on 15.02.2018.
 */

public class OrdersAdapter extends BaseAdapter<Orders, OrdersAdapter.ViewHolder> {
    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_order;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Orders orders = items.get(position);
        holder.bind(orders);
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv)
        ImageView imageView;
        @BindView(R.id.title)
        TextView title;

        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }

        public void bind(Orders orders) {
            if (orders.getGoods() != null && !orders.getGoods().isEmpty())
                /*for (int i = 0; i < orders.getGoods().size(); i++) {
                    Goods good = orders.getGoods().get(i);
                    if (good != null && !TextUtils.isEmpty(good.getUrl()))
                        GlideApp.with(context)
                                .asBitmap()
                                .load(orders.getGoods().get(i).getUrl())
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        multiImageView.addImage(resource);
                                    }
                                });
                }*/

                GlideApp.with(context)
                        .load(orders.getPhotoURL())
                        .into(imageView);

            title.setText("Orders = " + orders.getId());
        }
    }
}
