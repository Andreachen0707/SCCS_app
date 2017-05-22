package com.example.dell.sccs_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.CookieSyncManager;

import com.example.dell.sccs_app.Bean.AidiBean;
import com.example.dell.sccs_app.Bean.DeviceListBean;
import com.example.dell.sccs_app.Bean.ElectricListBean;
import com.example.dell.sccs_app.Bean.LampListBean;
import com.example.dell.sccs_app.Bean.LcuBean;
import com.example.dell.sccs_app.Bean.Lcu_lampBean;
import com.example.dell.sccs_app.Bean.OpenBean;
import com.example.dell.sccs_app.Bean.ProjectBean;
import com.example.dell.sccs_app.Bean.StationBean;
import com.example.dell.sccs_app.Util.Md5Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.dell.sccs_app.StaticValue.AidiData;
import static com.example.dell.sccs_app.StaticValue.ElectricListData;
import static com.example.dell.sccs_app.StaticValue.LampListData;
import static com.example.dell.sccs_app.StaticValue.LcuData;
import static com.example.dell.sccs_app.StaticValue.Lcu_lampData;
import static com.example.dell.sccs_app.StaticValue.OpenData;
import static com.example.dell.sccs_app.StaticValue.StationData;
import static com.example.dell.sccs_app.StaticValue.addContrallor;
import static com.example.dell.sccs_app.StaticValue.addLamp;
import static com.example.dell.sccs_app.StaticValue.askAidiUrl;
import static com.example.dell.sccs_app.StaticValue.askConcentratorUrl;
import static com.example.dell.sccs_app.StaticValue.askElectricUrl;
import static com.example.dell.sccs_app.StaticValue.askLampUrl;
import static com.example.dell.sccs_app.StaticValue.askLucUrl;
import static com.example.dell.sccs_app.StaticValue.askOpenUrl;
import static com.example.dell.sccs_app.StaticValue.askProjectListUrl;
import static com.example.dell.sccs_app.StaticValue.connectState;
import static com.example.dell.sccs_app.StaticValue.deletelamp;
import static com.example.dell.sccs_app.StaticValue.deletestation;
import static com.example.dell.sccs_app.StaticValue.deviceListData;
import static com.example.dell.sccs_app.StaticValue.infostate_1;
import static com.example.dell.sccs_app.StaticValue.infostate_2;
import static com.example.dell.sccs_app.StaticValue.lampquery;
import static com.example.dell.sccs_app.StaticValue.logout;
import static com.example.dell.sccs_app.StaticValue.projectData;
import static com.example.dell.sccs_app.StaticValue.projectTemp;
import static com.example.dell.sccs_app.StaticValue.project_get;
import static com.example.dell.sccs_app.StaticValue.sid;
import static com.example.dell.sccs_app.StaticValue.stationquery;
import static com.example.dell.sccs_app.StaticValue.stationquery_all;
import static com.example.dell.sccs_app.WebFunction.getRequestHeader;

/**
 * Created by dell on 2017/4/9.
 */

public class LoginProcess extends AppCompatActivity {
    final static String urltest = "http://121.40.34.92:7070/api/json?cmd=login&ctrl=user&version=1&lang=zh_CN";
    //public String sid = "AXWEBSID";
    public Map<String,List<String>> map = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(this);
    }


    public String sendJSON(String user, String password) {
        String json = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", user);
            jsonObject.put("password", password);
            json = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Map<String,List<String>> header(String user, String password){
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        Map<String,List<String>> map = null;
        Map<String,String> head = null;
        try {
            URL url = new URL(urltest);
            urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Connection", "keep-alive");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)");
            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入
            head = getRequestHeader(urlConnection);

            map = urlConnection.getRequestProperties();

            OutputStream os = urlConnection.getOutputStream();
            DataOutputStream wr = new DataOutputStream(os);
            /**
             * JSONObject jsonParam = new JSONObject();
             * jsonParam.put("user", user);
             * jsonParam.put("password", password);
             */

            String data = "{\"langKey\": \"zh_CN\",\"password\": \""+password+"\",\"pkey\": null,\"pkeyMode\": false,\"remember\": false,\"user\": \""+user+"\"}";

            wr.writeBytes(data);
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
            } else {
                // 返回响应信息
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                // 断开连接
                urlConnection.disconnect();
        }

       return map;
    }

    public static List loginByPost(String user, String password) {
        HttpURLConnection urlConnection = null;
        StringBuilder sb = new StringBuilder();
        String axWebSID = "";
        List result = new ArrayList<>();
        result.add(0,"result1");
        result.add(1,"result2");
        result.add(2,"result3");
        result.add(3,"result4");
        result.add(4,"result5");

        CookieStore cookieStore = new BasicCookieStore();
        HttpContext context = new BasicHttpContext();

        try {
            //String spec = "http://121.40.34.92:7070/api/json?cmd=login&ctrl=user&version=1&lang=zh_CN";
            URL url = new URL(urltest);
            urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            urlConnection.setRequestMethod("POST");
            // 设置请求的超时时间
            //urlConnection.setReadTimeout(5000);
            //urlConnection.setConnectTimeout(5000);
            // 传递的数据
            //String data = "{\"langKey\": \"zh_CN\",\"password\": \""+password+"\",\"pkey\": null,\"pkeyMode\": false,\"remember\": false,\"user\": \""+user+"\"}";
            // 设置请求的头
            urlConnection.setRequestProperty("Connection", "keep-alive");
            // 设置请求的头
            //urlConnection.setRequestProperty("X_REQUESTED_WITH", "XMLHttpRequest");
            // 设置请求的头
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.setRequestProperty("Accept", "application/json");

            // 设置请求的头
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)");
            axWebSID = sid(urltest);

            urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
            urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入

            context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);



            //Map<String,List<String>> map = urlConnection.getHeaderFields();

            //setDoInput的默认值就是true
            //获取输出流

            OutputStream os = urlConnection.getOutputStream();
            DataOutputStream wr = new DataOutputStream(os);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("user", user);
            jsonParam.put("password", password);

           // urlConnection.setRequestProperty("Cookie",axWebSID);

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

                Map<String,List<String>> map = urlConnection.getHeaderFields();
                //result(5,map);

                br.close();
            } else {
                // 返回响应信息
                result.set(0 ,urlConnection.getResponseMessage());
                return result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null)
                // 断开连接
                urlConnection.disconnect();
        }
        // 判断数据是否为空
        if (sb != null) {
            // 返回结果
            String response = sb.toString();
            Log.i("response",response);
            String sid = response.substring(response.indexOf("sid")+6,response.indexOf(",")-1);
            result.set(1,response);
            result.set(3,sid);
            return result;
        } else {
            result.set(2,"error");
            return result;
        }
    }

    public static String logon(String url,String user,String password){
        password= Md5Util.md5(password);
        String axWebSID = "";
        //登录服务器
        HttpClient httpclient = new DefaultHttpClient();
        // 创建cookie store的本地实例
        CookieStore cookieStore = new BasicCookieStore();
        // 创建本地的HTTP内容
        HttpContext context = new BasicHttpContext();
        //使用POST方法
        HttpPost httpPost = new HttpPost(url);
        HttpResponse response = null;
        try {
            //String data = "{\"langKey\": \"zh_CN\",\"password\": \""+password+"\",\"pkey\": null,\"pkeyMode\": false,\"remember\": false,\"user\": \""+user+"\"}";
            String data = "{\"password\": \""+password+"\",\"user\": \""+user+"\"}";
            //httpPost发送的数据
            StringEntity entity = new StringEntity(data);
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
        //打印服务器返回的状态
        HttpEntity resEntity = response.getEntity();
        String res = "";
        try {

            res = EntityUtils.toString(response.getEntity());
            System.out.print(res);
            //获取Cookie中的AXWEBSID,用于WebSocket登录
            List<Cookie> cookies = cookieStore.getCookies();
            for(int i=0;i<cookies.size();i++){
                Cookie cookie =cookies.get(i);
                System.out.println("===="+cookie.getName() + "=" + cookie.getValue());
                if("AXWEBSID".equalsIgnoreCase(cookie.getName()) ){
                    axWebSID = cookie.getValue();
//                    if(url.indexOf("?")>0)
//                        url += "&AXWEBSID="+axWebSID;
//                    else
//                        url+="?AXWEBSID" + axWebSID;
                }
            }
        }
        catch (org.apache.http.ParseException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if(res.charAt(0) == '1')
        {
            sid = axWebSID;
            connectState = true;
        }
        return axWebSID;
    }

    public static String getProject(int con_type,String name,String cuid,String ssid,String luid,String lmodelid,String lampid,String lcumodelid,double lat,double lng) {
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

            case 4://灯具型号
                link = askLampUrl;
                projectId = projectData.get(projectTemp).getId();
                body = "{\"page\":1,\"pageSize\":50,\"wheres\":[{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[{\"k\":\"model\",\"v\":\"ASC\"}]}";
                Log.i("body" ,body);
                break;

            case 5:
                link = stationquery_all;
                projectId = projectData.get(projectTemp).getId();
                body =  " {\"pid\":\""+projectId+"\"}";
                break;

            case 6:
                link = askLucUrl;
                projectId = projectData.get(projectTemp).getId();
                body = "{\"wheres\":[{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[]}";
                break;

            case 7://add controller
                link = addContrallor;
                projectId = projectData.get(projectTemp).getId();
                //body = "{\"pid\":\""+projectId+"\",\"name\":\""+name+"\",\"cuid\":\""+cuid+"\",\"ctype\":1,\"cmodel\":\""+deviceListData.get(1).getModelId()+"\",\"devices\":[{\"deviceType\":2,\"modelId\":\""+ElectricListData.get(0).getModelId()+"\",\"name\":\"Built-in METER\"}],\"lat\":"+lat+",\"lng\":"+lng+"}";
                body = "{\"pid\":\""+projectId+"\",\"name\":\""+name+"\",\"cuid\":\""+cuid+"\",\"ctype\":1,\"cmodel\":\""+deviceListData.get(1).getModelId()+"\",\"devices\":[{\"deviceType\":2,\"modelId\":\""+ElectricListData.get(0).getModelId()+"\",\"name\":\"Built-in METER\"},{\"deviceType\":3,\"modelId\":\""+AidiData.get(0).getModelId()+ "\",\"name\":\"Built-in 2AI-4DI-4DO\"}],\"lat\":"+lat+",\"lng\":"+lng+"}";

                Log.i("body" ,body);
                break;

            case 8:
                link = addLamp;
                projectId = projectData.get(projectTemp).getId();
                //body = "{\"luid\":\""+luid+"\",\"lampmodel\":\""+lmodelid+"\",\"pid\":\""+projectId+"\",\"sid\":\""+ssid+"\",\"cuid\":\""+cuid+"\",\"ctype\":1,\"name\":\""+name+"\",\"channel\":1,\"lcumodel\":\""+lcumodelid+"\",\"kmId\":null,\"lat\":"+lat+",\"lng\":"+lng+",\"dirId\":\""+ssid+"\"}";

                body = "{\"luid\":\""+luid+"\",\"lampmodel\":\""+lmodelid+"\",\"pid\":\""+projectId+"\",\"sid\":\""+ssid+"\",\"cuid\":\""+cuid+"\",\"ctype\":1,\"name\":\""+name+"\",\"channel\":1,\"lcumodel\":\""+lcumodelid+"\",\"kmId\":\""+OpenData.get(0).getDeviceId()+"\",\"lat\":"+lat+",\"lng\":"+lng+",\"dirId\":\""+ssid+"\"}";

                Log.i("type 8 body",body);
                break;

            case 9:
                projectId = projectData.get(projectTemp).getId();
                link = lampquery;
                body = "\""+ssid+"\"";
                break;

            case 0:
                link = logout;
                break;

            case 10:
                link = deletestation;
                body = "{\"wheres\":[{\"k\":\"stationId\",\"o\":\"=\",\"v\":\""+ssid+"\"}],\"orders\":[]}";
                break;

            //要开关信息
            case 11:
                projectId = projectData.get(projectTemp).getId();
                link = askAidiUrl;
                body = "{\"wheres\":[{\"k\":\"asDefault\",\"o\":\"=\",\"v\":true},{\"k\":\"projectId\",\"o\":\"=\",\"v\":\""+projectId+"\"}],\"orders\":[]}";
                break;

            case 12:
                link = askOpenUrl;
                body = "{\"wheres\":[{\"k\":\"stationId\",\"o\":\"=\",\"v\":\""+ssid+"\"},{\"k\":\"deviceType\",\"o\":\"=\",\"v\":3},{\"k\":\"type\",\"o\":\"=\",\"v\":3}],\"orders\":[{\"k\":\"dadd\",\"v\":\"ASC\"},{\"k\":\"dch\",\"v\":\"ASC\"}]}";
                break;

            case 13:
                link = deletelamp;
                body = "{\"wheres\":[{\"k\":\"lampId\",\"o\":\"=\",\"v\":\""+lampid+"\"}],\"orders\":[]}";

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


    public static String sid(String url){
        android.webkit.CookieManager cookieManager = android.webkit.CookieManager.getInstance();
        String cookie = cookieManager.getCookie(url);
        String Websid="";
        if (cookie != null) {
            Websid = cookie.substring(cookie.indexOf("AXWEBSID=")+9, cookie.indexOf(";"));
            Log.i("SID", Websid);
        } else
            Log.i("SID", "NULL");
        sid = Websid;
        connectState = true;
        return Websid;
    }

    public static void jsonTranslate(String  str,int type) {
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        //将JSON的String 转成一个JsonArray对象
        JsonArray jsonArray = parser.parse(str).getAsJsonArray();
        Gson gson = new Gson();
        /*if(type == 1)
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
        }*/
        if(type == 2) {
            deviceListData.clear();
            int i = 0;
            for(JsonElement user : jsonArray) {
                DeviceListBean device =  gson.fromJson(user,DeviceListBean.class);
                deviceListData.add(i,device);
                i++;
            }

            //Log.i("elecid",deviceListData.get(1).getModelId());
        } else if(type == 3) {
            ElectricListData.clear();
            int i = 0;
            for(JsonElement user : jsonArray) {
                ElectricListBean device =  gson.fromJson(user,ElectricListBean.class);
                ElectricListData.add(i,device);
                i++;
            }
            infostate_2 = true;
        }
        else if(type == 4){
            Log.i("im ","in");
            LampListData.clear();
            int i = 0;
            for(JsonElement user : jsonArray) {
                LampListBean device =  gson.fromJson(user,LampListBean.class);
                LampListData.add(i,device);
                i++;
            }
        }

        else if(type == 5){
            Log.i("im ","in");
            StationData.clear();
            int i = 0;
            for(JsonElement user : jsonArray) {
                StationBean station=  gson.fromJson(user,StationBean.class);
                StationData.add(i,station);
                Log.i("station id",i+StationData.get(i).getSid());
                i++;
            }
            infostate_1 = true;
        }
        else if(type == 6){
            LcuData.clear();
            int i = 0;
            for(JsonElement user : jsonArray) {
                LcuBean lcumodel=  gson.fromJson(user,LcuBean.class);
                LcuData.add(i,lcumodel);
                i++;
            }
        }
        else if(type == 9){
            Lcu_lampData.clear();
            int i = 0;
            for (JsonElement user : jsonArray){
                Lcu_lampBean lcu_lamp = gson.fromJson(user,Lcu_lampBean.class);
                Lcu_lampData.add(i,lcu_lamp);
                i++;
            }
        }
        else if (type == 11){
            AidiData.clear();
            int i = 0;
            for (JsonElement user : jsonArray){
                AidiBean aidi = gson.fromJson(user,AidiBean.class);
                AidiData.add(i,aidi);
                i++;
            }
        }
        else if(type==12){
            OpenData.clear();
            int i = 0;
            for (JsonElement user : jsonArray){
                OpenBean open = gson.fromJson(user,OpenBean.class);
                OpenData.add(i,open);
                i++;
            }
        }
    }



}