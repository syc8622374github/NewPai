package com.cyc.newpai.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;

public class LocationHelper extends BDAbstractLocationListener {

	BDLocation location;//定位位置

	PoiInfo poiInfo;//移动后位置

	BDLocationListener bdLocationListener;

	// LocationClient类必须在主线程中声明
	LocationClient locationClient;

//	static LocationHelper instance;

	Context context;

	LocationClientOption defaultLocationClientOption;

	public static final String LOCATION_BEAN = "locationBean";

	public static final int BAIDU_READ_PHONE_STATE = 1001;

	public LocationHelper(Context context) {
		this.context = context;
		if(locationClient==null){
			locationClient = new LocationClient(context);
			locationClient.registerLocationListener(this);
            defaultLocationClientOption = new LocationClientOption();
            defaultLocationClientOption.setIsNeedAddress(true);
			//可选，是否需要地址信息，默认为不需要，即参数为false
			//如果开发者需要获得当前点的地址信息，此处必须为true
            defaultLocationClientOption.setIsNeedLocationDescribe(true);
			//可选，是否需要位置描述信息，默认为不需要，即参数为false
			//如果开发者需要获得当前点的位置信息，此处必须为true

            defaultLocationClientOption = new LocationClientOption();
            defaultLocationClientOption.setOpenGps(true);
            defaultLocationClientOption.setAddrType("all");
            defaultLocationClientOption.setCoorType("bd09ll");
            defaultLocationClientOption.disableCache(false);// 禁止启用缓存定位
			//locationClient.setLocOption(defaultLocationClientOption);
			//mLocationClient为第二步初始化过的LocationClient对象
			//需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
			//更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
		}
	}

	public BDLocation getLocation() {
		return location;
	}

	public PoiInfo getPoiInfo() {
		return poiInfo;
	}

	public LocationClientOption getLocationClientOption(){
		return defaultLocationClientOption;
	}

	public void setBdLocationListener(BDLocationListener bdLocationListener) {
		this.bdLocationListener = bdLocationListener;
	}

	/**
	 * 开始定位
	 */
	public void startLocation() {
		startLocation(defaultLocationClientOption);
	}

	/**
	 * 开始定位
	 */
	public void startLocation(LocationClientOption option) {
		if (option != null) {
			locationClient.setLocOption(option);
			locationClient.start();
		}
		if (locationClient != null && locationClient.isStarted())
			locationClient.requestLocation();
		else
			Log.d("LocationUtil", "locationClient is null or not started");
	}

	/**
	 * 停止定位
	 */
	public void stopLocation() {
		if (locationClient != null)
			locationClient.stop();
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location != null) {
			if (bdLocationListener != null) {
				bdLocationListener.onReceiveLocation(location);
			}
			this.location = location;
		}
	}

	//添加marker标记
	public void addMarker(LatLng latlng, BaiduMap baiduMap,int rsid) {
		baiduMap.clear();
		System.out.println("add marker==>" + latlng.toString());
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(rsid);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latlng).icon(bitmap).draggable(false);
		// 在地图上添加Marker，并显示
		baiduMap.addOverlay(option);
	}

	public void addMarkerWithAnimation(LatLng latlng, BaiduMap baiduMap,int rsid) {
		baiduMap.clear();
		System.out.println("add marker==>" + latlng.toString());
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(rsid);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(latlng).icon(bitmap).draggable(false).animateType(MarkerOptions.MarkerAnimateType.drop);
		// 在地图上添加Marker，并显示
		baiduMap.addOverlay(option);
	}

	//移动到坐标位置
	public void moveToPosition(LatLng latLng,PoiInfo newPosition, BaiduMap baiduMap) {
		if(newPosition!=null){
			latLng = newPosition.location;
		}
		MapStatus mapStatus = new MapStatus.Builder().target(latLng).zoom(18).build();
		baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus));
		this.poiInfo = newPosition;
	}

	//绘制当前位置
	public void initMyLocData(BDLocation location,BaiduMap baiduMap) {
		if (location == null || baiduMap == null) {
			return;
		}
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(100).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, false, null));
		baiduMap.setMyLocationData(locData);
	}

	// 刷新当前位置
	public void updateMyLocDataNormal(BDLocation location, BaiduMap baiduMap, float xdirection){
		if (location == null || baiduMap == null) {
			return;
		}
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(location.getRadius())
				// 此处设置开发者获取到的方向信息，顺时针0-360
				.direction(xdirection).latitude(location.getLatitude())
				.longitude(location.getLongitude()).build();
		baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, false, null));
		baiduMap.setMyLocationData(locData);
	}
}
