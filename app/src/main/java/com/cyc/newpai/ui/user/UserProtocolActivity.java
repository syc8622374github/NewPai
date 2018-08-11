package com.cyc.newpai.ui.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.util.StringUtil;

import org.w3c.dom.Text;

public class UserProtocolActivity extends BaseActivity {

    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = findViewById(R.id.tv_user_protocol_content);
        content.setText(Html.fromHtml(getResources().getString(R.string.user_protocol_content)));
        //content.setMovementMethod(ScrollingMovementMethod.getInstance());
        //content.setText(StringUtil.ToSBC(getResources().getString(R.string.user_protocol)));
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_protocol;
    }


}
