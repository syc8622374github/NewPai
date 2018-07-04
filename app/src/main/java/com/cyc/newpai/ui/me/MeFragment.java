package com.cyc.newpai.ui.me;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.common.RechargeActivity;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private View view;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        return view;
    }

    private void initView() {
        LinearLayout llMyProperty = view.findViewById(R.id.ll_me_my_property);
        llMyProperty.setOnClickListener(this);
        Button btnRecharge = view.findViewById(R.id.btn_me_recharge);
        btnRecharge.setOnClickListener(this);
    }

    public static String getFlag() {
        return MeFragment.class.getName();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_me_my_property:
                startActivity(new Intent(getContext(), MyPropertyActivity.class));
                break;
            case R.id.btn_me_recharge:
                startActivity(new Intent(getContext(), RechargeActivity.class));
                break;
        }
    }
}
