package com.cyc.newpai;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.category.CategoryFragment;
import com.cyc.newpai.ui.common.NotificationMessageActivity;
import com.cyc.newpai.ui.common.SearchActivity;
import com.cyc.newpai.ui.main.HomeFragment;
import com.cyc.newpai.ui.me.MeFragment;
import com.cyc.newpai.ui.me.SettingActivity;
import com.cyc.newpai.ui.transaction.CompleteTransactionFragment;
import com.cyc.newpai.util.DataGenerator;
import com.cyc.newpai.util.LoginUtil;
import com.cyc.newpai.widget.ToastManager;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private BaseFragment []mFragments;
    private TabLayout mTabLayout;
    private static int showPosition;
    private boolean isRestoreActivity;
    private String showFragmentTag;
    private long firstTime = 0l;

    public static void startAct(Context context){
        startAct(context,false);
    }

    public static void startAct(Context context,boolean isSingleTask){
        Intent intent = new Intent(context,MainActivity.class);
        if(isSingleTask){
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        context.startActivity(new Intent(context,MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mFragments = DataGenerator.getFragments("");
        if(savedInstanceState!=null){
            isRestoreActivity = savedInstanceState.getBoolean("isRestoreActivity");
            showFragmentTag = savedInstanceState.getString("showFragmentTag");
        }
        mTabLayout = findViewById(R.id.tl_tab_layout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                for (int i=0;i<mTabLayout.getTabCount();i++){
                    View view = mTabLayout.getTabAt(i).getCustomView();
                    ImageView icon = view.findViewById(R.id.tab_content_image);
                    TextView text = view.findViewById(R.id.tab_content_text);
                    if(i == tab.getPosition()){ // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }else{// 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }
                ctb_toolbar.setTitle(((TextView)tab.getCustomView().findViewById(R.id.tab_content_text)).getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // 提供自定义的布局添加Tab
        for(int i=0;i<4;i++){
            mTabLayout.addTab(mTabLayout.newTab().setCustomView(DataGenerator.getMainBottomTabView(this,i)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for(Fragment fragment : mFragments){
            fragmentTransaction.remove(fragment);
        }
        fragmentTransaction.commitNowAllowingStateLoss();*/
        outState.putBoolean("isRestoreActivity",true);
        outState.putString("showFragmentTag",mFragments[showPosition].getClass().getName());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG,"newIntent");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"newIntent");
        if(LoginUtil.isLogin(this)&& mFragments[showPosition] instanceof MeFragment){
            ((MeFragment)mFragments[showPosition]).initData();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG,"onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setLeftAction1(R.drawable.ic_search,null);
        ctb_toolbar.setRightAction1(R.drawable.ic_notification,null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void onTabItemSelected(int position){
        BaseFragment fragment = mFragments[position];
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(isRestoreActivity){
            Fragment oldShowFragment = getSupportFragmentManager().findFragmentByTag(showFragmentTag);
            if(oldShowFragment!=null){
                fragmentTransaction.remove(oldShowFragment);
            }
            isRestoreActivity = false;
        }
        if(fragment!=null&&!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container,fragment,fragment.getClass().getName());
        }
        if(mFragments[showPosition]!=null){
            fragmentTransaction.hide(mFragments[showPosition]);
        }
        fragmentTransaction.show(fragment).commit();
        showPosition = position;
        ctb_toolbar.setTitle(getTitle().toString());
        if(fragment instanceof CategoryFragment){
            ctb_toolbar.divider.setVisibility(View.GONE);
            ctb_toolbar.setLeftAction1(0,null);
            ctb_toolbar.setRightAction1(0,null);
        }else if(fragment instanceof CompleteTransactionFragment){
            ctb_toolbar.setLeftAction1(0,null);
            ctb_toolbar.setRightAction1(0,null);
        }else if(fragment instanceof MeFragment){
            ctb_toolbar.divider.setVisibility(View.GONE);
            ctb_toolbar.setTitle("");
            ctb_toolbar.setLeftAction1(R.drawable.ic_me_setting, v -> startActivityForResult(new Intent(MainActivity.this, SettingActivity.class),0));
            ctb_toolbar.setRightAction1(R.drawable.ic_notification, v -> startActivity(new Intent(MainActivity.this, NotificationMessageActivity.class)));
        }else{
            //ctb_toolbar.divider.setVisibility(View.VISIBLE);
            ctb_toolbar.setLeftAction1(R.drawable.ic_search, v -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));
            ctb_toolbar.setRightAction1(R.drawable.ic_notification, v -> startActivity(new Intent(MainActivity.this, NotificationMessageActivity.class)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            mFragments[showPosition].review();
        }
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if(secondTime - firstTime > 2000){
            ToastManager.showToast(this,"再按一次退出",Toast.LENGTH_SHORT);
            firstTime = secondTime;
        }else{
            finish();
            super.onBackPressed();
        }
    }
}
