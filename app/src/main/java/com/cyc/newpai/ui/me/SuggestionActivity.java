package com.cyc.newpai.ui.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;

public class SuggestionActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_suggestion;
    }
}
