package com.cyc.newpai.framework.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2016/2/22 0022 15:15
 * ViewHolder的简便写法
 */
public class VH {

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
