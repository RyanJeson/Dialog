package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.util.BaseDialog;

public class WaitDialog extends BaseDialog {

    private AlertDialog alertDialog;
    private static WaitDialog waitDialog;
    private boolean isCanCancel = false;

    private Context context;
    private String tip;

    private WaitDialog() {
    }

    public static WaitDialog show(Context context, String tip) {
        synchronized (WaitDialog.class) {
            if (waitDialog == null) waitDialog = new WaitDialog();
            waitDialog.context = context;
            waitDialog.tip = tip;
            waitDialog.showDialog();
            waitDialog.log("显示等待对话框 -> " + tip);
            return waitDialog;
        }
    }

    private RelativeLayout boxInfo;
    private TextView txtInfo;

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.processDialog);
        builder.setCancelable(isCanCancel);

        alertDialog = builder.create();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_progressbar);

        boxInfo = (RelativeLayout) window.findViewById(R.id.box_info);
        txtInfo = (TextView) window.findViewById(R.id.txt_info);

        if (!tip.isEmpty()) {
            boxInfo.setVisibility(View.VISIBLE);
            txtInfo.setText(tip);
        } else {
            boxInfo.setVisibility(View.GONE);
        }
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (dialogLifeCycleListener != null) dialogLifeCycleListener.onDismiss();
            }
        });
        alertDialog.show();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onShow(alertDialog);
    }

    public WaitDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }

    public void dismiss(){
        if (alertDialog != null) alertDialog.dismiss();;
    }
}
