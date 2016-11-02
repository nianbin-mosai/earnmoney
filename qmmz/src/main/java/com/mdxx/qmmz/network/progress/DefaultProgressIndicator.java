package com.mdxx.qmmz.network.progress;

import android.content.Context;

import com.mdxx.qmmz.common.LoadingDialog;


/**
 * Created by Rays on 16/5/12.
 */
public class DefaultProgressIndicator implements IProgressIndicator {
    private LoadingDialog dialog;
    private Context context;

    public static DefaultProgressIndicator newInstance(Context context) {
        return new DefaultProgressIndicator(context);
    }

    public DefaultProgressIndicator(Context context) {
        this.context = context;
    }

    @Override
    public void showProgress() {
        if (dialog == null) {
            dialog = new LoadingDialog(context);
        }
        dialog.show();
    }

    @Override
    public void showProgress(String message) {
        showProgress();
        dialog.setMessages(message);
    }

    @Override
    public void dismissProgress() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
