package com.cyc.newpai.ui.me;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyc.newpai.GlideApp;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseFragment;
import com.cyc.newpai.http.HttpUrl;
import com.cyc.newpai.http.OkHttpManager;
import com.cyc.newpai.http.entity.ResponseBean;
import com.cyc.newpai.http.entity.ResponseResultBean;
import com.cyc.newpai.ui.common.RechargeActivity;
import com.cyc.newpai.ui.me.entity.UserInfoBean;
import com.cyc.newpai.ui.user.LoginActivity;
import com.cyc.newpai.util.GlideCircleTransform;
import com.cyc.newpai.util.LoginUtil;
import com.cyc.newpai.widget.ToastManager;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = MeFragment.class.getSimpleName();
    private View view;
    private ImageView avator;
    private TextView mobile;
    private TextView paiBi;
    private TextView zengBi;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        initData();
        Log.i(TAG, "onCreateView");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    public void initData() {
        if (LoginUtil.isLogin(getActivity())) {
            OkHttpManager.getInstance(getContext()).postAsyncHttp(HttpUrl.HTTP_USER_INFO_URL, null, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            ResponseBean<ResponseResultBean<UserInfoBean>> data = getGson().fromJson(str, new TypeToken<ResponseBean<ResponseResultBean<UserInfoBean>>>() {
                            }.getType());
                            if (data.getCode() == 200 && data.getResult().getItem() != null) {
                                updateData(data.getResult().getItem());
                            } else if (data.getCode() == 1000) {
                                review();
                            }
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            review();
        }
    }

    public void review() {
        handler.post(() -> {
            avator.setImageResource(R.drawable.ic_avator_default);
            mobile.setText("");
            paiBi.setText("--");
            zengBi.setText("--");
        });
    }

    private void updateData(UserInfoBean item) {
        handler.post(() -> {
            GlideApp.with(getContext())
                    .load(item.getImg())
                    .placeholder(R.drawable.ic_avator_default)
                    .transform(new GlideCircleTransform(getContext()))
                    .into(avator);
            mobile.setText(item.getNickname());
            paiBi.setText(item.getMoney().substring(0, item.getMoney().indexOf(".")));
            zengBi.setText(item.getMoney_zeng().substring(0, item.getMoney_zeng().indexOf(".")));
        });
    }

    private void initView() {
        LinearLayout llMyProperty = view.findViewById(R.id.ll_me_my_property);
        llMyProperty.setOnClickListener(this);
        Button btnRecharge = view.findViewById(R.id.btn_me_recharge);
        btnRecharge.setOnClickListener(this);
        view.findViewById(R.id.tv_me_avator).setOnClickListener(this);
        view.findViewById(R.id.ll_me_suggestion).setOnClickListener(this);
        view.findViewById(R.id.ll_me_address).setOnClickListener(this);
        view.findViewById(R.id.ll_me_order_detail).setOnClickListener(this);
        view.findViewById(R.id.ll_me_auction).setOnClickListener(this);
        view.findViewById(R.id.ll_me_my_lucky_time).setOnClickListener(this);
        avator = view.findViewById(R.id.tv_me_avator);
        mobile = view.findViewById(R.id.tv_me_mobile);
        paiBi = view.findViewById(R.id.tv_me_pai_bi);
        zengBi = view.findViewById(R.id.tv_me_zeng_bi);
    }

    public static String getFlag() {
        return MeFragment.class.getName();
    }

    @Override
    public void onClick(View v) {
        if (LoginUtil.isLogin(getActivity())) {
            switch (v.getId()) {
                case R.id.ll_me_my_property:
                    startActivity(new Intent(getContext(), MyPropertyActivity.class));
                    break;
                case R.id.btn_me_recharge:
                    startActivity(new Intent(getContext(), RechargeActivity.class));
                    break;
                case R.id.tv_me_avator:
                    startActivityForResult(new Intent(getContext(), LoginActivity.class), 1);
                    break;
                case R.id.ll_me_suggestion:
                    startActivity(new Intent(getContext(), SuggestionActivity.class));
                    break;
                case R.id.ll_me_address:
                    Intent intent = new Intent(getContext(), SelectAddressActivity.class);
                    intent.putExtra(SelectAddressActivity.TYPE_ADDRESS, SelectAddressActivity.TYPE_EDIT_ADDRESS);
                    startActivity(intent);
                    break;
                case R.id.ll_me_order_detail:
                    startActivity(new Intent(getContext(), OrderDetailActivity.class));
                    break;
                case R.id.ll_me_auction:
                    startActivity(new Intent(getContext(), MyAuctionActivity.class));
                    break;
                case R.id.ll_me_my_lucky_time:
                    startActivity(new Intent(getContext(), MyLuckTimeActivity.class));
                    break;
                default:
                    ToastManager.showToast(getActivity(), "该功能暂未开放", Toast.LENGTH_SHORT);
                    break;
            }
        }else{
            startActivityForResult(new Intent(getContext(), LoginActivity.class), 1);
            ToastManager.showToast(getActivity(),"未检测账号登录",Toast.LENGTH_SHORT);
        }
    }
}
