package com.cyc.newpai.ui.me;


import android.os.Bundle;

import com.cyc.newpai.framework.base.BaseFragment;

public class MeFragment extends BaseFragment {

    public static MeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
