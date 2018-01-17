package moran_company.honestgram.utility;


import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import moran_company.honestgram.R;


/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class DialogUtility {

    private final static String TAG = DialogUtility.class.getName();


    public static Dialog getAlertDialogWithoutButtons(Context context, View view, String title, String text) {
        return new AlertDialog.Builder(context, R.style.Dialog)//
                .setView(view)
                .setCancelable(true)
                .create();
    }

    public static Dialog getAlertDialogTwoButtons(Context context, String title, String text, final OnDialogButtonsClickListener onDialogButtonsClickListener, int okResId, int cancelResId) {
        return new AlertDialog.Builder(context, R.style.Dialog)
                .setTitle(title)
                .setMessage(text)
                .setPositiveButton(okResId <= 0 ? R.string.ok : okResId, (dialog, which) -> {
                    Log.d(TAG,"AlertDialogTwoButtons");
                    dialog.dismiss();
                    if (onDialogButtonsClickListener != null)
                        onDialogButtonsClickListener.onPositiveClick();
                })
                .setNegativeButton(cancelResId <= 0 ? R.string.cancel : cancelResId, (dialog, which) -> {
                    Log.d(TAG,"AlertDialogTwoButtons Negative");
                    dialog.dismiss();
                    if (onDialogButtonsClickListener != null)
                        onDialogButtonsClickListener.onNegativeClick();
                }).create();
    }

    public static Dialog getWaitDialog(Context context, String text, boolean isShow) {
        return getWaitDialog(context, text, isShow, false);
    }

    public static Dialog getWaitDialog(Context context, String text, boolean isShow, boolean cancelable) {
        Dialog dialog = new Dialog(context, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_wait);
        dialog.setCancelable(cancelable);
        //CircularProgressView progressBarCircular = (CircularProgressView) dialog.findViewById(R.id.progressBarCircular);

        TextView textTextView =  dialog.findViewById(R.id.textTextView);
        if (text != null && text.length() != 0) {
            textTextView.setVisibility(View.VISIBLE);
            textTextView.setText(text);
        } else {
            textTextView.setVisibility(View.GONE);
        }
        try {
            if (isShow) {
                try {
                    dialog.show();
                } catch (Exception ignored) {

                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dialog;
    }
    public static void closeDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
    public interface OnDialogButtonsClickListener {
        void onPositiveClick();

        void onNegativeClick();
    }


}
