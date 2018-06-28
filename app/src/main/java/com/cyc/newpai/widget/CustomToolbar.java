package com.cyc.newpai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyc.newpai.R;

public class CustomToolbar extends RelativeLayout {

    public ImageButton iv_left_action1;
    public ImageButton iv_right_action1;
    public TextView tv_title;
    public View divider;
    private Drawable leftIcon;
    private Drawable rightIcon;

    public CustomToolbar(Context context) {
        super(context,null);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs,R.styleable.CustomToolbar));
        View view = LayoutInflater.from(getContext()).inflate(R.layout.custom_toolbar,this,true);
        iv_left_action1 = view.findViewById(R.id.iv_left_action1);
        iv_left_action1.setImageDrawable(leftIcon);
        iv_right_action1 = view.findViewById(R.id.iv_right_action1);
        iv_right_action1.setImageDrawable(rightIcon);
        tv_title = view.findViewById(R.id.tv_title);
        divider = view.findViewById(R.id.v_divider);
    }

    private void parseAttributes(TypedArray typedArray) {
        leftIcon = typedArray.getDrawable(R.styleable.CustomToolbar_leftIcon);
        rightIcon = typedArray.getDrawable(R.styleable.CustomToolbar_rightIcon);
    }

    public void setLeftAction1(int imgResId,OnClickListener listener){
        if(imgResId>=0)
            iv_left_action1.setImageResource(imgResId);
        if(listener!=null)
            iv_left_action1.setOnClickListener(listener);
    }

    public void setRightAction1(int imgResId,OnClickListener listener){
        if(imgResId>=0)
            iv_right_action1.setImageResource(imgResId);
        if(listener!=null)
            iv_right_action1.setOnClickListener(listener);
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }
}
