package com.cyc.newpai.ui.me;

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
import com.cyc.newpai.ui.me.adapter.AllRecordRecyclerViewAdapter;
import com.cyc.newpai.ui.me.entity.AllRecordBean;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MyPropertyAllRecordFragment extends BaseFragment {

    private View view;
    private RecyclerView list;
    private AllRecordRecyclerViewAdapter allRecordRecyclerViewAdapter;
    public static final String TYPE_ALL_RECORD = "type_all_record";
    public static final String TYPE_PAY_RECORD = "type_pay_record";
    public static final String TYPE_INCOME_RECORD = "type_income_record";
    private String type;
    private List<AllRecordBean> allRecordBeans = new ArrayList<>();
    private List<AllRecordBean> payRecordBeans = new ArrayList<>();
    private List<AllRecordBean> inComingRecordBeans = new ArrayList<>();

    public static MyPropertyAllRecordFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type",type);
        MyPropertyAllRecordFragment fragment = new MyPropertyAllRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_item_list,container,false);
        initView();
        return view;
    }

    private void initView() {
        list = view.findViewById(R.id.list);
        allRecordRecyclerViewAdapter = new AllRecordRecyclerViewAdapter(list);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(allRecordRecyclerViewAdapter);
        initData();
    }

    private void initData() {
        Map<String,String> params = new HashMap<>();
        params.put("type","0");
        OkHttpManager.getInstance(getContext())
                .postAsyncHttp(HttpUrl.HTTP_PROPERTY_URL, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            if(response.isSuccessful()){
                                String str = response.body().string();
                                ResponseBean<ResponseResultBean<AllRecordBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<ResponseResultBean<AllRecordBean>>>(){}.getType());
                                if(data.getCode()==200){
                                    initList(data.getResult().getList());
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initList(List<AllRecordBean> list) {
        allRecordBeans.clear();
        inComingRecordBeans.clear();
        payRecordBeans.clear();
        allRecordBeans.addAll(list);
        for(AllRecordBean allRecordBean : list){
            if(Integer.valueOf(allRecordBean.getMoney())>0){
                inComingRecordBeans.add(allRecordBean);
            }else if(Integer.valueOf(allRecordBean.getMoney())<=0){
                payRecordBeans.add(allRecordBean);
            }
        }
        handler.post(()->{
            switch (type){
                case TYPE_ALL_RECORD:
                    allRecordRecyclerViewAdapter.setListNotify(allRecordBeans);
                    break;
                case TYPE_PAY_RECORD:
                    allRecordRecyclerViewAdapter.setListNotify(payRecordBeans);
                    break;
                case TYPE_INCOME_RECORD:
                    allRecordRecyclerViewAdapter.setListNotify(inComingRecordBeans);
                    break;
            }
        });
    }
}
