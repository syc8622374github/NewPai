package com.cyc.newpai.util;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogUtil {

    public static ProgressDialog getProgressDialog(Context context){
        final ProgressDialog pd6 = new ProgressDialog(context);
        pd6.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置水平进度条
        pd6.setCancelable(false);// 设置是否可以通过点击Back键取消
        pd6.setMessage("数据准备中..");
        pd6.show();
        return pd6;
    }

    public static ProgressDialog getCustomProgressDialog(Context context,String msg){
        final ProgressDialog pd6 = new ProgressDialog(context);
        pd6.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置水平进度条
        pd6.setCancelable(true);// 设置是否可以通过点击Back键取消
        pd6.setMessage(msg);
        pd6.show();
        return pd6;
    }
}
