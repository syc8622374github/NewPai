package com.cyc.newpai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cyc.newpai.R;

public class SearchToolbar extends RelativeLayout {

    private EditText input;

    public SearchToolbar(Context context) {
        super(context,null);
    }

    public SearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_search,this,true);
        input = view.findViewById(R.id.et_search_input);
    }
}
