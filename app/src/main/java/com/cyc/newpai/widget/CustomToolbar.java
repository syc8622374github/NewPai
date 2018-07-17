package com.cyc.newpai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
    private RelativeLayout rlRightAction1;
    private TextView rightTitle1;

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
        rlRightAction1 = view.findViewById(R.id.rl_right_action1);
        rightTitle1 = view.findViewById(R.id.tv_toolbar_right_title);
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
        rightTitle1.setVisibility(View.GONE);
        iv_right_action1.setVisibility(View.VISIBLE);
        if(imgResId>=0) {
            iv_right_action1.setImageResource(imgResId);
        }
        if(listener!=null) {
            rlRightAction1.setOnClickListener(listener);
            //iv_right_action1.setOnClickListener(listener);
        }
    }

    public void setRightAction1(String text,OnClickListener listener){
        setRightAction1(text,-1,listener);
    }

    public void setRightAction1(String text,int color,OnClickListener listener){
        rightTitle1.setVisibility(View.VISIBLE);
        iv_right_action1.setVisibility(View.GONE);
        if(color>0){
            rightTitle1.setTextColor(getResources().getColor(color));
        }
        if(!TextUtils.isEmpty(text)) {
            rightTitle1.setText(text);
        }
        if(listener!=null) {
            rlRightAction1.setOnClickListener(listener);
            //iv_right_action1.setOnClickListener(listener);
        }
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }
}
