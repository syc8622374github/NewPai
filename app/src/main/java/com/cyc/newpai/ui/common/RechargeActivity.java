package com.cyc.newpai.ui.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.ui.common.adapter.PayMethodRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.AmountBean;
import com.cyc.newpai.ui.common.entity.PayMethodBean;
import com.cyc.newpai.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class RechargeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAmount();
        initPayMethod();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.setTitle(getTitle().toString());
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(R.color.color_toolbar_title_black));
    }

    private void initPayMethod() {
        RecyclerView recyclerView = findViewById(R.id.rv_recharge_method);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PayMethodRecyclerViewAdapter payMethodRecyclerViewAdapter = new PayMethodRecyclerViewAdapter(recyclerView);
        List<PayMethodBean> payMethodBeans = new ArrayList<>();
        payMethodBeans.add(new PayMethodBean(R.drawable.ic_alipay,"支付宝支付",true));
        payMethodBeans.add(new PayMethodBean(R.drawable.ic_wechat,"微信支付",false));
        payMethodRecyclerViewAdapter.setListNotify(payMethodBeans);
        payMethodRecyclerViewAdapter.setOnClickItemListener((view, itemBean, position) -> {
            for(int i=0;i<payMethodBeans.size();i++){
                if(i==position){
                    payMethodBeans.get(i).setCheck(true);
                }else{
                    payMethodBeans.get(i).setCheck(false);
                }
            }
            payMethodRecyclerViewAdapter.notifyDataSetChanged();
        });
        recyclerView.setAdapter(payMethodRecyclerViewAdapter);
    }

    private void initAmount() {
        List<AmountBean> amountBeans = new ArrayList<>();
        amountBeans.add(new AmountBean("60","50",false));
        amountBeans.add(new AmountBean("120","100",false));
        amountBeans.add(new AmountBean("360","300",false));
        amountBeans.add(new AmountBean("600","500",false));
        amountBeans.add(new AmountBean("1200","1000",false));
        amountBeans.add(new AmountBean("2400","2000",false));
        MyGridView gridView = findViewById(R.id.gv_recharge_amount);
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this,amountBeans);
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            for(int i=0;i<amountBeans.size();i++){
                if(position==i){
                    amountBeans.get(i).setSelect(!amountBeans.get(i).isSelect());
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
