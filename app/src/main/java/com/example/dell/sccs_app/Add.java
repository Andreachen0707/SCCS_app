package com.example.dell.sccs_app;

import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.java_websocket.client.WebSocketClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Add extends AppCompatActivity {

    private LocationManager locationManager;
    //private WebSocketConnection mAdd = new WebSocketConnection();
    private WebSocketClient mNew;
    //private Draft selectDraft;

    public String sendMessage;

    private static final String url = "ws://121.40.34.92:7070/api/ws";
    public static final String logURL = "http://121.40.34.92:7070/api/json?cmd=login&ctrl=user&version=1&lang=zh_CN";
    public static final String user = "snow";
    public static final String password = "snow.chen";
    //private String ws = "ws://121.40.34.92:7070/api/ws";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbarAdd);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //String testlog = logon(logURL,user,password);
       // Log.i("UID=",testlog);

      // connect();

        Button add = (Button) findViewById(R.id.scan);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add.this, Scan.class);
                startActivity(intent);
            }
        });

        Button cancel = (Button) findViewById(R.id.negativeButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button ok = (Button) findViewById(R.id.positiveButton);
        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                       //String id = LoginProcess.loginByPost(user,password);
                       //Log.i("UID",id);
                       //Toast.makeText(Add.this,id,Toast.LENGTH_SHORT).show();

            }
        });

    }

}


