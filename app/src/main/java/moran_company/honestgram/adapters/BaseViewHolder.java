package moran_company.honestgram.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import moran_company.honestgram.R;
import moran_company.honestgram.base_mvp.BaseMvp;
import moran_company.honestgram.utility.DialogUtility;
import moran_company.honestgram.utility.Utility;

/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder implements BaseMvp.View {

    protected final Context context;

    private Dialog waitingDialog;

    public BaseViewHolder(OnViewHolderEventListener listener, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        context = itemView.getContext();
        itemView.setOnClickListener(v -> {
            if (listener != null) listener.onViewHolderClick(itemView, getAdapterPosition());
        });
    }

    @Override
    public void onNetworkDisable() {
        showToast(R.string.network_disable_error);
    }

    @Override
    public void onUnauthorized() {
        //LoginActivity.startThisActivity(context);
    }

    @Override
    public void showToast(String text) {
        Utility.showToast(context, text);
    }

    @Override
    public void showToast(int textId) {
        Utility.showToast(context, textId);
    }

    private void showUnknownError() {
        showToast("Something went wrong");
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            if (waitingDialog == null) waitingDialog = DialogUtility.getWaitDialog(
                    context, context.getString(R.string.wait_loading), false);
            waitingDialog.show();

        } else {
            DialogUtility.closeDialog(waitingDialog);
        }
    }

    public interface OnViewHolderEventListener {
        void onViewHolderClick(View itemView, int position);
    }
}
