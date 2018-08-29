package com.cyc.newpai.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyc.newpai.MainActivity;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.Util;
import com.cyc.newpai.framework.adapter.base.WrapContentGridLayoutManager;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.ui.common.BaseWebViewActivity;
import com.cyc.newpai.ui.common.adapter.PaySucessAdapter;
import com.cyc.newpai.ui.common.entity.RechargeDetailBean;
import com.cyc.newpai.ui.main.adapter.GridDivider;
import com.cyc.newpai.ui.main.entity.HomePageBean;
import com.cyc.newpai.util.GsonManager;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PaySuccessActivity extends BaseActivity {

    private static final String TAG = PaySuccessActivity.class.getSimpleName();
    private RecyclerView list;
    private View headView;
    private int pageSize;
    private Timer timer;
    private String selectType = "1";
    private PaySucessAdapter paySucessAdapter;
    private String type;
    private RechargeDetailBean requestData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startRefreshData();
    }

    private void startRefreshData() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateIndexData(selectType, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                if (response.isSuccessful()) {
                                    String str = response.body().string();
                                    ResponseBean<HomePageBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<HomePageBean>>() {
                                    }.getType());
                                    if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                                        updateShopData(responseBean.getResult());
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    private void updateShopData(HomePageBean bean) {
        if (bean.getList() != null) {
            pageSize = bean.getList().size();
            handler.post(() -> paySucessAdapter.setData(bean.getList()));
        }
    }

    private void updateIndexData(String type, Callback callback) {
        Map<String, String> param = new HashMap<>();
        param.put("type", type);
        param.put("pagesize", String.valueOf(pageSize));
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_INDEX_URL, param, callback);
    }

    private void initData() {
        updateIndexData(selectType, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        String str = response.body().string();
                        ResponseBean<HomePageBean> responseBean = GsonManager.getInstance().getGson().fromJson(str, new TypeToken<ResponseBean<HomePageBean>>() {
                        }.getType());
                        if (responseBean.getCode() == 200 && responseBean.getResult() != null) {
                            updateShopData(responseBean.getResult());
                            return;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    private void initView() {
        type = getIntent().getStringExtra(BaseWebViewActivity.REQUEST_STATUS);
        requestData = (RechargeDetailBean) getIntent().getSerializableExtra(BaseWebViewActivity.STATUS_RECHARGE_DATA);
        initHeadView();
        list = findViewById(R.id.rv_pay_success_list);
        list.setLayoutManager(new WrapContentGridLayoutManager(this, 2));
        list.addItemDecoration(new GridDivider(this, 2, getResources().getColor(R.color.divider)));
        paySucessAdapter = new PaySucessAdapter(this,null,true);
        paySucessAdapter.addHeaderView(headView);
        list.setAdapter(paySucessAdapter);
    }

    private void initHeadView() {
        headView = Util.inflate(this, R.layout.activity_pay_success_head01);
        TextView price = headView.findViewById(R.id.tv_pay_success_price);
        if(!TextUtils.isEmpty(type)&&type.equals(BaseWebViewActivity.STATUS_RECHARGE)&&requestData!=null)
        price.setText("￥"+requestData.getRealprice()+"");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_success;
    }

    public void clickEvent(View view) {
        switch (view.getId()){
            case R.id.tv_pay_success_finish:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
        }
    }
}
