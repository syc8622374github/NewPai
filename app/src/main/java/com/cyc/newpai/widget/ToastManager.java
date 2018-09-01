package com.cyc.newpai.widget;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {

    private static Toast toast;

    public static void showToast(Context context, String msg, int toastTime){
        try {
            if(toast ==null){
                toast = Toast.makeText(context,msg,toastTime);
            }
            toast.setText(msg);
            toast.setDuration(toastTime);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
