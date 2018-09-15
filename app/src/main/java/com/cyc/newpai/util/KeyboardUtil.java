package com.cyc.newpai.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具类
 * Created by chendajian on 2016/7/1 0001.
 */
public class KeyboardUtil {
    private KeyboardUtil(){}

    /**
     * 打开键盘
     * @param context
     * @param editText
     */
    public static void open(Context context, EditText editText){
        if (context == null || editText == null){
            return;
        }
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    /**
     * 关闭软键盘
     * @param context
     * @param editText
     */
    public static void close(Context context, EditText editText){
        if (context == null || editText == null){
            return;
        }
        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 关闭软键盘
     * @param context
     */
    public static void close(Context context, View view){
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 判断软键盘是否已打开
     * @param context
     * @return
     */
    public static boolean isOpend(Context context){
        if (context == null){
            return false;
        }
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }
}
