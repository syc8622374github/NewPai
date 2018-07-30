package com.cyc.newpai.util;

import android.content.Context;
import android.view.View;

import com.cyc.newpai.widget.LoadingFooter;

public class ViewUtil {

    public static View getFootView(Context context,LoadingFooter.State state) {
        LoadingFooter mFooterView = null;
        if (mFooterView == null) {
            mFooterView = new LoadingFooter(context);
            mFooterView.setState(state);
        }
        return mFooterView;
    }
}
