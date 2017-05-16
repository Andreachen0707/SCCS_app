package com.example.dell.sccs_app.FragmentDesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.dell.sccs_app.Add_lamp;
import com.example.dell.sccs_app.Project;
import com.example.dell.sccs_app.R;

import java.util.List;

import static com.example.dell.sccs_app.StaticValue.addaction;

/**
 * Created by dell on 2017/5/4.
 */

public class MapFragment extends Fragment {
    private MapView mMapView = null;
    private BaiduMap mBaidumap;

    private com.getbase.floatingactionbutton.FloatingActionButton mAddaction;
    private com.getbase.floatingactionbutton.FloatingActionButton mGetaction;

    private double longitude;
    private double latitude;
    private LocationClient mLocationClient;
    private LocationManager locationManager;
    private String provider;
    private boolean ifFrist = true;

    private View mView;
    private boolean modeFlag = true;
    private float zoomLevel;
    private MyLocationListener mLocationListener;
    private boolean isFirstLocation = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());


        mView = inflater.inflate(R.layout.map_fragment, container, false);

        mAddaction = (com.getbase.floatingactionbutton.FloatingActionButton) mView.findViewById(R.id.addItemLamp);
        mGetaction = (com.getbase.floatingactionbutton.FloatingActionButton) mView.findViewById(R.id.addItemControl);

        initMap();
        initLocation();
        currentLocation();
        Handler info = new Handler();


        //初始化以后要获得一系列的参数 去获取集中器和灯的地理位置 然后标记在地图上


        //添加新集中器时标记的方式
        latitude = 30.00;
        longitude = 120.00;
        mapAnnotation();

        mAddaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addaction = 0;
                Add_show();
            }
        });

        mGetaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                // Fragment_Project.notify();
            }
        });
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    private void initMap() {
        //获取地图控件引用
        mMapView = (MapView) mView.findViewById(R.id.baidu_map);
        // 不显示缩放比例尺
        mMapView.showZoomControls(false);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        //百度地图
        mBaidumap = mMapView.getMap();
        mBaidumap.setMyLocationEnabled(true);
        // 改变地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaidumap.setMapStatus(mMapStatusUpdate);
        //设置地图状态改变监听器
        mBaidumap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
                //当地图状态改变的时候，获取放大级别
                zoomLevel = arg0.zoom;
            }
        });
        mBaidumap.setOnMarkerClickListener(markerClick);
    }

    private void initLocation() {
        //定位客户端的设置
        mLocationClient = new LocationClient(getActivity());
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");//坐标类型
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//打开Gps
        option.setScanSpan(1000);//1000毫秒定位一次
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    private void currentLocation(){
        //String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        //Location location = locationManager.getLastKnownLocation(bestProvider);
        if(ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
            List<String> list = locationManager.getProviders(true);
            if (list.contains(LocationManager.GPS_PROVIDER)) {
                //是否为GPS位置控制器
                provider = LocationManager.GPS_PROVIDER;
            } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
                //是否为网络位置控制器
                provider = LocationManager.NETWORK_PROVIDER;

            } else {
                Toast.makeText(getActivity(), "请检查网络或GPS是否打开",
                        Toast.LENGTH_LONG).show();
                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);
            //updateView(location);
            if (location != null) {
                //获取当前位置，这里只用到了经纬度
                //longitude = location.getLatitude();
                //latitude = location.getLongitude();
                navigateTo(location);
            }

            locationManager.requestLocationUpdates(provider, 2000, 1, new LocationListener() {

                @Override
                public void onProviderEnabled(String provider) {
                    //updateView(locationManager.getLastKnownLocation(provider));
                }

                @Override
                public void onProviderDisabled(String provider) {
                }

                @Override
                public void onLocationChanged(Location location) {
                    //updateView(location);
                    navigateTo(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }
            });
        }
    }

    private void navigateTo(Location location) {
        // 按照经纬度确定地图位置
        if (ifFrist) {
            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            // 移动到某经纬度
            mBaidumap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomBy(5f);
            // 放大
            mBaidumap.animateMapStatus(update);

            ifFrist = false;
        }
        // 显示个人位置图标
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        MyLocationData data = builder.build();
        mBaidumap.setMyLocationData(data);
    }


    private Criteria getCriteria(){
        Criteria criteria=new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        return criteria;
    }

    public static double convertToDouble(String number, double defaultValue) {
        if (TextUtils.isEmpty(number)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(number);
        } catch (Exception e) {
            return defaultValue;
        }

    }

    private void Add_show(){
        Intent intent = new Intent();
        intent.setClass(getActivity(),Add_lamp.class);
        startActivity(intent);
    }


    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaidumap.setMyLocationData(data);
            if (isFirstLocation) {
                //获取经纬度
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                //Toast.makeText(getActivity(),latitude+" "+latitude,Toast.LENGTH_SHORT).show();
                Log.i("location", latitude+" "+longitude);
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);

                mBaidumap.animateMapStatus(status);//动画的方式到中间
                isFirstLocation = false;
                //showInfo("位置：" + location.getAddrStr());
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }

    public void mapAnnotation()
    {
        Marker marker = null;
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_icon_controler_gray);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        Bundle bundle = new Bundle();
        bundle.putSerializable("info", "test");
        //在地图上添加Marker，并显示
        marker = (Marker) mBaidumap.addOverlay(option);
        marker.setExtraInfo(bundle);
    }

    BaiduMap.OnMarkerClickListener markerClick = new BaiduMap.OnMarkerClickListener() {
        /**
         * 地图 Marker 覆盖物点击事件监听函数
         * @param marker 被点击的 marker
         */
        public boolean onMarkerClick(Marker marker){
            InfoWindow mInfoWindow;

            Bundle res = marker.getExtraInfo();
            String a = res.getString("info");
            TextView location = new TextView(getActivity().getApplicationContext());
            location.setPadding(30, 20, 30, 50);
            location.setText(a);
            final LatLng ll = marker.getPosition();
            android.graphics.Point p = mBaidumap.getProjection().toScreenLocation(ll);
            p.y -= 50;
            LatLng llInfo = mBaidumap.getProjection().fromScreenLocation(p);
            //为弹出的InfoWindow添加点击事件
            mInfoWindow = new InfoWindow(location, llInfo, 0);
            //显示InfoWindow
            mBaidumap.showInfoWindow(mInfoWindow);
            return false;
        }
    };


    @Override
    public void onDestroy() {

        super.onDestroy();

        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理

        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理

        mMapView.onPause();
    }
}



