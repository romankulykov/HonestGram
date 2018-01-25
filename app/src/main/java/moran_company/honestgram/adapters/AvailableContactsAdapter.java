package moran_company.honestgram.adapters;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Users;

/**
 * Created by roman on 14.01.2018.
 */

public class AvailableContactsAdapter extends BaseAdapter<Users,AvailableContactsAdapter.ViewHolder>{
    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_avaibable_contact;
    }

    @Override
    public void onBindViewHolder(AvailableContactsAdapter.ViewHolder holder, int position) {
        GlideApp.with(holder.context)
                .load(items.get(position).getPhotoURL())
                .placeholder(R.drawable.unknown)
                .into(holder.avatarProfile);
        holder.nickname.setText(items.get(position).getNickname());
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.avatarProfile)
        CircleImageView avatarProfile;
        @BindView(R.id.nickname)
        TextView nickname;
        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }
    }
}
