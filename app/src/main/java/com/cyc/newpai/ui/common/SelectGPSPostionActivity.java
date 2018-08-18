package com.cyc.newpai.ui.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cyc.newpai.R;
import com.cyc.newpai.framework.adapter.VH;
import com.cyc.newpai.ui.common.entity.LocationBean;
import com.cyc.newpai.util.Constant;
import com.cyc.newpai.util.LocationHelper;
import com.cyc.newpai.util.SharePreUtil;
import com.cyc.newpai.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// 选择GPS位置界面
public class SelectGPSPostionActivity extends BaseMapActivity implements AdapterView.OnItemClickListener{

    private HashMap<String,Boolean> location = new HashMap<>();
    private List<PoiInfo> nearPointer = new ArrayList<>();
    private LocationHelper locationHelper;
    private ListView nearLocListView;
    private MyArrayAdapter adapter;
    private MapStatus mMapStatus;
    private LatLng centerPoint;
    private TextView title;
    private View view;

    boolean firstLoaction = true;        // 第一次定位

    float xdirection = 100;

    private LocationBean curr_location = new LocationBean();

    private MyOrientationListener myOrientationListener = null;

    // 刷新列表事件
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if(adapter==null){
                        adapter = new MyArrayAdapter();
                        nearLocListView.setAdapter(adapter);
                        //nearLocListView.setEmptyView(findViewById(R.id.empty));
                    }else{
                        adapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        locationMapWiew();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        ctb_toolbar.tv_title.setTextColor(getResources().getColor(android.R.color.black));
        ctb_toolbar.setRightAction1("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLocation();
            }
        });
    }

    private void sendLocation() {
        Intent intent = new Intent();
        intent.putExtra(LocationHelper.LOCATION_BEAN,getGson().toJson(curr_location));
        setResult(2,intent);
        finish();
    }

    //初始化地图
    private void locationMapWiew() {
        //初始化定位信息
        locationHelper = new LocationHelper(this);

        // 初始位置采用上一次保存的位置
        LatLng newPosition = new LatLng(SharePreUtil.getPref(this,Constant.KEY_IM_LON,0f), SharePreUtil.getPref(this,Constant.KEY_IM_LAT,0f));
        if (newPosition.latitude > 0) {
            centerPoint = newPosition;
            locationHelper.moveToPosition(centerPoint, null, baiduMap);
        }

        locationHelper.setBdLocationListener(new BDLocationListener(){

            @Override
            public void onReceiveLocation(BDLocation location) {
                // 第一次定位时才自动切到屏幕中间并搜索附近信息
                if (firstLoaction) {
                    firstLoaction = false;
                    LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    centerPoint = newPosition;
                    addPinInMapView(centerPoint);
                    locationHelper.initMyLocData(location, baiduMap);
                    locationHelper.moveToPosition(centerPoint, null, baiduMap);
                    searchNear(newPosition);
                    saveLocation(newPosition);
                    return;
                }

                locationHelper.updateMyLocDataNormal(location, baiduMap, xdirection);
            }
        });
        //locationHelper.getLocationClientOption().setScanSpan(1000);     // 每隔1秒定位一次
        locationHelper.startLocation();
    }

    //在地图上添加定位大头针
    private void addPinInMapView(LatLng point) {
        mMapStatus = baiduMap.getMapStatus();
        ImageView imageView = new ImageView(SelectGPSPostionActivity.this);
        imageView.setVisibility(View.VISIBLE);
        imageView.setBackgroundResource(R.drawable.icon_position);
        MapViewLayoutParams.Builder params = new MapViewLayoutParams.Builder();
        params.point(mMapStatus.targetScreen);
        params.position(point);
        mMapView.addView(imageView, params.build());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        location.clear();
        location.put(nearPointer.get(i - 1).uid, true);
        selectAndMoveToLocation(nearPointer.get(i - 1).location, nearPointer.get(i - 1));
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        handler.sendEmptyMessage(0);
        curr_location.setLatitude(nearPointer.get(i-1).location.latitude);
        curr_location.setLongitude(nearPointer.get(i-1).location.longitude);
        curr_location.setkAddress(nearPointer.get(i-1).address);
        curr_location.setName(nearPointer.get(i-1).name);
        sendLocation();
    }

    //添加当前定位信息
    private void addLocationView(String address) {
        if(view==null){
            view = LayoutInflater.from(SelectGPSPostionActivity.this).inflate(android.R.layout.simple_list_item_1, null);
            title = VH.get(view,android.R.id.text1);
        }
        location.clear();
        title.setText(address);
        title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.location_green, 0);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location.clear();
                handler.sendEmptyMessage(0);
                title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.location_green, 0);
                selectAndMoveToLocation(centerPoint, null);
            }
        });
        nearLocListView.removeHeaderView(view);
        nearLocListView.addHeaderView(view);
    }

    /**
     * 更新POI
     * @param latLng
     * @param point
     */
    private void selectAndMoveToLocation(LatLng latLng, PoiInfo point) {
        locationHelper.addMarker(latLng, baiduMap, R.drawable.location);
        locationHelper.moveToPosition(latLng, point, baiduMap);
    }

    //查询周边地区位置信息
    private void searchNear(LatLng newPosition) {
        final GeoCoder mSearch = GeoCoder.newInstance();
        final ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        reverseGeoCodeOption.location(newPosition);
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult != null) {
                    nearPointer.clear();
                    List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
                    if (poiInfos != null) {
                        nearPointer.addAll(poiInfos);
                    }
                    if (reverseGeoCodeResult.getLocation() != null) {
                        curr_location.setLatitude(reverseGeoCodeResult.getLocation().latitude);
                        curr_location.setLongitude(reverseGeoCodeResult.getLocation().longitude);
                    }
                    curr_location.setSite("");      // 查询不到名称
                    if (reverseGeoCodeResult.getAddress() != null) {
                        curr_location.setkAddress(reverseGeoCodeResult.getAddress());
                        addLocationView(reverseGeoCodeResult.getAddress());
                    }
                    if (reverseGeoCodeResult.getAddressDetail()!=null){
                        curr_location.setAddressComponent(reverseGeoCodeResult.getAddressDetail());
                    }
                }
                handler.sendEmptyMessage(0);

                mSearch.destroy();
            }
        });
        mSearch.reverseGeoCode(reverseGeoCodeOption);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_gpspostion;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        nearLocListView = findViewById(R.id.rv_select_address_list);
        nearLocListView.setBackgroundColor(Color.WHITE);
        nearLocListView.setOnItemClickListener(this);
        mMapView = (MapView) findViewById(R.id.bmapView);
    }

    /**
     * 保存定位坐标
     * @param location
     */
    public void saveLocation(LatLng location){
        if (location == null){
            return;
        }
        SharePreUtil.setPref(this,Constant.KEY_IM_LAT,(float) location.latitude);
        SharePreUtil.setPref(this,Constant.KEY_IM_LON,(float) location.longitude);
    }

    /**
     * 列表适配器
     */
    class MyArrayAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return nearPointer.size();
        }

        @Override
        public Object getItem(int i) {
            return nearPointer.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view = LayoutInflater.from(SelectGPSPostionActivity.this).inflate(R.layout.item_select_gps_postion, null);
            }
            PoiInfo item = (PoiInfo) getItem(i);
            TextView title = VH.get(view,R.id.text1);
            TextView content = VH.get(view,R.id.text2);
            ImageView img = VH.get(view,R.id.location);
            title.setText(item.name);
            if(TextUtils.isEmpty(item.address)){
                content.setVisibility(View.GONE);
            }else{
                content.setVisibility(View.VISIBLE);
                content.setText(nearPointer.get(i).address);
            }
           /* if(location.get(nearPointer.get(i).uid)== Boolean.TRUE){
                img.setBackgroundResource(R.mipmap.location_green);
            }else{
                img.setBackgroundResource(0);
            }*/
            return view;
        }
    }

    @Override
    protected void onDestroy() {
//        myOrientationListener.stop();
        locationHelper.stopLocation();
        super.onDestroy();
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            mMapStatus = baiduMap.getMapStatus();
            centerPoint = mMapStatus.target;
            searchNear(centerPoint);
        }
    }

    /**
     * 初始化方向传感器
     */
    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(getApplicationContext());
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                xdirection = x;
            }
        });
        myOrientationListener.start();
    }

    /**
     * 方向感应器监听器
     */
    public static class MyOrientationListener implements SensorEventListener {

        private Context context;
        private SensorManager sensorManager;
        private Sensor sensor;

        private float lastX ;

        private OnOrientationListener onOrientationListener ;

        public MyOrientationListener(Context context)
        {
            this.context = context;
        }

        // 开始
        public void start() {
            // 获得传感器管理器
            sensorManager = (SensorManager) context
                    .getSystemService(Context.SENSOR_SERVICE);
            if (sensorManager != null) {
                // 获得方向传感器
                sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            }
            // 注册
            if (sensor != null) {//SensorManager.SENSOR_DELAY_UI
                sensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_UI);
            }

        }

        // 停止检测
        public void stop() {
            sensorManager.unregisterListener(this);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 接受方向感应器的类型
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                // 这里我们可以得到数据，然后根据需要来处理
                float x = event.values[SensorManager.DATA_X];

                if( Math.abs(x- lastX) > 1.0 ) {
                    onOrientationListener.onOrientationChanged(x);
                }
//            Log.e("DATA_X", x+"");
                lastX = x ;

            }
        }

        public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
            this.onOrientationListener = onOrientationListener ;
        }

        public interface OnOrientationListener {
            void onOrientationChanged(float x);
        }
    }
}
