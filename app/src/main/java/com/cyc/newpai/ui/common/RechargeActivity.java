package com.cyc.newpai.ui.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.ui.common.adapter.PayMethodRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.AmountBean;
import com.cyc.newpai.ui.common.entity.PayMethodBean;
import com.cyc.newpai.ui.common.entity.RechargeDetailBean;
import com.cyc.newpai.ui.common.entity.RechargeResultBean;
import com.cyc.newpai.ui.me.PaySuccessActivity;
import com.cyc.newpai.util.PayHelper;
import com.cyc.newpai.util.StringUtil;
import com.cyc.newpai.widget.MyGridView;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RechargeActivity extends BaseActivity implements View.OnClickListener{

    private Button payOk;
    private RecyclerView rvRechargeMethodList;
    private PayMethodRecyclerViewAdapter payMethodRecyclerViewAdapter;
    private int PayMethod = 1;
    private GridViewAdapter gridViewAdapter;
    private List<AmountBean> amountBeans = new ArrayList<>();
    private EditText rechargeRmb;
    private TextView rechargePaiBi;
    private int selectPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initAmount();
        initPayMethod();
        initEvent();
        initData();
    }

    private void initData() {
        OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_RECHARGE_AMOUNT_URL, null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.isSuccessful()){
                        String str = response.body().string();
                        ResponseBean<Map<String,List<AmountBean>>> data = getGson().fromJson(str,new TypeToken<ResponseBean<Map<String,List<AmountBean>>>>(){}.getType());
                        if(data.getCode()==200){
                            amountBeans.clear();
                            selectPosition = 0;
                            amountBeans.addAll(data.getResult().get("list"));
                            amountBeans.get(0).setSelect(true);
                            rechargeRmb.setText(amountBeans.get(0).getMoney());
                            rechargePaiBi.setText("预计获得"+(Integer.valueOf(amountBeans.get(0).getMoney())+Integer.valueOf(amountBeans.get(0).getZeng_money()))+"拍币");
                            handler.post(()->gridViewAdapter.notifyDataSetChanged());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initEvent() {
        payOk.setOnClickListener(this);
    }

    private void initView() {
        payOk = findViewById(R.id.btn_recharge_pay_ok);
        rechargeRmb = findViewById(R.id.et_recharge_rmb);
        rechargePaiBi = findViewById(R.id.tv_recharge_get_pai_bi);
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(R.color.color_toolbar_title_black));
    }

    private void initPayMethod() {
        rvRechargeMethodList = findViewById(R.id.rv_recharge_method);
        rvRechargeMethodList.setLayoutManager(new LinearLayoutManager(this));
        payMethodRecyclerViewAdapter = new PayMethodRecyclerViewAdapter(rvRechargeMethodList);
        List<PayMethodBean> payMethodBeans = new ArrayList<>();
        payMethodBeans.add(new PayMethodBean(R.drawable.ic_alipay,"支付宝支付",true));
        payMethodBeans.add(new PayMethodBean(R.drawable.ic_wechat,"微信支付",false));
        payMethodRecyclerViewAdapter.setListNotify(payMethodBeans);
        payMethodRecyclerViewAdapter.setOnClickItemListener((view, itemBean, position) -> {
            for(int i=0;i<payMethodBeans.size();i++){
                if(i==position){
                    payMethodBeans.get(i).setCheck(true);
                    if(payMethodBeans.get(i).getPayMethod().equals("支付宝支付")){
                        PayMethod = 1;
                    }else{
                        PayMethod = 2;
                    }
                }else{
                    payMethodBeans.get(i).setCheck(false);
                }
            }
            payMethodRecyclerViewAdapter.notifyDataSetChanged();
        });
        rvRechargeMethodList.setAdapter(payMethodRecyclerViewAdapter);
    }

    private void initAmount() {
        MyGridView gridView = findViewById(R.id.gv_recharge_amount);
        gridViewAdapter = new GridViewAdapter(this, amountBeans);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            selectPosition = position;
            for(int i = 0; i< amountBeans.size(); i++){
                if(position==i){
                    amountBeans.get(i).setSelect(true);
                    rechargeRmb.setText(amountBeans.get(i).getMoney());
                    rechargePaiBi.setText("预计获得"+(Integer.valueOf(amountBeans.get(i).getMoney())+Integer.valueOf(amountBeans.get(i).getZeng_money()))+"拍币");
                }else{
                    amountBeans.get(i).setSelect(false);
                }
            }
            gridViewAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_recharge_pay_ok:
                /*Map<String,String> params = new HashMap<>();
                params.put("uid","6b8b17d0e90c3cc3dc3d05d1");
                params.put("price","0.01");
                params.put("istype","1");
                params.put("notify_url","http://app.zhideting.cn/Crontab/paysPayReturnBack.html");
                params.put("return_url","http://app.zhideting.cn/Crontab/paysPayReturnBack.html");
                params.put("orderid",System.currentTimeMillis()+"");
                params.put("orderuid","1111");
                params.put("goodsname","111");
                List<String> keys = new ArrayList<>();
                keys.add("uid");
                keys.add("price");
                keys.add("istype");
                keys.add("notify_url");
                keys.add("return_url");
                keys.add("orderid");
                keys.add("orderuid");
                keys.add("goodsname");
                PayHelper.pay(this, HttpUrl.HTTP_PAY_REQUEST_URL, keys, params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("pay",e.getMessage());

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            String str = response.body().string();
                            Log.e("pay",str);
                        }
                    }
                });*/
                if(selectPosition==-1){
                    ToastManager.showToast(this,"请选择充值金额", Toast.LENGTH_SHORT);
                    return;
                }
                Map<String,String> params = new HashMap<>();
                params.put("price",amountBeans.get(selectPosition).getMoney());
                params.put("istype", String.valueOf(PayMethod));
                OkHttpManager.getInstance(this).postAsyncHttp(HttpUrl.HTTP_RECHATGE_URL,params, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            String str = response.body().string();
                            ResponseBean<RechargeResultBean<RechargeDetailBean>> data = getGson().fromJson(str,new TypeToken<ResponseBean<RechargeResultBean<RechargeDetailBean>>>(){}.getType());
                            if(data.getCode()==200){
                                Intent intent = new Intent(RechargeActivity.this,BaseWebViewActivity.class);
                                intent.putExtra(BaseWebViewActivity.REQUEST_STATUS,BaseWebViewActivity.STATUS_RECHARGE);
                                intent.putExtra(BaseWebViewActivity.STATUS_RECHARGE_DATA,data.getResult().getData());
                                intent.putExtra(BaseWebViewActivity.REQUEST_URL,data.getResult().getData().getQrcode());
                                startActivity(intent);
                            }
                        }
                    }
                });
                break;
        }
    }

    public class GridViewAdapter extends BaseAdapter{
        private Context context;
        private List<AmountBean> data;

        public GridViewAdapter(Context context,List<AmountBean> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RechargeHolderView holderView;
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.recharge_amount_item,parent,false);
                holderView = new RechargeHolderView(convertView);
                convertView.setTag(holderView);
            }else{
                holderView = (RechargeHolderView) convertView.getTag();
            }
            if(data.get(position)!=null&&data.get(position).isSelect()){
                convertView.setBackgroundResource(R.drawable.shape_border_red);
                holderView.tvPaiPi.setTextColor(Color.WHITE);
                holderView.tvRmb.setTextColor(Color.WHITE);
            }else{
                convertView.setBackgroundResource(R.drawable.shape_border_red_line);
                holderView.tvPaiPi.setTextColor(getResources().getColor(R.color.colorPrimary));
                holderView.tvRmb.setTextColor(getResources().getColor(R.color.color_recharge_rmb));
            }
            holderView.tvRmb.setText("售价"+((AmountBean)getItem(position)).getMoney()+"元");
            holderView.tvPaiPi.setText(Integer.valueOf(((AmountBean)getItem(position)).getMoney())+Integer.valueOf(((AmountBean)getItem(position)).getZeng_money())+"拍币");
            return convertView;
        }

        public class RechargeHolderView{

            public final TextView tvPaiPi;
            public final TextView tvRmb;
            public final View view;

            public RechargeHolderView(View view) {
                this.view = view;
                tvPaiPi = view.findViewById(R.id.tv_recharge_amount_pai_bi);
                tvRmb = view.findViewById(R.id.tv_recharge_amount_rmb);
            }
        }
    }
}
