package com.cyc.newpai.ui.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class UserHelpActivity extends BaseActivity {

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_help;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        SubsamplingScaleImageView img = findViewById(R.id.iv_user_help);
        img.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        img.setImage(ImageSource.resource(R.drawable.ic_user_help));
    }
}
