package com.cyc.newpai.ui.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;
import com.cyc.newpai.ui.common.adapter.AmountRecyclerViewAdapter;
import com.cyc.newpai.ui.common.entity.AmountBean;
import com.cyc.newpai.ui.main.adapter.GridDivider;
import com.cyc.newpai.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

public class RechargeActivity extends BaseActivity {

    private RecyclerView amountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAmount();
    }

    private void initAmount() {
        //amountList = findViewById(R.id.rv_recharge_amount);
        //amountList.setNestedScrollingEnabled(false);
        //amountList.setLayoutManager(new LinearLayoutManager(this));
        //amountList.addItemDecoration(new GridDivider(this,3,0));
        //AmountRecyclerViewAdapter amountRecyclerViewAdapter = new AmountRecyclerViewAdapter(amountList);
        List<AmountBean> amountBeans = new ArrayList<>();
        amountBeans.add(new AmountBean("60","50",false));
        amountBeans.add(new AmountBean("60","50",false));
        amountBeans.add(new AmountBean("60","50",false));
        amountBeans.add(new AmountBean("60","50",false));
        amountBeans.add(new AmountBean("60","50",false));
        amountBeans.add(new AmountBean("60","50",false));
        //amountRecyclerViewAdapter.setListNotify(amountBeans);
        //amountList.setAdapter(amountRecyclerViewAdapter);

        MyGridView gridView = findViewById(R.id.gv_recharge_amount);
        gridView.setAdapter(new GridViewAdapter(this,amountBeans));
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
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.recharge_amount_item,parent,false);
            }
            return convertView;
        }
    }
}
