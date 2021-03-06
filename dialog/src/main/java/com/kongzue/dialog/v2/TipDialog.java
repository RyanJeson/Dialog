package com.kongzue.dialog.v2;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.R;
import com.kongzue.dialog.listener.DialogLifeCycleListener;
import com.kongzue.dialog.util.BaseDialog;

public class TipDialog extends BaseDialog {

    public static final int SHOW_TIME_SHORT = 0;
    public static final int SHOW_TIME_LONG = 1;

    public static final int TYPE_CUSTOM_DRAWABLE = -2;
    public static final int TYPE_CUSTOM_BITMAP = -1;
    public static final int TYPE_WARNING = 0;
    public static final int TYPE_ERROR = 1;
    public static final int TYPE_FINISH = 2;

    private AlertDialog alertDialog;
    private static TipDialog tipDialog;
    private Drawable customDrawable;
    private Bitmap customBitmap;
    private boolean isCanCancel = false;

    private Context context;
    private String tip;

    private TipDialog() {
    }

    private int howLong = 0;
    private int type = 0;

    //Fast Function
    public static TipDialog show(Context context, String tip) {
        synchronized (TipDialog.class) {
            if (tipDialog == null) tipDialog = new TipDialog();
            tipDialog.context = context;
            tipDialog.tip = tip;
            tipDialog.howLong = SHOW_TIME_SHORT;
            tipDialog.type = TYPE_WARNING;
            tipDialog.showDialog();
            tipDialog.log("显示等待对话框 -> " + tip);
            return tipDialog;
        }
    }

    public static TipDialog show(Context context, String tip, int howLong, int type) {
        synchronized (TipDialog.class) {
            if (tipDialog == null) tipDialog = new TipDialog();
            tipDialog.context = context;
            tipDialog.tip = tip;
            tipDialog.howLong = howLong;
            tipDialog.type = type;
            tipDialog.showDialog();
            tipDialog.log("显示等待对话框 -> " + tip);
            return tipDialog;
        }
    }

    public static TipDialog show(Context context, String tip, int howLong, Drawable customDrawable) {
        synchronized (TipDialog.class) {
            if (tipDialog == null) tipDialog = new TipDialog();
            tipDialog.context = context;
            tipDialog.tip = tip;
            tipDialog.customDrawable = customDrawable;
            tipDialog.howLong = howLong;
            tipDialog.type = TYPE_CUSTOM_DRAWABLE;
            tipDialog.showDialog();
            tipDialog.log("显示等待对话框 -> " + tip);
            return tipDialog;
        }
    }

    public static TipDialog show(Context context, String tip, int howLong, Bitmap customBitmap) {
        synchronized (TipDialog.class) {
            if (tipDialog == null) tipDialog = new TipDialog();
            tipDialog.context = context;
            tipDialog.tip = tip;
            tipDialog.customBitmap = customBitmap;
            tipDialog.howLong = howLong;
            tipDialog.type = TYPE_CUSTOM_BITMAP;
            tipDialog.showDialog();
            tipDialog.log("显示等待对话框 -> " + tip);
            return tipDialog;
        }
    }

    private RelativeLayout boxInfo;
    private ImageView image;
    private TextView txtInfo;

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.processDialog);
        builder.setCancelable(isCanCancel);

        alertDialog = builder.create();
        if (dialogLifeCycleListener != null) dialogLifeCycleListener.onCreate(alertDialog);
        if (isCanCancel) alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();

        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_tip);

        boxInfo = (RelativeLayout) window.findViewById(R.id.box_info);
        image = (ImageView) window.findViewById(R.id.image);
        txtInfo = (TextView) window.findViewById(R.id.txt_info);

        switch (type) {
            case TYPE_WARNING:
                image.setImageResource(R.mipmap.img_warning);
                break;
            case TYPE_ERROR:
                image.setImageResource(R.mipmap.img_error);
                break;
            case TYPE_FINISH:
                image.setImageResource(R.mipmap.img_finish);
                break;
            case TYPE_CUSTOM_BITMAP:
                image.setImageBitmap(customBitmap);
                break;
            case TYPE_CUSTOM_DRAWABLE:
                image.setImageDrawable(customDrawable);
                break;
        }

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

        int time = 1500;
        switch (howLong) {
            case SHOW_TIME_SHORT:
                time = 1500;
                break;
            case SHOW_TIME_LONG:
                time = 3000;
                break;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog != null) alertDialog.dismiss();
            }
        }, time);
    }

    public TipDialog setCanCancel(boolean canCancel) {
        isCanCancel = canCancel;
        if (alertDialog != null) alertDialog.setCancelable(canCancel);
        return this;
    }

}
