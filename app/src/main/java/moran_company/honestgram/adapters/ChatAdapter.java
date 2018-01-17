package moran_company.honestgram.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;


public class ChatAdapter extends BaseAdapter<Dialogs, ChatAdapter.ViewHolder> {

    private static final int LAYOUT_MY_MESSAGE = 0;

    private static final int LAYOUT_FRIEND_MESSAGE = 1;
    private static final int LAYOUT_OPENED_MESSAGE = 2;
    private Users mUserProfile = PreferencesData.INSTANCE.getUser();

    @Override
    public int getItemViewType(int position) {
        Dialogs chatMessage = items.get(position);
        return chatMessage.getMessage_id() == 0 ? LAYOUT_OPENED_MESSAGE : ((chatMessage.getUser_id() == mUserProfile.getId()) ? LAYOUT_MY_MESSAGE : LAYOUT_FRIEND_MESSAGE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == LAYOUT_OPENED_MESSAGE) view = inflater.inflate(
                R.layout.list_item_opened_message, parent, false);
        else if (viewType == LAYOUT_MY_MESSAGE) view = inflater.inflate(
                R.layout.list_item_my_message, parent, false);
        else view = inflater.inflate(R.layout.list_item_friend_message, parent, false);
        return new ViewHolder(this, view);
    }

    @Override
    protected int getItemLayout(int viewType) {
        return 0;
    }

    public void addNewMessage(Dialogs chatMessage) {
       /* items.add(0,chatMessage);
        notifyItemInserted(0);*/
        items.add(chatMessage);
        notifyItemInserted(items.size() - 1);

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Dialogs chatMessage = items.get(position);
        if (chatMessage.getMessage_id() != 0) {
            holder.messageBody.setText(chatMessage.getMessage());
            holder.messageTime.setText(chatMessage.getTimestamp() + "");
        } else {
            holder.itemView.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        }
    }

    public class ViewHolder extends BaseViewHolder {

        @BindView(R.id.messageBody)
        TextView messageBody;
        @BindView(R.id.messageTime)
        TextView messageTime;

        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }
    }
}
