package com.cyc.newpai.framework.adapter.interfaces;

import com.cyc.newpai.framework.adapter.ViewHolder;

/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnItemChildClickListener<T> {
    void onItemChildClick(ViewHolder viewHolder, T data, int position);
}
