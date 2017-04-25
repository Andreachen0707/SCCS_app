package com.example.dell.sccs_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.example.dell.sccs_app.Bean.DeviceListBean;
import com.example.dell.sccs_app.Bean.ElectricListBean;
import com.example.dell.sccs_app.Bean.ProjectBean;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.framing.FramedataImpl1;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


import static com.example.dell.sccs_app.StaticValue.ElectricListData;
import static com.example.dell.sccs_app.StaticValue.addlamp;
import static com.example.dell.sccs_app.StaticValue.addlamp2;
import static com.example.dell.sccs_app.StaticValue.askConcentratorUrl;
import static com.example.dell.sccs_app.StaticValue.askElectricUrl;
import static com.example.dell.sccs_app.StaticValue.askProjectListUrl;
import static com.example.dell.sccs_app.StaticValue.askStationListUrl;
import static com.example.dell.sccs_app.StaticValue.deviceListData;
import static com.example.dell.sccs_app.StaticValue.projectData;
import static com.example.dell.sccs_app.StaticValue.projectTemp;
import static com.example.dell.sccs_app.StaticValue.sendUrl;
import static com.example.dell.sccs_app.StaticValue.sid;
import static com.example.dell.sccs_app.StaticValue.stationquery;
import static com.example.dell.sccs_app.StaticValue.stationquery_all;


public class Project extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback {

    private GoogleMap mMap;

    private String cookieID = null;
    private Map<String,List<String>> head;
    private Map<String,List<String>> headfinal;
    private Map<String,String> head1;

    private int scanType;
    private String pid;
    private String cuid;
    private double lat;
    private double lng;
    private int ctime;
    private int ctype;
    private String name;

    private int connectType = -1;
    private boolean ready = false;
    private List<String> project_name = new ArrayList<String>();


    private HeadTask headTask = null;





    //private Client mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        new Thread(networkTask).start();

        connectType = 1;
        MyThread2 m1 = new MyThread2();
        m1.setName(connectType);
        new Thread(m1).start();





        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button scan = (Button)findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Project.this,Add.class);
                startActivity(intent);
            }
        });

        Button getlist = (Button)findViewById(R.id.getlist) ;
        getlist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                connectType = 2;
                MyThread2 m2 = new MyThread2();
                m2.setName(connectType);
                new Thread(m2).start();
            }
        });

        Button getelec = (Button)findViewById(R.id.getelec) ;
        getelec.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                connectType = 3;
                MyThread2 m3 = new MyThread2();
                m3.setName(connectType);
                new Thread(m3).start();
            }
        });

        Button send = (Button)findViewById(R.id.send) ;
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                connectType = 4;
                lat = 30.265571244862127;
                lng = 120.18019689712332;
                name ="AAAAA";
                cuid = "000FONDA_673";
                MyThread2 m4 = new MyThread2();
                m4.setName(connectType);
                new Thread(m4).start();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton addaction = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.addItemLamp);
        addaction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Project.this,Add.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.project, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {
            Intent intent= new Intent();
            intent.setClass(Project.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //@Override
    /*public void beforeRequest(Map<String, List<String>> headers) {
        super.beforeRequest(headers);
        List<String> cookieList =
                headers.get("Cookie");
        if (null == cookieList) {
            cookieList = new ArrayList<>();
        }
        cookieList.add("AXWEBSID=" + sid); // set your cookie value here
        headers.put("Cookie", cookieList);
    }*/








    public class HeadTask extends AsyncTask<String, Void, Map>{
        private final String muser;
        private final String mpassword;
        private final Handler mHandler;

        HeadTask(String user,String password,Handler handler){
            muser = user;
            mpassword = password;
            mHandler = handler;
        }
        @Override
        protected Map doInBackground(String ... params){
            //String user = params[0];
            //String password = params[1];
            Map headin = null;
            headin = LoginProcess.header("snow","snow.chen");

           return  headin;
        }
        @Override
        protected void onPostExecute(final Map result) {
            super.onPostExecute(result);
            Map resultout = null;
            resultout = WebFunction.get_head(result,cookieID);
            Message msg = mHandler.obtainMessage();
            if(result!=null){
                msg.what = 1;
                msg.obj = resultout;
            }else{
                msg.what = 2;
            }
            mHandler.sendMessage(msg);

            //Log.i("test head",result.toString());
        }
    }

    public static String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                // 如果值是list类型则调用自己
                if (list.get(i) instanceof List) {
                    sb.append(ListToString((List<?>) list.get(i)));
                    sb.append("|");
                } else {
                    sb.append(list.get(i));
                    sb.append("|");
                }
            }
        }
        return "L" + sb.toString();
    }

    private String answer(){
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        try{
            String url = "http://121.40.34.92:7070/api/json?cmd=lamp&ctrl=latlng&version=1&lang=zh_CN";
            URL urlnew = new URL(url);

            urlConnection = (HttpURLConnection) urlnew.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Cookie",cookieID);
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)");
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入


            OutputStream os = urlConnection.getOutputStream();
            DataOutputStream wr = new DataOutputStream(os);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("cmd", "lamp");
            jsonParam.put("ctrl","latlng");
            jsonParam.put("version","1");
            jsonParam.put("lang","zh_CN");
            wr.writeBytes(jsonParam.toString());
            wr.close();
            int HttpResult = urlConnection.getResponseCode();

            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(), "utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sb != null) {
            // 返回结果
            String response = sb.toString();
            Log.i("response",response);
            return response;
        } else {
            Log.i("response","null");
            return null;
        }
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
           // Log.i("fuck",type);
            if(val.charAt(0)=='1')
            {
                if(val.charAt(3)!='['){
                    StringBuffer sb = new StringBuffer();
                    String input= "[";
                    String output = "]";
                    val = val.substring(3);
                    sb.append(input).append(val).append(output);

                    val = sb.toString();
                    Log.i("typ2",val);
                }
                else{
                    val = val.substring(3);
                    Log.i("typ1",val);
                }
                jsonTranslate(val,Integer.parseInt(type));
            }
        }
    };

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            int type = connectType;
            String res = getProjectName(type);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", res);
            data.putString("type", String.valueOf(type));
            msg.setData(data);
            handler.sendMessage(msg);
            if(connectType!=1)
                connectType++;
        }
    };
    public class MyThread2 implements Runnable
    {
        private int type;

        public void setName(int name)
        {
            this.type = name;
        }
        public void run()
        {

            String res = getProjectName(type);
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("value", res);
            data.putString("type", String.valueOf(type));
            msg.setData(data);
            handler.sendMessage(msg);
        }
    }

    @SuppressWarnings({ "deprecation", "resource" })
    public String getProjectName(int con_type) {
        String body = "";
        String projectId = "";
        String link = "";
        projectTemp=0;
        switch (con_type) {
            case 1://获取项目
                link = askProjectListUrl;
                body = "{\"wheres\":[{\"k\":\"enabled\",\"o\":\"=\",\"v\":true}],\"orders\":[{\"k\":\"name\",\"v\":\"ASC\"}]}";
                break;
            case 2://集中器型号列表接口
                link = askConcentratorUrl;
                projectId = projectData.get(projectTemp).getId();
                body = "{\"page\":1,\"pageSize\":50,\"wheres\":[{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[{\"k\":\"model\",\"v\":\"ASC\"}]}";
                //body = "{\" wheres\":[{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[]}";
                break;
            case 3://电表型号接口
                link = askElectricUrl;
                projectId = projectData.get(projectTemp).getId();
                //String electid = ElectricListData.get(1).getModelId();
                //Log.i("elecid",electid);
                //body = "{\"page\":1,\"pageSize\":50,\"wheres\":[{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[{\"k\":\"model\",\"v\":\"ASC\"}]}";
                body = "{\"wheres\":[{\"k\":\"asDefault\",\"o\":\"=\",\"v\":true},{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[]}";
                break;
            case 4:
                link = sendUrl;
                projectId = projectData.get(projectTemp).getId();
                body = "{\"pid\":\""+projectId+"\",\"name\":\""+name+"\",\"cuid\":\""+cuid+"\",\"ctype\":1,\"cmodel\":\""+deviceListData.get(1).getModelId()+"\",\"devices\":[{\"deviceType\":2,\"modelId\":\""+ElectricListData.get(0).getModelId()+"\",\"name\":\"Built-in METER\"}],\"lat\":"+lat+",\"lng\":"+lng+"}";
                Log.i("body" ,body);
                break;
            case 5:
                link = stationquery_all;
                projectId = projectData.get(0).getId();
                body =  " {\"pid\":\""+projectId+"\"}";
                break;
            case 6:
                link = stationquery;
                body = "{\"wheres\":[{\"k\":\"stationId\",\"o\":\"=\",\"v\":\"a5cff7fddf19414289ea8d6959e1021b\"}],\"orders\":[]}";
                break;
            default:
                break;

        }
        //登录服务器
        HttpClient httpclient = new DefaultHttpClient();
        // 创建cookie store的本地实例
        CookieStore cookieStore = new BasicCookieStore();
        // 创建本地的HTTP内容
        HttpContext context = new BasicHttpContext();
        //使用POST方法
        HttpPost httpPost = new HttpPost(link+sid);
        HttpResponse response = null;
        try {
            //httpPost发送的数据
            StringEntity entity = new StringEntity(body);
            httpPost.setEntity(entity);
            httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)");
            httpPost.addHeader("Content-Type", "text/plain");
            // 设置以AJAX方式的http提交
            httpPost.addHeader("X_REQUESTED_WITH", "XMLHttpRequest");
            // 绑定cookie store到本地内容中
            context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            response = httpclient.execute(httpPost, context); //httpclient.execute(httpPost);

        }
        catch (IOException e1) {
            // TODO 自动生成的 catch 块
            e1.printStackTrace();
        }
        String res = null;
        try {
            res = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //res=res.substring(3);
        System.out.print(res);
        return res;
    }

    public void jsonTranslate(String  str,int type) {
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(str).getAsJsonArray();
        Gson gson = new Gson();
        if(type == 1)
        {
            projectData.clear();//清除缓存
            int i = 0;
            for (JsonElement user : jsonArray) {
                ProjectBean projectBean = gson.fromJson(user,ProjectBean.class);
                projectData.add(i,projectBean);
                Log.i("projectname",i+projectData.get(i).getName());
                i++;
            }
            //Log.i("projectdata",projectData.get(0).getId());
        }
        else if(type == 2 ||type == 5) {
            deviceListData.clear();
            int i = 0;
            for(JsonElement user : jsonArray) {
                DeviceListBean device =  gson.fromJson(user,DeviceListBean.class);
                deviceListData.add(i,device);
                i++;
            }
            Log.i("elecid",deviceListData.get(1).getModelId());
        } else if(type == 3) {
            ElectricListData.clear();
            int i = 0;
            for(JsonElement user : jsonArray) {
                ElectricListBean device =  gson.fromJson(user,ElectricListBean.class);
                ElectricListData.add(i,device);
                i++;
            }
        }
        else if(type == 6){
            Log.i("im ","in");
            ElectricListData.clear();
            for(JsonElement user : jsonArray) {
                ElectricListBean device =  gson.fromJson(user,ElectricListBean.class);
                ElectricListData.add(device);
            }
        }
    }

    private void initData() {
        for(int i=0;i<projectData.size();i++) {
            project_name.add(projectData.get(i).getName());
        }
    }
}
