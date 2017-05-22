package com.example.dell.sccs_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.sccs_app.Bean.DeviceListBean;
import com.example.dell.sccs_app.Bean.ElectricListBean;
import com.example.dell.sccs_app.Bean.LampListBean;
import com.example.dell.sccs_app.Bean.ProjectBean;
import com.example.dell.sccs_app.Bean.StationBean;
import com.example.dell.sccs_app.FragmentDesign.MapFragment;
import com.example.dell.sccs_app.FragmentDesign.ProjectFragment;
import com.example.dell.sccs_app.Util.DensityUtil;
import com.example.dell.sccs_app.Widgets.Fragments;
import com.example.dell.sccs_app.Widgets.ViewPagerAdapter;
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


import static android.support.design.widget.TabLayout.MODE_SCROLLABLE;
import static com.example.dell.sccs_app.LoginProcess.getProject;
import static com.example.dell.sccs_app.StaticValue.ElectricListData;
import static com.example.dell.sccs_app.StaticValue.LampListData;
import static com.example.dell.sccs_app.StaticValue.StationData;
import static com.example.dell.sccs_app.StaticValue.addContrallor;
import static com.example.dell.sccs_app.StaticValue.addLamp;
import static com.example.dell.sccs_app.StaticValue.askConcentratorUrl;
import static com.example.dell.sccs_app.StaticValue.askElectricUrl;
import static com.example.dell.sccs_app.StaticValue.askLampUrl;
import static com.example.dell.sccs_app.StaticValue.askProjectListUrl;
import static com.example.dell.sccs_app.StaticValue.askStationListUrl;
import static com.example.dell.sccs_app.StaticValue.deviceListData;
import static com.example.dell.sccs_app.StaticValue.infostate_1;
import static com.example.dell.sccs_app.StaticValue.infostate_2;
import static com.example.dell.sccs_app.StaticValue.logout;
import static com.example.dell.sccs_app.StaticValue.projectData;
import static com.example.dell.sccs_app.StaticValue.projectTemp;
import static com.example.dell.sccs_app.StaticValue.project_get;
import static com.example.dell.sccs_app.StaticValue.sendUrl;
import static com.example.dell.sccs_app.StaticValue.sid;
import static com.example.dell.sccs_app.StaticValue.stationquery;
import static com.example.dell.sccs_app.StaticValue.stationquery_all;


public class Project extends AppCompatActivity
        implements ViewPager.OnPageChangeListener,OnMapReadyCallback {

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
    private WebClient mClient;
    private Timer _axTimer = new Timer();
    private  MyThread2 m1 = new MyThread2();
    private  MyThread2 m2 = new MyThread2();
    private  MyThread2 m3 = new MyThread2();
    private  MyThread2 m4 = new MyThread2();
    private  MyThread2 m5 = new MyThread2();
    private  MyThread2 m6 = new MyThread2();
    private int mapIndex = 0;

    public static String username;
    private TextView show_name;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private DrawerLayout mDrawerLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private NavigationView mNavigationView;

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> mFragments;
    private AppBarLayout  mAppBarLayout;
    private String[] mTitles;
    private String[] mtest;

    private Fragment Fragment_Map;
    private Fragment Fragment_Project;
    private Fragment Fragment_History;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        connectToServer("121.40.34.92","7070","1");


        initViews();
        initSet();
        configview();

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

    @Override
    public void onPageSelected(int position) {
        mToolbar.setTitle(mTitles[position]);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageScrollStateChanged(int state) {

    }

    private void initViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.content_main);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.id_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        //mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_floatingactionbutton);
        mNavigationView =  (NavigationView) findViewById(R.id.nav_view);
        connectType = 1;
        m1.setName(2);
        m2.setName(3);
        m3.setName(4);
        m4.setName(5);
        m5.setName(6);
        m6.setName(11);
        new Thread(networkTask).start();
    }

    private void initSet() {

        // Tab的标题采用string-array的方法保存，在res/values/arrays.xml中写
        mTitles = getResources().getStringArray(R.array.tab_titles);
        mtest = getResources().getStringArray(R.array.test_list);

        //初始化填充到ViewPager中的Fragment集合
        mFragments = new ArrayList<>();
        Fragment_Project = new ProjectFragment();
        /*Bundle mBundle = new Bundle();
        mBundle.putInt("flag",0);
        Fragment_Project = new Fragments();
        Fragment_Project.setArguments(mBundle);
        */
        Fragment_Map = new MapFragment();

        mFragments.add(0,Fragment_Project);
        mFragments.add(1,Fragment_Map);
        /*for (int i = 0; i < mTitles.length; i++) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("flag", i);
            Fragments mFragment = new Fragments();
            mFragment.setArguments(mBundle);
            mFragments.add(i, mFragment);
        }*/
        //Fragment_History =getItem(0);
        //mFragments.add(2,Fragment_History);
    }


    private void configview(){
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerOpened(View drawerView) {
                show_name = (TextView)findViewById(R.id.username);
                show_name.setText(username);
            }
        };
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //mNavigationView.inflateHeaderView(R.layout.nav_header_project);
        //mNavigationView.inflateMenu(R.menu.activity_project_drawer);
        //show_name = (TextView)findViewById(R.id.username);
        //show_name.setText(username);
        onNavgationViewMenuItemSelected(mNavigationView);

        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mTitles, mFragments);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 设置ViewPager最大缓存的页面个数
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setCurrentItem(1);
        // 给ViewPager添加页面动态监听器（为了让Toolbar中的Title可以变化相应的Tab的标题）
        mViewPager.addOnPageChangeListener(this);

        mTabLayout.setTabMode(MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        // 将TabLayout和ViewPager进行关联，让两者联动起来
        mTabLayout.setupWithViewPager(mViewPager);
        // 设置Tablayout的Tab显示ViewPager的适配器中的getPageTitle函数获取到的标题
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
    }


    private void onNavgationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressWarnings("StatementWithEmptyBody")
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_camera) {
                    // Handle the camera action
                    //mAppBarLayout.addView(mTabLayout);

                } else if (id == R.id.nav_gallery) {
                    Intent intent = new Intent();
                    intent.setClass(Project.this,Project_list.class);
                    intent.putExtra("name",String.valueOf(item.getTitle()));
                    startActivity(intent);


                }
                else if (id == R.id.nav_manage) {
                    //退出
                    connectType = 0;
                    MyThread2 m4 = new MyThread2();
                    m4.setName(connectType);
                    new Thread(m4).start();
                    Intent intent = new Intent();
                    intent.setClass(Project.this, LoginActivity.class);
                    startActivity(intent);
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

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

    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            int type = connectType;
            String res = getProject(type,name,cuid,null,null,null,null,lat,lng);
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
            if("".equals(val)) {
                Toast.makeText(Project.this,"Log out successful",Toast.LENGTH_SHORT).show();
            }
            else {
                if ("1".equals(type)) {
                    if (val.charAt(0) == '1') {
                        val = val.substring(3);
                        Log.i("type_new", val);
                        jsonProjectTranslate(val, Integer.parseInt(type));
                    }
                }
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
                    else
                        Toast.makeText(Project.this,"Error",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private class MyThread2 implements Runnable
    {
        private int type;
        public void setName(int name)
        {
            this.type = name;
        }
        public void run()
        {
            String res = getProject(type,name,cuid,null,null,null,null,lat,lng);
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
                body = "{\"wheres\":[{\"k\":\"asDefault\",\"o\":\"=\",\"v\":true},{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[]}";
                break;

            case 4://lamp interface
                link = askLampUrl;
                projectId = projectData.get(projectTemp).getId();
                body = "{\"page\":1,\"pageSize\":50,\"wheres\":[{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[{\"k\":\"model\",\"v\":\"ASC\"}]}";
                Log.i("body" ,body);
                break;

            case 5://add controller
                link = addContrallor;
                projectId = projectData.get(projectTemp).getId();
                body = "{\"pid\":\""+projectId+"\",\"name\":\""+name+"\",\"cuid\":\""+cuid+"\",\"ctype\":1,\"cmodel\":\""+deviceListData.get(1).getModelId()+"\",\"devices\":[{\"deviceType\":2,\"modelId\":\""+ElectricListData.get(0).getModelId()+"\",\"name\":\"Built-in METER\"}],\"lat\":"+lat+",\"lng\":"+lng+"}";
                Log.i("body" ,body);
                break;
            case 6:
                link = addLamp;
                projectId = projectData.get(0).getId();
                body =  " {\"pid\":\""+projectId+"\"}";
                break;
            case 7:
                link = stationquery;
                body = "{\"wheres\":[{\"k\":\"stationId\",\"o\":\"=\",\"v\":\"a5cff7fddf19414289ea8d6959e1021b\"}],\"orders\":[]}";
                break;
            case 0:
                link = logout;
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

    public void jsonProjectTranslate(String  str,int type) {
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
            project_get = true;
            new Thread(m1).start();
            new Thread(m2).start();
            new Thread(m3).start();
            new Thread(m4).start();
            new Thread(m5).start();
            new Thread(m6).start();
        }
        else
            Log.i("failed","yes failed");
    }


    //连websocket
    public void connectToServer(String ip, String port, String type) {
        String address = String.format("ws://%s:%s/api/ws", ip, port);
        Draft draft = new Draft_17();

        try {
            URI uri = new URI(address);
            if("1".equals(type)){
                mClient = new WebClient(uri,draft);
                mClient.connect();
                if(mClient.isOpen()) {
                    Log.i("Test new","Success");
                    JSONObject jsonParam = new JSONObject();
                    try {
                        /*jsonParam.put("cmd", "push-lcu");
                        jsonParam.put("ctrl", "action");
                        jsonParam.put("ver", "1");*/
                        jsonParam.put("sid", sid);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    mClient.senddata("login","session","1","","","0",jsonParam.toString());
                    Log.i("sid",jsonParam.toString());
                    //mClient.senddata("login", "session","1","zh_CN", "" , "" , "",sid);
                    startHeartTimer(true);

                }
                else
                    Log.i("Test new","fail");
            }
            else if(type == "0"){
                mClient.connect();
                if(mClient.isOpen()) {
                    startHeartTimer(true);
                    Log.i("Test new","Success");
                }
                else
                    Log.i("Test new","fail");
            }

            //mClient.onWebsocketPing(mClient,null);

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        /**
         * catch (InterruptedException f){
         f.printStackTrace();
         }
         */


    }

    //发送心跳包
    public void startHeartTimer(boolean start) {
        if (start) {
            if (_axTimer == null)
                _axTimer = new Timer();
            _axTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // TODO 自动生成的方法存根
                    mClient.senddata("login", "heart-beat","1","zh_CN","" , "0" , "");
                }
            }, 1000, 60000);
        } else {
            _axTimer.cancel();
        }
    }

    public class WebClient extends WebsocketTest{

        public WebClient(URI serverUri, Draft draft) {
            super(serverUri,draft);
        }

        /*public WebClient(URI serverUri, Draft draft,Map<String, List<String>> httpHeaders, int connectTimeout){
            super(serverUri,draft,httpHeaders,connectTimeout);
        }*/
        public WebClient(URI serverUri, Draft draft,Map<String, String> httpHeaders, int connectTimeout){
            super(serverUri,draft,httpHeaders,connectTimeout);
        }

        @Override
        public void onOpen(ServerHandshake handShakeData) {
            Log.i("handshake","success");
            JSONObject jsonParam = new JSONObject();
            try {
                        /*jsonParam.put("cmd", "push-lcu");
                        jsonParam.put("ctrl", "action");
                        jsonParam.put("ver", "1");*/
                jsonParam.put("sid",""+sid+"");

            }catch (JSONException e){
                e.printStackTrace();
            }
            mClient.senddata("login","session","1","","","0",jsonParam.toString());
            Log.i("sid test",jsonParam.toString());
            //mClient.senddata("login", "session","1","zh_CN", "" , "" , "",sid);
            if(mClient.isOpen()) {
                startHeartTimer(true);
                mClient.senddata("login", "add-listener","1","zh_CN","" , "0" , "{\"cmd\":\"push-rtu\",\"ctrl\":\"info\",\"ver\":\"1\"}");
                mClient.senddata("login", "add-listener","1","zh_CN","" , "0" , "{\"cmd\":\"push-lcu\",\"ctrl\":\"status\",\"ver\":\"1\"}");
                mClient.senddata("login", "add-listener","1","zh_CN","" , "0" , "{\"cmd\":\"push-lcu\",\"ctrl\":\"data\",\"ver\":\"1\"}");
                mClient.senddata("login", "add-listener","1","zh_CN","" , "0" , "{\"cmd\":\"push-lcu\",\"ctrl\":\"action\",\"ver\":\"1\"}");
                mClient.senddata("login", "add-listener","1","zh_CN","" , "0" , "{\"cmd\":\"push-alarm\",\"ctrl\":\"alarm\",\"ver\":\"1\"}");
                mClient.senddata("login", "add-listener","1","zh_CN","" , "0" , "{\"cmd\":\"push-process\",\"ctrl\":\"info\",\"ver\":\"1\"}");
            }
            /*Message msg = new Message();
            msg.what = STATUS_CONNECT;
            msg.obj = String.format("[Welcome：%s]", getURI());
            mHandle.sendMessage(msg);*/
        }

        @Override
        public void onMessage(String message) {
            Log.i("msg",message);
            /**
             * Message msg = new Message();
             * msg.what = STATUS_MESSAGE;
             * msg.obj = message;
             * mHandle.sendMessage(msg);
             */
        }

        @Override
        public void onWebsocketMessageFragment (WebSocket conn, Framedata f){
            Log.i("frame",f.getPayloadData().toString());
        }
        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.i("Login",reason);
            startHeartTimer(false);
            /**
             * Message msg = new Message();
             *  msg.what = STATUS_CLOSE;
             msg.obj = String.format("[Bye：%s]", getURI());
             mHandle.sendMessage(msg);
             */

        }

        @Override
        public void onWebsocketPong(WebSocket conn, Framedata f) {
            super.onWebsocketPong(conn, f);
            String value = parseFramedata(f);
            Log.i("ping",f.toString());

           /* Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = "pong:" + value;
            mHandle.sendMessage(msg);*/
        }


        @Override
        public void onWebsocketPing(WebSocket conn, Framedata f) {
            super.onWebsocketPing(conn, f);
            String result = parseFramedata(f);
            Log.i("ping",result);

            String value = parseFramedata(f);

           /* Message msg = new Message();
            msg.what = STATUS_MESSAGE;
            msg.obj = "ping:" + value;
            mHandle.sendMessage(msg);*/
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
        }

        /* ClientEndpointConfig config = ClientEndpointConfig.Builder.create().configurator(new ClientEndpointConfig.Configurator() {
                @Override
                public void beforeRequest(Map<String, List<String>> headers) {
                    super.beforeRequest(headers);
                    List<String> cookieList = headers.get("Cookie");
                    if (null == cookieList) {
                        cookieList = new ArrayList<>();
                    }
                    cookieList.add("AXWEBSID=" + 1); // set your cookie value here
                    headers.put("Cookie", cookieList);}
            }).build();*/

        public String parseFramedata(Framedata framedata) {
            String result = "null";
            ByteBuffer buffer = framedata.getPayloadData();
            if (null == buffer) {
                return result;
            }
            byte[] data = buffer.array();
            if (null != data && data.length > 0) {
                return new String(data);
            }
            return result;
        }

        public void senddata(String cmd,String ctrl,String version,String lang,String cuid,String ctype,String data){

            String type = cmd + "|" + ctrl + "|" + version + "|" + getNextMappingIndex();
            String message = "0|" + type + "|1||" + lang + "|" + cuid + "|" + ctype + "|" + data;

            FramedataImpl1 resp = new FramedataImpl1(Framedata.Opcode.TEXT);
            //System.out.println("=============WebSocket send:" + message);
            try {
                resp.setFin(true);
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                resp.setPayload(buffer);
                Log.i("message test", message);
                mClient.sendFrame(resp);
                /**
                 * WSManager.notifySocketEvent(new WSocketEvent(_self,WSEventType.WS_SEND, message));
                 */
            } catch (Exception e) {
// TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }

        public void sendMessage(String cmd,String ctrl,String version,String cuid,String ctype,String data){
            String type = cmd + "|" + ctrl + "|" + version + "|" + getNextMappingIndex();
            String message = "0|" + type + "|1||" + "zh_CN" + "|" + cuid + "|" + ctype + "|" + (data!=null?data:"");
            FramedataImpl1 resp = new FramedataImpl1(Framedata.Opcode.PING);
            //System.out.println("=============WebSocket send:" + message);
            try {
                resp.setFin(true);
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                resp.setPayload(buffer);
                mClient.sendFrame(resp);
                /**
                 * WSManager.notifySocketEvent(new WSocketEvent(_self,WSEventType.WS_SEND, message));
                 */
            } catch (Exception e) {
// TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }

        public int getNextMappingIndex() {
            return mapIndex++;
        }
    }

    public void popBackStack(String name,int ind){

    }

    //其他fragment的调用接口
    public WebClient getmClient(){
        return mClient;
    }

    public ViewPager getmViewPager(){
        return mViewPager;
    }
}
