package com.cyc.newpai.ui.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.BaseRecyclerAdapter;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.framework.decoration.SpaceItemDecoration;
import com.cyc.newpai.framework.flowlayout.FlowLayoutManager;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.category.CommItemDecoration;
import com.cyc.newpai.ui.common.adapter.SearchKeywordRecyclerViewAdapter;
import com.cyc.newpai.ui.common.adapter.SearchRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.SearchBean;
import com.cyc.newpai.ui.common.entity.SearchKeywdBean;
import com.cyc.newpai.util.ScreenUtil;
import com.cyc.newpai.widget.SearchToolbar;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {

    private SearchKeywordRecyclerViewAdapter searchKeyWordRecyclerViewAdapter;
    private SearchRecyclerViewAdapter searchRecyclerViewAdapter;
    private RecyclerView rvSearchList;
    private LinearLayout llHotSearchKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initVaryView();
        //varyViewHelper.showLoadingView();
        varyViewHelper.hideView(true);
        initData();
    }

    @Override
    protected View getLoadingTargetView() {
        return rvSearchList;
    }

    private void initData() {
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_BID_SEARCH_HISTORY_URL, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<SearchKeywdBean>> data = getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<SearchKeywdBean>>>() {
                        }.getType());
                        if (data.getCode() == 200 && data.getResult().getList() != null) {
                            updateHisKeyword(data.getResult());
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateHisKeyword(ResponseResultBean<SearchKeywdBean> result) {
        handler.post(()->searchKeyWordRecyclerViewAdapter.setListNotify(result.getList()));
    }

    private void initView() {
        RecyclerView rvKeywordList = findViewById(R.id.rv_search_keyword_list);
        rvKeywordList.setLayoutManager(new FlowLayoutManager());
        rvKeywordList.addItemDecoration(new SpaceItemDecoration(ScreenUtil.dp2px(this, ScreenUtil.dp2px(this,4))));
        searchKeyWordRecyclerViewAdapter = new SearchKeywordRecyclerViewAdapter(rvKeywordList);
        searchKeyWordRecyclerViewAdapter.setOnClickItemListener(new BaseRecyclerAdapter.OnAdapterListener<SearchKeywdBean>() {
            @Override
            public void onItemClickListener(View view, SearchKeywdBean itemBean, int position) {
                varyViewHelper.hideView(false);
                llHotSearchKey.setVisibility(View.GONE);
                varyViewHelper.showLoadingView();
                //varyViewHelper.showEmptyView();
                Map<String, String> params = new HashMap<>();
                params.put("keyword",itemBean.getTag());
                OkHttpManager.getInstance(SearchActivity.this).postAsyncHttp(HttpUrl.HTTP_BID_SEARCH_URL, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if (response.isSuccessful()) {
                                String str = response.body().string();
                                ResponseBean<ResponseResultBean<SearchBean>> data = getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<SearchBean>>>() {
                                }.getType());
                                if (data.getCode() == 200) {
                                    if (data.getResult().getList() != null && data.getResult().getList().size() > 0) {
                                        updateSearchData(data.getResult().getList());
                                    }else{
                                        handler.post(()->varyViewHelper.showEmptyView());
                                    }
                                    return;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        rvKeywordList.setAdapter(searchKeyWordRecyclerViewAdapter);
        llHotSearchKey = findViewById(R.id.ll_search_hot_search_key);
        /*List<SearchKeywdBean> data = new ArrayList<>();
        data.add(new SearchKeywdBean("iphonex"));
        data.add(new SearchKeywdBean("xiaomi"));
        data.add(new SearchKeywdBean("huawei"));
        data.add(new SearchKeywdBean("oneplus3T"));
        data.add(new SearchKeywdBean("oneplus3T-23"));
        data.add(new SearchKeywdBean("vivo-23124"));
        data.add(new SearchKeywdBean("huawei-0"));
        data.add(new SearchKeywdBean("xiaomi"));
        data.add(new SearchKeywdBean("vivo-dd"));
        data.add(new SearchKeywdBean("iphonex-eqh"));
        searchKeyWordRecyclerViewAdapter.setListNotify(data);*/
        SearchToolbar searchToolbar = findViewById(R.id.st_search_bar);
        searchToolbar.setSearchListener(key -> {
            varyViewHelper.hideView(false);
            llHotSearchKey.setVisibility(View.GONE);
            varyViewHelper.showLoadingView();
            //varyViewHelper.showEmptyView();
            Map<String, String> params = new HashMap<>();
            params.put("keyword", key);
            /*List<SearchBean> datas = new ArrayList<>();
            datas.add(new SearchBean("11","111","111","111",10));
            datas.add(new SearchBean("11","111","111","111",10));
            datas.add(new SearchBean("11","111","111","111",10));
            datas.add(new SearchBean("11","111","111","111",10));
            datas.add(new SearchBean("11","111","111","111",10));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    searchRecyclerViewAdapter.setListNotify(datas);
                    varyViewHelper.showDataView();
                }
            },2000);*/
            OkHttpManager.getInstance(SearchActivity.this).postAsyncHttp(HttpUrl.HTTP_BID_SEARCH_URL, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            ResponseBean<ResponseResultBean<SearchBean>> data = getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<SearchBean>>>() {
                            }.getType());
                            if (data.getCode() == 200) {
                                if (data.getResult().getList() != null && data.getResult().getList().size() > 0) {
                                    updateSearchData(data.getResult().getList());
                                }else{
                                    handler.post(()->varyViewHelper.showEmptyView());
                                }
                                return;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        rvSearchList = findViewById(R.id.rv_search_list);
        rvSearchList.setLayoutManager(new LinearLayoutManager(this));
        rvSearchList.addItemDecoration(new CommItemDecoration(this,LinearLayoutManager.VERTICAL,getResources().getColor(R.color.divider),1));
        searchRecyclerViewAdapter = new SearchRecyclerViewAdapter(rvSearchList);
        rvSearchList.setAdapter(searchRecyclerViewAdapter);
    }

    private void updateSearchData(List<SearchBean> list) {
        handler.post(()->{
            searchRecyclerViewAdapter.setListNotify(list);
            varyViewHelper.showDataView();
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }
}
