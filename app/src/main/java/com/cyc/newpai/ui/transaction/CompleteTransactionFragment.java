package com.cyc.newpai.ui.transaction;


import android.os.Bundle;

import com.cyc.newpai.framework.base.BaseFragment;

public class CompleteTransactionFragment extends BaseFragment {

    public static CompleteTransactionFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CompleteTransactionFragment fragment = new CompleteTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
