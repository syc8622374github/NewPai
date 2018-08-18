package com.cyc.newpai.ui.common;

import android.os.Bundle;
import android.view.MotionEvent;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.base.BaseActivity;

public abstract class BaseMapActivity extends BaseActivity implements BaiduMap.OnMapTouchListener{

	protected MapView mMapView = null;
	protected BaiduMap baiduMap;
	@Override
	protected void onStart() {
		super.onStart();

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
        if(savedInstanceState!=null) {
            finish();
            return;
        }*/
		mMapView = (MapView) findViewById(R.id.bmapView);
		baiduMap = mMapView.getMap();
		baiduMap.setOnMapTouchListener(this);
		baiduMap.setMyLocationEnabled(true);
		// 删除缩放控件
		mMapView.removeViewAt(2);
		// 删除百度地图logo
		mMapView.removeViewAt(1);
	}

	@Override
	protected void onDestroy() {
		baiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onTouch(MotionEvent motionEvent) {

	}
}
