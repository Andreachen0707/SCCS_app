package com.example.dell.sccs_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;

public class Scan extends AppCompatActivity {

    private LocationManager locationManager;
    private EditText NAME;
    private EditText UID;
    private EditText GPS_1;
    private EditText GPS_2;

    private static final String GPS_LOCATION_NAME = android.location.LocationManager.GPS_PROVIDER;
    private final static String[] MULTI_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private boolean isGpsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        NAME = (EditText)findViewById(R.id.Text);
        UID = (EditText)findViewById(R.id.Text2);
        GPS_1 = (EditText)findViewById(R.id.Text3);
        GPS_2 = (EditText)findViewById(R.id.Text4);


        Button scan_button = (Button) findViewById(R.id.scan);
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentscan = new Intent(Scan.this, CaptureActivity.class);
                startActivityForResult(intentscan,0x123);
            }
        });

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        //Location location = locationManager.getLastKnownLocation(bestProvider);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateView(location);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, new LocationListener() {

            @Override
            public void onProviderEnabled(String provider) {
                updateView(locationManager.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {
                updateView(null);
            }

            @Override
            public void onLocationChanged(Location location) {
                updateView(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
        });


    }

    private void updateView(Location location) {
        if (location != null) {
            StringBuffer sb = new StringBuffer();
            sb.append("实时的位置信息：\n经度：");
            sb.append(location.getLongitude());
            sb.append("\n纬度：");
            sb.append(location.getLatitude());
            sb.append("\n高度：");
            sb.append(location.getAltitude());
            sb.append("\n速度：");
            sb.append(location.getSpeed());
            sb.append("\n方向：");
            sb.append(location.getBearing());
            sb.append("\n精度：");
            sb.append(location.getAccuracy());

            StringBuffer test_1 = new StringBuffer();
            test_1.append(location.getLongitude());
            GPS_1.setText(test_1.toString());
            StringBuffer test_2 = new StringBuffer();
            test_2.append(location.getLatitude());
            GPS_2.setText(test_2.toString());
        } else {
            // 如果传入的Location对象为空则清空EditText
            GPS_1.setText("no");
            GPS_2.setText("no");
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode == 0x345)&&(requestCode==0x123)) {
            TextView scanResult = (TextView) findViewById(R.id.scan_result);
            if (null != data) {
                Bundle bundle = data.getExtras();
                if(bundle == null)
                    return;
                else {
                    String result = bundle.getString("scan_result");
                    scanResult.setText(result);

                   // String [] temp = null;
                  //  temp = result.split("@");

                   // NAME.setText(temp[0]);
                   // UID.setText(temp[1]);

                    WebView browser = (WebView) findViewById(R.id.Towebtest);
                    browser.loadUrl(result);

                    browser.getSettings().setSupportZoom(true);
                    browser.getSettings().setBuiltInZoomControls(true);

                    browser.setWebViewClient(new WebViewClient() {
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }
            }
        }
        else
            Log.i("result","fail");


    }




}

