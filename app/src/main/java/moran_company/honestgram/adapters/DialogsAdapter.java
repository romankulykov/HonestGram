package moran_company.honestgram.adapters;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class DialogsAdapter extends BaseAdapter<List<Dialogs>, DialogsAdapter.ViewHolder> {


    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_dialog;
    }

    @Override
    public void onBindViewHolder(DialogsAdapter.ViewHolder holder, int position) {
        Dialogs lastMessage = items.get(position).get(items.get(position).size() - 1);
        holder.lastMessage.setText(lastMessage.getMessage());
        Users users = PreferencesData.INSTANCE.getUser();
        Users otherUser = Utility.getUserById(lastMessage.getUser_id(), PreferencesData.INSTANCE.getUsers());
        if (otherUser != null)
            GlideApp.with(holder.context)
                    .load(otherUser.getPhotoURL())
                    .placeholder(R.drawable.round_frame_selector)
                    .into(holder.avatarProfile);
        holder.whoWrite.setText(lastMessage.getUser_id() == users.getId() ? holder.context.getString(R.string.you_write) : otherUser.getNickname());
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.lastMessage)
        TextView lastMessage;
        @BindView(R.id.whoWrite)
        TextView whoWrite;
        @BindView(R.id.avatarProfile)
        CircleImageView avatarProfile;

        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }
    }
}
