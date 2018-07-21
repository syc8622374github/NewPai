package com.cyc.newpai.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v13.view.inputmethod.EditorInfoCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.R;
import com.cyc.newpai.ui.common.SearchActivity;

public class SearchToolbar extends RelativeLayout {

    private EditText input;
    private SearchListener searchListener;

    public SearchToolbar(Context context) {
        super(context,null);
    }

    public interface SearchListener{
        void search(String key);
    }

    public SearchToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.toolbar_search,this,true);
        input = view.findViewById(R.id.et_search_input);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    ((InputMethodManager)input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken()
                            ,InputMethodManager.HIDE_NOT_ALWAYS);
                    String key = input.getText().toString().trim();
                    if(!TextUtils.isEmpty(key)){
                        searchListener.search(key);
                    }else{
                        ToastManager.showToast(context,"请输入关键词", Toast.LENGTH_SHORT);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setSearchListener(SearchListener searchListener) {
        this.searchListener = searchListener;
    }
}
