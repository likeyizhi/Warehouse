package com.bbld.warehouse.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.bbld.warehouse.R;
import com.bbld.warehouse.base.BaseActivity;

import butterknife.BindView;

/**
 * 获取坐标
 * Created by likey on 2017/12/4.
 */

public class SetXYActivity extends BaseActivity{
    @BindView(R.id.tvBack)
    TextView tvBack;
    @BindView(R.id.tvSure)
    TextView tvSure;
    @BindView(R.id.tvToSearch)
    TextView tvToSearch;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;
    private double mCurrentLat;
    private double mCurrentLon;
    private float mCurrentAccracy;
    private MyLocationData locData;
    boolean isFirstLoc = true; // 是否首次定位
    private int mCurrentDirection = 0;
    public MyLocationListenner myListener = new MyLocationListenner();
    private GeoCoder search=null;
    private double latitude=0.0;//y
    private double longitude=0.0;//x

    @Override
    protected void initViewsAndEvents() {
        initMap();
        setListeners();
    }

    private void setListeners() {
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                if (longitude==0.0 && latitude==0.0){
                    intent.putExtra("_X", mCurrentLon);
                    intent.putExtra("_Y", mCurrentLat);
                }else{
                    intent.putExtra("_X", longitude);
                    intent.putExtra("_Y", latitude);
                }
                setResult(2010,intent);
                finish();
            }
        });
        tvToSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readyGoForResult(SetXYSearchActivity.class,2011);
                overridePendingTransition(0, 0);
            }
        });
    }

    private void initMap() {
        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            /**屏幕中间的经纬度**/
            @Override
            public void onMapStatusChangeFinish(final MapStatus mapStatus) {
                latitude = mapStatus.target.latitude;//43---y
                longitude = mapStatus.target.longitude;//125---x
//                showToast(latitude+","+longitude);
                /*LatLng ptCenter = new LatLng(latitude, longitude);
                search.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));*/
            }
        });
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
//            showToast(mCurrentLat+","+mCurrentLon);
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2011 && resultCode!=0){
            Intent intent=new Intent();
            intent.putExtra("_X", Double.parseDouble(data.getStringExtra("_X")));
            intent.putExtra("_Y", Double.parseDouble(data.getStringExtra("_Y")));
            setResult(2010,intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    public int getContentView() {
        return R.layout.activity_set_x_y;
    }
}
