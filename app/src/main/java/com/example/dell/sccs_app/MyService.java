package com.example.dell.sccs_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import org.apache.http.HttpEntity;
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

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.dell.sccs_app.StaticValue.askProjectListUrl;
import static com.example.dell.sccs_app.StaticValue.heartUrl;
import static com.example.dell.sccs_app.StaticValue.sid;

/**
 * Created by Administrator on 2017/4/7.
 */

public class MyService extends Service{
    public static final String TAG="MyFirstService";
    private Timer timer;
    @SuppressWarnings({ "deprecation", "resource" })
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            //TODO 这里可以执行定时非UI操作
//            Message message = new Message();
//            message.what = 1;
//            handler.sendMessage(message);
            //登录服务器
            HttpClient httpclient = new DefaultHttpClient();
            // 创建cookie store的本地实例
            CookieStore cookieStore = new BasicCookieStore();
            // 创建本地的HTTP内容
            HttpContext context = new BasicHttpContext();
            //使用POST方法
            HttpPost httpPost = new HttpPost(heartUrl+sid);
            HttpResponse response = null;
            try {
                httpPost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;Windows NT 5.1; SV1; .NET CLR 2.0.50727; CIBA)");
                httpPost.addHeader("Content-Type", "text/plain");
                // 设置以AJAX方式的http提交
                httpPost.addHeader("X_REQUESTED_WITH", "XMLHttpRequest");
                // 绑定cookie store到本地内容中
                context.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
                response = httpclient.execute(httpPost, context);
                //response = httpclient.execute(httpPost);
                Log.e(TAG, response.toString());
            }
            catch (IOException e1) {
                // TODO 自动生成的 catch 块
                e1.printStackTrace();
            }
            Log.e(TAG, "--------HeartConnect--------");
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        Log.e(TAG, "--------onCreate--------");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "--------onStartCommand--------");
        timer.schedule(task, 1000, 60000);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "--------onDestroy--------");
    }

}
