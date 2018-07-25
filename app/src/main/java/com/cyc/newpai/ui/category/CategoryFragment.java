package com.cyc.newpai.ui.category;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.category.adapter.CategoryDetailRecyclerAdapter;
import com.cyc.newpai.ui.category.adapter.CategoryTitleRecyclerAdapter;
import com.cyc.newpai.ui.category.entity.CategoryDetailBean;
import com.cyc.newpai.ui.category.entity.CategoryMenuBean;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CategoryFragment extends BaseFragment {

    private RecyclerView rvCategoryTitle;
    private RecyclerView rvCategoryDetail;
    private Map<Integer,List<CategoryDetailBean>> categoryDetailBeanMap = new HashMap<>();
    public static final String FLAG = CategoryFragment.class.getName();
    private CategoryTitleRecyclerAdapter categoryTitleRecyclerAdapter;
    private CategoryDetailRecyclerAdapter categoryDetailRecyclerAdapter;
    private Map<Integer,List<CategoryDetailBean>> cacheDetailData = new HashMap<>();
    private List<CategoryMenuBean> categoryMenuBeanList;
    private View view;

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category,container,false);
        //initVaryView();
        initCategoryTitle(view);
        initCategoryDetail(view);
        initData();
        return view;
    }

    @Override
    protected View getLoadingTargetView() {
        return view.findViewById(R.id.ll_category_content);
    }

    private void initData() {
        //varyViewHelper.showLoadingView();
        OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_CATE_LIST_URL, null, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                //handler.post(()->varyViewHelper.showErrorView());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    if(response.isSuccessful()){
                        String str = response.body().string();
                        ResponseBean<ResponseResultBean<CategoryMenuBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<CategoryMenuBean>>>(){}.getType());
                        if(data.getCode()==200&&data.getResult()!=null){
                            categoryMenuBeanList = data.getResult().getList();
                            initMenu(categoryMenuBeanList);
                            return;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                //handler.post(()->varyViewHelper.showErrorView());
            }
        });
    }

    private void initMenu(List<CategoryMenuBean> list) {
        list.get(0).setSelect(true);
        for(int i=0;i<list.size();i++){
            initShopDetail(list.get(i).getCate_id(),list.get(0).getCate_id());
        }
        handler.post(() ->{
            categoryTitleRecyclerAdapter.setListNotify(list);
            /*if(categoryMenuBeanList.size()>0){
                varyViewHelper.showDataView();
            }else{
                varyViewHelper.showEmptyView();
            }*/
        });
    }

    private void initShopDetail(int cate_id, int defaultId) {
        if(cacheDetailData.get(defaultId)==null){
            Map<String,String> params = new HashMap<>();
            params.put("cate_id",String.valueOf(cate_id));
            params.put("p","1");
            params.put("pagesize","1");
            OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_GOODS_BY_CATE_ID_URL, params, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        if(response.isSuccessful()){
                            String str = response.body().string();
                            ResponseBean<ResponseResultBean<CategoryDetailBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<CategoryDetailBean>>>(){}.getType());
                            if(data.getCode()==200&&data.getResult()!=null){
                                List<CategoryDetailBean> list =  data.getResult().getList();
                                cacheDetailData.put(cate_id,list);
                                if(cate_id==defaultId){
                                    initDetail(cacheDetailData.get(defaultId));
                                }
                                return;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });
        }
        if(cate_id == defaultId){
            initDetail(cacheDetailData.get(defaultId));
        }
    }

    private void initDetail(List<CategoryDetailBean> list) {
        if(list!=null){
            handler.post(() -> categoryDetailRecyclerAdapter.setListNotify(list));
        }
    }

    private void initCategoryDetail(View view) {
        rvCategoryDetail = view.findViewById(R.id.rv_category_right_category_detail);
        rvCategoryDetail.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategoryDetail.addItemDecoration(new CommItemDecoration(getContext(),1,getResources().getColor(R.color.divider),2));
        categoryDetailRecyclerAdapter = new CategoryDetailRecyclerAdapter(rvCategoryDetail);
        rvCategoryDetail.setAdapter(categoryDetailRecyclerAdapter);
    }

    private void initCategoryTitle(View view) {
        rvCategoryTitle = view.findViewById(R.id.rv_category_left_category_title);
        rvCategoryTitle.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategoryTitle.addItemDecoration(new CommItemDecoration(getContext(),1,getResources().getColor(R.color.divider),2));
        categoryTitleRecyclerAdapter = new CategoryTitleRecyclerAdapter(rvCategoryTitle);
        categoryTitleRecyclerAdapter.setOnClickItemListener((view1, itemBean, position) -> {
            for(int i=0;i<categoryMenuBeanList.size();i++){
                if(i==position){
                    categoryMenuBeanList.get(i).setSelect(true);
                }else{
                    categoryMenuBeanList.get(i).setSelect(false);
                }
            }
            categoryTitleRecyclerAdapter.notifyDataSetChanged();
            categoryDetailRecyclerAdapter.setListNotify(cacheDetailData.get(categoryMenuBeanList.get(position).getCate_id()));
        });
        rvCategoryTitle.setAdapter(categoryTitleRecyclerAdapter);
    }
}
