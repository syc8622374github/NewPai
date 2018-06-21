package com.cyc.newpai.ui.main;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.ui.main.adapter.HomeRecyclerViewAdapter;
import com.cyc.newpai.ui.main.adapter.HomeWindowRecyclerViewAdapter;
import com.cyc.newpai.ui.main.entity.HomeBean;
import com.cyc.newpai.ui.main.entity.HomeWindowBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance() {
        
        Bundle args = new Bundle();
        
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String[] shopCategorys = new String[]{"正在热拍","我在拍","我的收藏"};

    private HomeViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        //RecyclerView rvMain = view.findViewById(R.id.rv_main);
        //RecyclerViewHeader header = view.findViewById(R.id.header);
        Banner banner = view.findViewById(R.id.banner);
        initBanner(banner);
        RecyclerView window = view.findViewById(R.id.rv_window);
        initWindow(window);
        TabLayout tabLayout = view.findViewById(R.id.tl_home_shop_category);
        // 提供自定义的布局添加Tab
        for(String title : shopCategorys){
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());
                // Tab 选中之后，改变各个Tab的状态
                /*for (int i=0;i<tabLayout.getTabCount();i++){
                    View view = tabLayout.getTabAt(i).getCustomView();
                    ImageView icon = (ImageView) view.findViewById(R.id.tab_content_image);
                    TextView text = (TextView) view.findViewById(R.id.tab_content_text);
                    if(i == tab.getPosition()){ // 选中状态
                        icon.setImageResource(DataGenerator.mTabResPressed[i]);
                        text.setTextColor(getResources().getColor(android.R.color.black));
                    }else{// 未选中状态
                        icon.setImageResource(DataGenerator.mTabRes[i]);
                        text.setTextColor(getResources().getColor(android.R.color.darker_gray));
                    }
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (savedInstanceState == null) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fl_home_shop_content, HomeCategoryShoppingFragment.newInstance())
                    .commitNow();
        }
        /*rvMain.setLayoutManager(new GridLayoutManager(getContext(),2));
        header.attachTo(rvMain);*/
        //init(rvMain);
        return view;
    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = HomeCategoryShoppingFragment.newInstance();
                break;
            case 1:
                fragment = HomeCategoryShoppingFragment.newInstance();
                break;
            case 2:
                fragment = HomeCategoryShoppingFragment.newInstance();
                break;
        }
        if(fragment!=null) {
            getChildFragmentManager().beginTransaction().replace(R.id.fl_home_shop_content,fragment).commit();
        }
    }

    private void init(RecyclerView rvMain) {
        //rvMain.setLayoutManager(new GridLayoutManager(getContext(),2));
        HomeRecyclerViewAdapter adapter  = new HomeRecyclerViewAdapter(rvMain);
        List<HomeBean> beanList = new ArrayList<>();
        beanList.add(new HomeBean());
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        beanList.add(new HomeBean(R.drawable.shop_iphonex,"00:00:10","暂未拍得"));
        adapter.setListNotify(beanList);
        rvMain.setAdapter(adapter);
    }

    private void initWindow(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),5));
        HomeWindowRecyclerViewAdapter adapter  = new HomeWindowRecyclerViewAdapter(recyclerView);
        List<HomeWindowBean> beanList = new ArrayList<>();
        beanList.add(new HomeWindowBean("师徒分享", R.drawable.ic_home_window_share));
        beanList.add(new HomeWindowBean("大转盘",R.drawable.ic_home_window_turntable));
        beanList.add(new HomeWindowBean("每日签到",R.drawable.ic_home_window_sign));
        beanList.add(new HomeWindowBean("充值",R.drawable.ic_home_window_recharge));
        beanList.add(new HomeWindowBean("幸运晒单",R.drawable.ic_home_window_luckytime));
        adapter.setListNotify(beanList);
        recyclerView.setAdapter(adapter);
    }

    private void initBanner(Banner banner) {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.isAutoPlay(true);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                //Glide.with(context).load(path).into(imageView);
                imageView.setBackgroundResource(R.drawable.test111);
            }
        });
        List<String> images = new ArrayList<>();
        images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");
        images.add("111");
        banner.setImages(images);
        banner.start();
        banner.startAutoPlay();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel

    }

}
