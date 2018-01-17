package moran_company.honestgram.adapters;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import moran_company.honestgram.R;
import moran_company.honestgram.data.ItemMenu;

/**
 * Created by Kulykov Anton on 9/8/17.
 */


public class MenuAdapter extends BaseAdapter<ItemMenu, MenuAdapter.ViewHolder> {

    private static final String TAG = MenuAdapter.class.getName();
    private final int layout;
    private ItemMenu.MENU_TYPE currentItem = ItemMenu.MENU_TYPE.NONE;

    public MenuAdapter(int layout) {
        this(null, layout);
    }

    public MenuAdapter(List<ItemMenu> items, int layout) {
        super(items);
        this.layout = layout;

    }

    @Override
    protected int getItemLayout(int viewType) {
        return layout;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemMenu itemMenu = items.get(position);
        holder.setViewContent(itemMenu);

    }

    public void setItemChecked(ItemMenu.MENU_TYPE menuType) {
        currentItem = menuType;
        notifyDataSetChanged();
    }


    class ViewHolder extends BaseViewHolder {
        @BindView(R.id.menuTitleTextView)
        TextView mTitleView;
        @BindView(R.id.menuImageView)
        ImageView mMenuImageView;

       /* @BindView(R.id.countTextView)
        TextView mCountTextView;*/


        ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }

        public void setViewContent(ItemMenu itemMenu) {
            if (itemMenu != null) {
                boolean isSelected = itemMenu.getMenuType() == currentItem;
                mTitleView.setTypeface(mTitleView.getTypeface(),
                        !isSelected ? Typeface.NORMAL : Typeface.BOLD);
                if (currentItem != ItemMenu.MENU_TYPE.NONE)
                    mTitleView.setTextColor(ContextCompat.getColor(mTitleView.getContext(),
                            !isSelected ? R.color.blackTextColor : R.color.saffron));
                mTitleView.setText(itemMenu.getIdTitleString());
                mMenuImageView.setImageResource(itemMenu.getIdImageDrawable());
                //mTitleView.setCompoundDrawablesWithIntrinsicBounds(itemMenu.getIdImageDrawable(), 0, 0, 0);
            }
        }


    }
}
