package com.cyc.newpai.ui.me;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.Util;
import com.cyc.newpai.framework.adapter.ViewHolder;
import com.cyc.newpai.framework.adapter.base.CommonBaseAdapter;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.me.entity.MyLuckyTimeBean;
import com.cyc.newpai.util.DateUtil;
import com.cyc.newpai.util.GsonManager;
import com.cyc.newpai.util.ViewUtil;
import com.cyc.newpai.widget.LoadingFooter;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyLuckTimeActivity extends BaseActivity {

    private static final String TAG = MyLuckTimeActivity.class.getSimpleName();
    private RecyclerView rvList;
    private MyLuckTimeRecyclerAdapter myLuckTimeRecyclerAdapter;
    private int pageSize = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_sun_drying_acitivity;
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        params.put("p",String.valueOf(pageSize++));
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_MY_LUCKY_LIST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<MyLuckyTimeBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<MyLuckyTimeBean>>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult().getList() != null) {
                            handler.post(()->{
                                myLuckTimeRecyclerAdapter.setData(responseBean.getResult().getList());
                            });
                            return;
                        }
                    }
                    handler.post(() -> ToastManager.showToast(MyLuckTimeActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void initView() {
        rvList = findViewById(R.id.rv_my_sun_drying);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        myLuckTimeRecyclerAdapter = new MyLuckTimeRecyclerAdapter(this, null, true);
        //初始化 开始加载更多的loading View
        myLuckTimeRecyclerAdapter.setLoadingView(ViewUtil.getFootView(this, LoadingFooter.State.Loading));
        //加载失败，更新footer view提示
        myLuckTimeRecyclerAdapter.setLoadFailedView(ViewUtil.getFootView(this, LoadingFooter.State.NetWorkError));
        //加载完成，更新footer view提示
        myLuckTimeRecyclerAdapter.setLoadEndView(ViewUtil.getFootView(this, LoadingFooter.State.TheEnd));
        myLuckTimeRecyclerAdapter.setEmptyView(Util.inflate(this,R.layout.layout_emptyview, (ViewGroup) rvList.getParent(),false));
        myLuckTimeRecyclerAdapter.setOnLoadMoreListener(isReload -> loadMoreData());
        rvList.setAdapter(myLuckTimeRecyclerAdapter);
    }

    private void loadMoreData() {
        Map<String, String> params = new HashMap<>();
        params.put("p", String.valueOf(pageSize++));
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_MY_LUCKY_LIST, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<MyLuckyTimeBean>> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<MyLuckyTimeBean>>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult().getList() != null) {
                            if(responseBean.getResult().getList().size()>0){
                                handler.post(()->myLuckTimeRecyclerAdapter.setLoadMoreData(responseBean.getResult().getList()));
                            }else{
                                handler.post(()->myLuckTimeRecyclerAdapter.loadEnd());
                            }
                            return;
                        }
                    }
                    handler.post(()->ToastManager.showToast(MyLuckTimeActivity.this, "数据加载失败", Toast.LENGTH_LONG));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    public class MyLuckTimeRecyclerAdapter extends CommonBaseAdapter<MyLuckyTimeBean>{

        public MyLuckTimeRecyclerAdapter(Context context, List<MyLuckyTimeBean> datas, boolean isOpenLoadMore) {
            super(context, datas, isOpenLoadMore);
        }

        @Override
        protected void convert(ViewHolder holder, MyLuckyTimeBean data, int position) {
            try {
                ImageView avator = holder.getView(R.id.iv_sun_drying_avator);
                GlideApp.with(mContext).load(data.getImages()).placeholder(R.drawable.ic_avator_default).into(avator);
                holder.setText(R.id.tv_sun_drying_name,data.getNickname());
                holder.setText(R.id.tv_sun_drying_date,DateUtil.formatDate(Long.valueOf(data.getAdd_time()),"yyyy-MM-dd HH:mm"));
                holder.setText(R.id.tv_sun_drying_shop_name,data.getGoods_image());
                holder.setText(R.id.tv_sun_drying_shop_deal_price, Html.fromHtml("成交价：<font color='"+getResources().getColor(R.color.colorPrimary)+"'>"+data.getDeal_price()+"</font>").toString());
                ImageView shopIcon = holder.getView(R.id.iv_sun_drying_shop_icon);
                GlideApp.with(mContext).load(data.getGoods_image()).placeholder(R.drawable.shop_iphonex).into(shopIcon);
                ImageView shopPic = holder.getView(R.id.tv_sun_drying_sun_pic);
                GlideApp.with(mContext).load(data.getImages().get(0)).into(shopIcon);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected int getItemLayoutId() {
            return R.layout.list_my_sun_drying_item;
        }
    }
}
