package com.cyc.newpai.ui.category;

import android.os.Bundle;

import com.cyc.newpai.framework.base.BaseFragment;

public class CategoryFragment extends BaseFragment {

    public static CategoryFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
