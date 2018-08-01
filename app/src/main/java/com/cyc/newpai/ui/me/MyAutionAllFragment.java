package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.main.HomeShopDetailActivity;
import com.cyc.newpai.ui.me.adapter.MyAutionAllRecyclerViewAdapter;
import com.cyc.newpai.ui.me.entity.MyAuctionBean;
import com.cyc.newpai.util.ScreenUtil;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyAutionAllFragment extends BaseFragment {

    public static final String TAG = MyAutionAllFragment.class.getSimpleName();
    private View view;
    private MyAuctionActivity activity;
    private MyAutionAllRecyclerViewAdapter myAutionAllRecyclerViewAdapter;
    private RecyclerView autionList;
    private Timer timer;
    private int pageSize=1;

    public static MyAutionAllFragment newInstance() {
        Bundle args = new Bundle();
        MyAutionAllFragment fragment = new MyAutionAllFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MyAuctionActivity) getActivity();
        Log.i(TAG,"onCreate"+ activity.getAuctionType());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG,"onAttach");
    }

    @Override
    protected View getLoadingTargetView() {
        return autionList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list,container,false);
        initView();
        initVaryView();
        Log.i(TAG,"onCreateView" + activity.getAuctionType());
        return view;
    }

    private void initView() {
        autionList = view.findViewById(R.id.list);
        autionList.setLayoutManager(new LinearLayoutManager(getContext()));
        //recyclerView.addItemDecoration(new CommItemDecoration(getContext(),LinearLayoutManager.VERTICAL,getResources().getColor(R.color.color_list_bg), ScreenUtil.dp2px(getContext(),10)));
        myAutionAllRecyclerViewAdapter = new MyAutionAllRecyclerViewAdapter(autionList,getAuctionType());
        myAutionAllRecyclerViewAdapter.setOnClickItemListener((BaseRecyclerAdapter.OnAdapterListener<MyAuctionBean>) (view, itemBean, position) -> {
            Intent intent = new Intent(getContext(),HomeShopDetailActivity.class);
            intent.putExtra("gid",itemBean.getId());
            startActivity(intent);
        });
        HeaderAndFooterRecyclerViewAdapter headerAndFooterRecyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(myAutionAllRecyclerViewAdapter);
        autionList.setAdapter(headerAndFooterRecyclerViewAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    private void initData() {
        varyViewHelper.showLoadingView();
        Map<String,String> param = new HashMap<>();
        param.put("d_type",getAuctionType());
        param.put("p",String.valueOf(pageSize));
        OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_AUCTION_URL, param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(()->varyViewHelper.showErrorView());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.isSuccessful()){
                        String str = response.body().string();
                        //Log.i(TAG,str);
                        ResponseBean<ResponseResultBean<MyAuctionBean>> data =
                                getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<MyAuctionBean>>>(){}.getType());
                        if(data.getCode()==200&&data.getResult().getList()!=null){
                            updateList(data.getResult().getList());
                            if(data.getResult().getList().size()==0){
                                handler.post(()->varyViewHelper.showEmptyView());
                            }else{
                                handler.post(()->varyViewHelper.showDataView());
                            }
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.post(()->{
                        varyViewHelper.showErrorView();
                    });
                }
                handler.post(()->varyViewHelper.showErrorView());
            }
        });
    }

    @Override
    public void onStart() {
        Log.i(TAG,"onStart" + activity.getAuctionType());
        super.onStart();
        if(timer==null && (getAuctionType() == activity.auctionTypes[0] || getAuctionType() == activity.auctionTypes[2])){
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Map<String,String> param = new HashMap<>();
                    param.put("d_type",getAuctionType());
                    param.put("p",String.valueOf(pageSize));
                    OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_AUCTION_URL, param, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            handler.post(()->varyViewHelper.showErrorView());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if(response.isSuccessful()){
                                    String str = response.body().string();
                                    //Log.i(TAG,str);
                                    ResponseBean<ResponseResultBean<MyAuctionBean>> data =
                                            getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<MyAuctionBean>>>(){}.getType());
                                    if(data.getCode()==200&&data.getResult().getList()!=null){
                                        if(data.getResult().getList().size()>0){
                                            updateList(data.getResult().getList());
                                        }
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.post(()->{
                                    varyViewHelper.showErrorView();
                                });
                            }
                            handler.post(()->varyViewHelper.showErrorView());
                        }
                    });
                }
            },1000,1000);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(timer!=null){
            timer.cancel();
            timer = null;
        }
        Log.i(TAG,"onStop" + activity.getAuctionType());
    }

    private void updateList(List<MyAuctionBean> list) {
        handler.post(()->myAutionAllRecyclerViewAdapter.setListNotify(list));
    }

    private String getAuctionType(){
        return activity.getAuctionType();
    }
}
