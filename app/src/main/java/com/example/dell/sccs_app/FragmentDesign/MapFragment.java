package com.example.dell.sccs_app.FragmentDesign;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.dell.sccs_app.Add_lamp;
import com.example.dell.sccs_app.LoginProcess;
import com.example.dell.sccs_app.Project;
import com.example.dell.sccs_app.R;
import com.example.dell.sccs_app.Util.DensityUtil;
import com.example.dell.sccs_app.Util.GPS;
import com.example.dell.sccs_app.Util.GPS_convert;
import com.example.dell.sccs_app.Widgets.FloatingActionButton;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.common.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static android.drm.DrmStore.DrmObjectType.CONTENT;
import static com.example.dell.sccs_app.LoginProcess.getProject;
import static com.example.dell.sccs_app.StaticValue.LampListData;
import static com.example.dell.sccs_app.StaticValue.LcuData;
import static com.example.dell.sccs_app.StaticValue.Lcu_lampData;
import static com.example.dell.sccs_app.StaticValue.StationData;
import static com.example.dell.sccs_app.StaticValue.addaction;
import static com.example.dell.sccs_app.Util.GPS_convert.bd09_To_Gps84;

/**
 * Created by dell on 2017/5/4.
 */

public class MapFragment extends Fragment {
    private MapView mMapView = null;
    private BaiduMap mBaidumap;

    private com.getbase.floatingactionbutton.FloatingActionButton mAddLamp;
    private com.getbase.floatingactionbutton.FloatingActionButton mAddControllor;
    private com.getbase.floatingactionbutton.FloatingActionButton refresh;
    private com.getbase.floatingactionbutton.FloatingActionButton mLampoff;
    private com.getbase.floatingactionbutton.FloatingActionButton mLampadjust;
    private com.getbase.floatingactionbutton.FloatingActionButton mdelete;
    private com.getbase.floatingactionbutton.FloatingActionsMenu mAddlist;

    private com.getbase.floatingactionbutton.FloatingActionButton scan;
    private com.getbase.floatingactionbutton.FloatingActionButton input;

    private double mlongitude;
    private double mlatitude;
    private double gpslongitude;
    private double gpslatitude;
    private LocationClient mLocationClient;
    private LocationManager locationManager;
    private String provider;
    private boolean ifFrist = true;

    private View mView;
    private boolean modeFlag = true;
    private float zoomLevel;
    private MyLocationListener mLocationListener;
    private boolean isFirstLocation = true;
    private BitmapDescriptor bitmap;
    private List<Marker> markerlist;
    private List<Marker> lampmarkerlist;
    private int markerindex;
    private int lampindex;

    private Marker point;//点击后出现的小点点
    private BitmapDescriptor bitmap_onclick;
    private boolean controllclick;
    private boolean lampclick;


    private EditText NAME;
    private EditText UID;
    private EditText GPS_1;
    private EditText GPS_2;
    private Button ok;
    private Button cancel;

    private MyUpdate upload = new MyUpdate();
    private MyUpdate queryopen = new MyUpdate();
    private MyUpdate mrefresh = new MyUpdate();
    private MyUpdate mLamp = new MyUpdate();

    private String name;
    private String cuid;
    private String ssid;
    private String luid;
    private String lid;//删除的时候要用的一个参数
    private String lmodel; //灯具型号 多少瓦的那个
    private String lmodelid;
    private String lcumodel; //单灯控制器型号，可以控制多少灯那个
    private String lcumodelid;
    private String cuid_now;//当前被点击的控制器的cuid
    private String ssid_now;//当前被点击的控制器的ssid
    private String luid_now;//当前被点击的灯的id
    private int numberoflamp;//默认灯具名字
    private int numberofcontrol;//默认集中器名字


    private Project.WebClient testClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        mView = inflater.inflate(R.layout.map_fragment, container, false);

        mAddlist = (com.getbase.floatingactionbutton.FloatingActionsMenu) mView.findViewById(R.id.addItem);
        mAddLamp = new com.getbase.floatingactionbutton.FloatingActionButton(getActivity());
        mAddControllor =  new com.getbase.floatingactionbutton.FloatingActionButton(getActivity());
        refresh = (com.getbase.floatingactionbutton.FloatingActionButton) mView.findViewById(R.id.refresh);

        mAddControllor.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        mAddControllor.setIcon(R.drawable.icons_controller_gray);
        mAddControllor.setColorNormalResId(R.color.white);
        mAddControllor.setColorPressedResId(R.color.instruction);
        mAddControllor.setTitle(getString(R.string.controller));

        mAddLamp.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        mAddLamp.setIcon(R.drawable.icons_pot);
        mAddLamp.setColorNormalResId(R.color.white);
        mAddLamp.setColorPressedResId(R.color.instruction);
        mAddLamp.setTitle(getString(R.string.lamp));

        mLampoff= new com.getbase.floatingactionbutton.FloatingActionButton(getActivity());
        mLampadjust= new com.getbase.floatingactionbutton.FloatingActionButton(getActivity());
        mdelete= new com.getbase.floatingactionbutton.FloatingActionButton(getActivity());


        mLampoff.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        mLampoff.setIcon(R.drawable.icon_lamp_off_large);
        mLampoff.setColorNormalResId(R.color.white);
        mLampoff.setColorPressedResId(R.color.instruction);
        mLampoff.setTitle("Turn off");

        mLampadjust.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        mLampadjust.setIcon(R.drawable.icons_lamp_adjust_large);
        mLampadjust.setColorNormalResId(R.color.white);
        mLampadjust.setColorPressedResId(R.color.instruction);
        mLampadjust.setTitle("Light adjust");

        mdelete.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        mdelete.setIcon(R.drawable.icon_delete);
        mdelete.setColorNormalResId(R.color.white);
        mdelete.setColorPressedResId(R.color.instruction);
        mdelete.setTitle("Delete");


        mAddlist.addButton(mdelete);
        mdelete.setVisibility(View.INVISIBLE);
        mdelete.setEnabled(false);
        mAddlist.addButton(mLampadjust);
        mLampadjust.setVisibility(View.INVISIBLE);
        mdelete.setEnabled(false);
        mAddlist.addButton(mLampoff);
        mLampoff.setVisibility(View.INVISIBLE);
        mdelete.setEnabled(false);

        mAddlist.addButton(mAddControllor);
        mAddlist.addButton(mAddLamp);



        initMap();
        initLocation();
        //currentLocation();
        //初始化以后要获得一系列的参数 去获取集中器和灯的地理位置 然后标记在地图上 check!
        //Handler info = new Handler();
        handler.postDelayed(new maphandler(),2000);

        //调用client发送是可以的
        testClient = ((Project)getActivity()).getmClient();
        //testClient.senddata("login", "heart-beat","1","zh_CN","" , "0" , "");


        //添加新集中器时标记的方式



        mAddLamp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addaction = 0;
                Add_scan_show();
            }
        });

        mAddControllor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addaction = 1;
                Add_scan_show();
                // Fragment_Project.notify();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LatLng initpoint = new LatLng(0, 0);
                point.setPosition(initpoint);
                for(int i=0;i<markerlist.size();i++){
                    markerlist.get(i).remove();
                }
                Log.i("length test",String.valueOf(markerlist.size()));
                mrefresh.setParam(5,null,null,null,null,null,null,null,0,0);
                new Thread(mrefresh).start();
                handler.postDelayed(new maphandler(),1000);
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
        markerindex = 0;
        lampindex = 0;
        numberoflamp = 1;
        numberofcontrol = 1;
        controllclick = false;
        lampclick = false;
        markerlist = new ArrayList<Marker>();
        lampmarkerlist = new ArrayList<Marker>();



        point = null;
        bitmap_onclick = BitmapDescriptorFactory
                .fromResource(R.drawable.icons_pin_controller_click);


        //获取地图控件引用
        mMapView = (MapView) mView.findViewById(R.id.baidu_map);
        // 不显示缩放比例尺
        mMapView.showZoomControls(false);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        //百度地图
        mBaidumap = mMapView.getMap();
        mBaidumap.setMyLocationEnabled(true);
        //初始化 加上point但是没位置
        final LatLng initpoint = new LatLng(0, 0);
        OverlayOptions option = new MarkerOptions()
                .position(initpoint)
                .icon(bitmap_onclick);
        point = (Marker) mBaidumap.addOverlay(option);
        // 改变地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaidumap.setMapStatus(mMapStatusUpdate);
        //设置地图状态改变监听器
        mBaidumap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
                point.setPosition(initpoint);
                if(controllclick){
                    mAddControllor.setIcon(R.drawable.icons_controller_gray);
                    mAddControllor.setTitle(getString(R.string.controller));

                    mdelete.setVisibility(View.INVISIBLE);
                    mdelete.setEnabled(false);

                    mLampadjust.setVisibility(View.INVISIBLE);
                    mdelete.setEnabled(false);

                    mLampoff.setVisibility(View.INVISIBLE);
                    mdelete.setEnabled(false);
                }

                if(lampclick){
                    mAddControllor.setIcon(R.drawable.icons_controller_gray);
                    mAddControllor.setTitle(getString(R.string.controller));

                    mAddLamp.setIcon(R.drawable.icons_pot);
                    mAddLamp.setTitle(getString(R.string.lamp));

                    mdelete.setVisibility(View.INVISIBLE);
                    mdelete.setEnabled(false);

                    mLampadjust.setVisibility(View.INVISIBLE);
                    mdelete.setEnabled(false);

                    mLampoff.setVisibility(View.INVISIBLE);
                    mdelete.setEnabled(false);

                }
                /*mAddControllor.setIcon(R.drawable.icons_controller_gray);
                mAddControllor.setTitle(getString(R.string.controller));
                mAddLamp.setIcon(R.drawable.icons_pot);
                */
                //应该加一个初始化按钮的函数，恢复到初始状态
                mAddLamp.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        addaction = 0;
                        Add_scan_show();
                    }
                });

                mAddControllor.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        addaction = 1;
                        Add_scan_show();
                        // Fragment_Project.notify();
                    }
                });
                //mBaidumap.clear();
                /*for(int i = 0;i<markerlist.size();i++){
                    bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.icons_pin_controller);
                    markerlist.get(i).setIcon(bitmap);
                }*/
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
                controllclick = false;
                lampclick = false;
                if(zoomLevel<18){
                    //把灯的标记删除
                }
                Log.i("finish","in");
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
        mLocationClient.start();
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

    private void Add_scan_show(){
        /**
         *  Intent intent = new Intent();
         *  intent.setClass(getActivity(),Add_lamp.class);
         *  startActivity(intent);
         */
        final Dialog bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_add_lamp, null);
        bottomDialog.setContentView(contentView);
        bottomDialog.setCanceledOnTouchOutside(true);

        scan = (com.getbase.floatingactionbutton.FloatingActionButton)contentView.findViewById(R.id.scan);
        input = (com.getbase.floatingactionbutton.FloatingActionButton)contentView.findViewById(R.id.input);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getActivity(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getActivity(), 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

        scan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(getActivity(), CaptureActivity.class);
                startActivityForResult(intent,addaction);
            }
        });

        input.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Commit_show("");
            }
        });
    }

    private void Commit_show(String res){
        final Dialog bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        //补全字符
        String str ="000000000000";
        res=str.substring(0, 12-res.length())+res;

        //改变gps格式
        GPS gps = new GPS(mlatitude,mlongitude);
        GPS upgps = bd09_To_Gps84(gps.getWgLat(),gps.getWgLon());
        gpslatitude = upgps.getWgLat();
        gpslongitude = upgps.getWgLon();

        //静态定义
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_commit, null);
        bottomDialog.setContentView(contentView);
        NAME = (EditText) contentView.findViewById(R.id.Text);
        UID = (EditText) contentView.findViewById(R.id.Text2);
        GPS_1 = (EditText) contentView.findViewById(R.id.Text3);
        GPS_2 = (EditText) contentView.findViewById(R.id.Text4);
        ok = (Button) contentView.findViewById(R.id.okButton);
        cancel = (Button) contentView.findViewById(R.id.cancelButton);

        UID.setText(res);
        GPS_1.setText(String.valueOf(mlatitude));
        GPS_2.setText(String.valueOf(mlongitude));

        //灯具上传和控制器上传分别不同界面
        if(addaction==0) {
            NAME.setText("Lamp"+String.valueOf(numberoflamp));
            TextView id = (TextView) contentView.findViewById(R.id.uid);
            id.setText("Luid:");

            LinearLayout container = (LinearLayout) contentView.findViewById(R.id.val_list);
            //LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            LinearLayout name_container = (LinearLayout) contentView.findViewById(R.id.name_list);
            TextView lamp_model = new TextView(getActivity());
            TextView lcu_model = new TextView(getActivity());
            TextView cuid = new TextView(getActivity());
            LinearLayout.LayoutParams lampParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            LinearLayout.LayoutParams lampParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,1);
            lampParams.gravity = Gravity.END;

            lamp_model.setText("Model:");
            lamp_model.setGravity(Gravity.CENTER);
            lamp_model.setTextSize(16);
            lamp_model.setLayoutParams(lampParams);
            lcu_model.setText("Lcu:");
            lcu_model.setGravity(Gravity.CENTER);
            lcu_model.setTextSize(16);
            lcu_model.setLayoutParams(lampParams);
            cuid.setText("cuid:");
            cuid.setGravity(Gravity.CENTER);
            cuid.setTextSize(16);
            cuid.setLayoutParams(lampParams);
            name_container.addView(cuid);
            name_container.addView(lamp_model);
            name_container.addView(lcu_model);

            EditText cuidvalue = new EditText(getActivity());
            lampParams2.gravity = Gravity.CENTER;
            cuidvalue.setEms(10);
            cuidvalue.setLayoutParams(lampParams2);
            cuidvalue.setText(cuid_now);
            if (controllclick) {
                cuidvalue.setEnabled(false);
            }
            else
                cuidvalue.setEnabled(true);
            container.addView(cuidvalue);


            //增加显示cuid，灯具型号（一个下拉菜单）（扫描获得）
            final Spinner list_lamp = new Spinner(getActivity());
            List<String> alllamp = new ArrayList<String>();
            for (int i = 0; i < LampListData.size(); i++) {
                alllamp.add(LampListData.get(i).getModel());
            }
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,alllamp);
            list_lamp.setAdapter(adapter1);
            LinearLayout.LayoutParams lampParams1 = new LinearLayout.LayoutParams(DensityUtil.dp2px(getActivity(),210),DensityUtil.dp2px(getActivity(),50),1);
            lampParams1.gravity = Gravity.CENTER;
            list_lamp.setLayoutParams(lampParams1);
            container.addView(list_lamp);
            list_lamp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //拿到被选择项的值,传给上传的指令
                    lmodel = (String) list_lamp.getSelectedItem();
                    lmodelid = LampListData.get(position).getModelId();
                    Log.i("lamp model",lmodelid+lmodel);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });

            //显示单灯控制器型号
            final Spinner list_lcu = new Spinner(getActivity());
            List<String> alllcu = new ArrayList<String>();
            for (int i = 0; i < LcuData.size(); i++) {
                alllcu.add(LcuData.get(i).getModel());
            }
            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,alllcu);
            list_lcu.setAdapter(adapter2);
            lampParams1.gravity = Gravity.CENTER;
            list_lcu.setLayoutParams(lampParams1);
            container.addView(list_lcu);
            list_lcu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //拿到被选择项的值,传给上传的指令
                    lcumodel = (String) list_lcu.getSelectedItem();
                    lcumodelid = LcuData.get(position).getModelId();
                    Log.i("lcu model",lcumodelid+lcumodel);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });


            ok.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //添加灯的指令
                    name = NAME.getText().toString();
                    luid = UID.getText().toString();

                    upload.setParam(8,name,cuid_now,ssid_now,luid,null,lmodelid,lcumodelid,gpslatitude,gpslongitude);
                    numberoflamp++;
                    new Thread(upload).start();
                    mapAnnotation(name,cuid_now,null,luid,null,0,convertToDouble(GPS_1.getText().toString(),0.00),convertToDouble(GPS_2.getText().toString(),0.00));
                    bottomDialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //cancel
                    bottomDialog.dismiss();
                }
            });
        }

        if(addaction==1) {
            NAME.setText("Controller"+String.valueOf(numberofcontrol));
            ok.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //上传到服务器同时更新
                    //添加集中器的语句
                    name = NAME.getText().toString();
                    cuid = UID.getText().toString();
                    upload.setParam(7,name,cuid,name,null,null,null,null,gpslatitude,gpslongitude);
                    numberofcontrol++;
                    new Thread(upload).start();
                    mrefresh.setParam(5,null,null,null,null,null,null,null,0,0);
                    new Thread(mrefresh).start();
                    handler.post(new maphandler());
                    //mapAnnotation(name,cuid,null,null,1,convertToDouble(GPS_1.getText().toString(),0.00),convertToDouble(GPS_2.getText().toString(),0.00));
                    bottomDialog.dismiss();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //cancel
                    bottomDialog.dismiss();
                }
            });
        }

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getActivity(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getActivity(), 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((resultCode == 0x123)) {
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null)
                        return;

                    else {
                        String res = bundle.getString("scan_result");
                        Log.i("result test",res);
                        switch (requestCode) {
                            case 0:
                                Log.i("scan_test","in");
                                Commit_show(res);
                                break;

                            case 1:
                               Commit_show(res);
                                break;
                            default:
                                break;

                    }
                }
            }

        }
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
                mlatitude = location.getLatitude();
                mlongitude = location.getLongitude();
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                //Toast.makeText(getActivity(),latitude+" "+latitude,Toast.LENGTH_SHORT).show();
                Log.i("location", mlatitude+" "+mlongitude);
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

    public void mapAnnotation(String name,String cuid,String ssid,String luid,String lid,int type,double lat,double lng)
    {
        Marker marker = null;
        //定义Maker坐标点
        LatLng point = new LatLng(lat, lng);
        //构建Marker图标
        if(type == 1) {
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icons_pin_controller_new);
        }
        if(type ==0){
            bitmap = BitmapDescriptorFactory
                    .fromResource(R.drawable.icons_pin_lamp_new);
        }
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        Bundle bundle = new Bundle();
        bundle.putSerializable("type",String.valueOf(type));
        bundle.putSerializable("info", name);
        bundle.putSerializable("cuid",cuid);
        bundle.putSerializable("luid",luid);
        bundle.putSerializable("ssid",ssid);
        bundle.putSerializable("lid",lid);
        //在地图上添加Marker，并显示
        marker = (Marker) mBaidumap.addOverlay(option);
        marker.setExtraInfo(bundle);
        if(type == 1) {
            markerlist.add(markerindex, marker);
            markerindex = markerindex+1;
        }
        else{
            lampmarkerlist.add(lampindex,marker);
            lampindex = lampindex+1;
        }

    }

    BaiduMap.OnMarkerClickListener markerClick = new BaiduMap.OnMarkerClickListener() {
        /**
         * 地图 Marker 覆盖物点击事件监听函数
         * @param marker 被点击的 marker
         */
        public boolean onMarkerClick(Marker marker){
            InfoWindow mInfoWindow;


            Bundle res = marker.getExtraInfo();
            String a = res.getString("cuid");
            final String type = res.getString("type");
            cuid_now = a;
            ssid_now = res.getString("ssid");
            luid_now = res.getString("luid");
            lid = res.getString("lid");
            Log.i("ssid now",ssid_now);

            //点击后动态改变浮动按钮的功能
            //如果点击的是集中器的按钮，改变按钮的布局
            if("1".equals(type)) {
                point.setPosition(marker.getPosition());
                point.setToTop();
                queryopen.setParam(12,null,null,ssid_now,null,null,null,null,0,0);
                new Thread(queryopen).start();

                //画灯
                if(zoomLevel>18){
                    mLamp.setParam(9,null,null,ssid_now,null,null,null,null,0,0);
                    new Thread(mLamp).start();
                }

                mAddControllor.setIcon(R.drawable.icon_lamp_on_large);
                mAddControllor.setTitle("Turn on");

                mLampoff.setVisibility(View.VISIBLE);
                mLampoff.setEnabled(true);
                mLampadjust.setVisibility(View.VISIBLE);
                mLampadjust.setEnabled(true);
                mdelete.setVisibility(View.VISIBLE);
                mdelete.setEnabled(true);

                controllclick = true;
                //mAddControllor.setIcon(R.drawable.icons_controller_white);
                mAddControllor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        testClient.senddata("ctrl-km", "turn-on", "1", "zh_CN", "" + cuid_now + "", "1", "{\"sid\":\"" + ssid_now + "\",\"kms\":null}");
                       // testClient.senddata("push-lcu","status","1","","","","{\"sid\":\""+ssid_now+"\",\"cuid\":\""+cuid_now+"\",\"ctype\":1,\"luid\":\""+luid+"\",\"alarms\":[],\"lights\":[{\"lid\":\""+lcumodelid+"\",\"ch\":1,\"rs\":2,\"ls\":2,\"dim\":0,\"acount\":0,\"alarms\":[],\"ltime\":1495450796317}]}");


                    }
                });

                mLampoff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testClient.senddata("ctrl-km","turn-off","1","zh_CN",""+cuid_now+"","1","{\"sid\":\"" + ssid_now + "\",\"kms\":null}");
                    }
                });

                mLampadjust.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳到改光度的界面
                    }
                });
                mdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert(type,null);
                    }
                });
            }

            else{
                //改变浮动按钮的内容
                controllclick  = false;
                mAddLamp.setIcon(R.drawable.icon_lamp_on_large);
                mAddLamp.setTitle("Turn on");
                mAddControllor.setIcon(R.drawable.icon_lamp_off_large);
                mAddControllor.setTitle("Turn off");
                mLampoff.setVisibility(View.VISIBLE);
                mLampoff.setEnabled(true);
                mLampoff.setIcon(R.drawable.icons_lamp_adjust_large);
                mLampoff.setTitle("Light adjust");
                mLampadjust.setVisibility(View.VISIBLE);
                mLampadjust.setEnabled(true);
                mLampadjust.setIcon(R.drawable.icon_delete);
                mLampadjust.setTitle("Delete");

                mdelete.setVisibility(View.INVISIBLE);
                mdelete.setEnabled(false);

                lampclick = true;

                mAddLamp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testClient.senddata("ctrl-lcu", "turn-on", "1", "zh_CN", "" + cuid_now + "", "1", "{\"sid\":\""+ssid_now+"\",\"cast\":3,\"gnum\":0,\"luids\":[\""+luid_now+"\"],\"chs\":[1],\"delayTime\":0}");
                    }
                });
                mAddControllor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        testClient.senddata("ctrl-lcu", "turn-off", "1", "zh_CN", "" + cuid_now + "", "1", "{\"sid\":\""+ssid_now+"\",\"cast\":3,\"gnum\":0,\"luids\":[\""+luid_now+"\"],\"chs\":[1],\"delayTime\":0}");
                    }
                });

                mLampoff.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //那个50是调光值
                        testClient.senddata("ctrl-lcu", "dimming", "1", "zh_CN", "" + cuid_now + "", "1", "{\"sid\":\""+ssid_now+"\",\"cast\":3,\"gnum\":0,\"luids\":[\""+luid_now+"\"],\"chs\":[1],\"dims\":[50],\"delayTime\":0}");
                    }
                });

                mLampadjust.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //那个50是调光值
                        alert(type,lid);
                    }
                });


            }


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
            //mBaidumap.showInfoWindow(mInfoWindow);
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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            String type = data.getString("type");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
            Log.i("fuck",type);
            if("0".equals(type))
                Log.i("Log out","true");

            if("10".equals(String.valueOf(type))) {
                Toast.makeText(getActivity(), "Delete Successful", Toast.LENGTH_LONG).show();
                point.setPosition(new LatLng(0,0));
                mrefresh.setParam(5,null,null,null,null,null,null,null,0,0);
                new Thread(mrefresh).start();
                handler.post(new maphandler());
            }


            if("13".equals(String.valueOf(type))){
                Toast.makeText(getActivity(), "Delete Successful", Toast.LENGTH_LONG).show();

            }

            if("9".equals(String.valueOf(type))){
                handler.post(new Lamphandler());
            }

            if("".equals(val))
                Log.i("no response","retry");

            else {
                if (val.charAt(0) == '1') {
                    if (val.charAt(3) != '[') {
                        StringBuffer sb = new StringBuffer();
                        String input = "[";
                        String output = "]";
                        val = val.substring(3);
                        sb.append(input).append(val).append(output);
                        val = sb.toString();
                        Log.i("typ2", val);
                    } else {
                        val = val.substring(3);
                        Log.i("typ1", val);
                    }
                    LoginProcess.jsonTranslate(val, Integer.parseInt(type));
                }
            }
        }
    };

    private class MyUpdate implements Runnable
    {
        private int type;
        private String name;
        private String cuid;
        private String ssid;
        private String luid;
        private String lid;
        private String lmodelid;
        private String lcumodelid;
        private double lat;
        private double lng;
        public void setParam(int type,String name,String cuid,String ssid,String luid,String lid,String lmodelid,String lcumodelid,double lat,double lng)
        {
            this.type = type;
            this.name = name;
            this.cuid = cuid;
            this.ssid = ssid;
            this.luid = luid;
            this.lid = lid;
            this.lmodelid = lmodelid;
            this.lcumodelid = lcumodelid;
            this.lat = lat;
            this.lng = lng;
        }
        public void run()
        {
            String res = getProject(type,name,cuid,ssid,luid,lid,lmodelid,lcumodelid,lat,lng);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", res);
            data.putString("type", String.valueOf(type));
            msg.setData(data);
            handler.sendMessage(msg);
        }
    }


    private class maphandler implements Runnable
    {
        public void run()
        {
            //初始化在地图上标记
            if(StationData.size()!=0) {
                for(int t = 0;t<StationData.size();t++) {
                    double slatitude = StationData.get(t).getLat();
                    double slongitude = StationData.get(t).getLng();

                    LatLng sourceLatLng = new LatLng(slatitude,slongitude);
                    CoordinateConverter converter  = new CoordinateConverter();
                    converter.from(CoordinateConverter.CoordType.GPS);
                    // sourceLatLng待转换坐标
                    converter.coord(sourceLatLng);
                    LatLng desLatLng = converter.convert();
                    slatitude = desLatLng.latitude;
                    slongitude = desLatLng.longitude;

                    mapAnnotation(StationData.get(t).getName(),StationData.get(t).getCuid(),StationData.get(t).getSid(),null,null,1,slatitude,slongitude);
                }
            }
            else
                Toast.makeText(getActivity(),"Pull to refresh",Toast.LENGTH_SHORT).show();
        }
    }

    private class Lamphandler implements Runnable
    {
        public void run()
        {
            //初始化在地图上标记

            for(int i = 0;i<Lcu_lampData.size();i++) {
                Log.i("draw lamp",String.valueOf(Lcu_lampData.size()));
                double slatitude = Lcu_lampData.get(i).getLat();
                double slongitude = Lcu_lampData.get(i).getLng();

                LatLng sourceLatLng = new LatLng(slatitude,slongitude);
                CoordinateConverter converter  = new CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                // sourceLatLng待转换坐标
                converter.coord(sourceLatLng);
                LatLng desLatLng = converter.convert();
                slatitude = desLatLng.latitude;
                slongitude = desLatLng.longitude;
                mapAnnotation(Lcu_lampData.get(i).getName(),Lcu_lampData.get(i).getCuid(),Lcu_lampData.get(i).getSid(),Lcu_lampData.get(i).getLuid(),Lcu_lampData.get(i).getLid(),0,slatitude,slongitude);
            }
        }
    }

    private void alert(String type,String lid){
        if("1".equals(type))
            //发送删除指令
            upload.setParam(10,null,null,ssid_now,null,null,null,null,0,0);
        else
            upload.setParam(13,null,null,null,null,lid,null,null,0,0);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete with caution");
        builder.setTitle("Warning");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new Thread(upload).start();
                /*mrefresh.setParam(5,null,null,null,null,null,null,0,0);
                new Thread(mrefresh).start();
                handler.post(new maphandler());*/
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create().show();
    }

}



