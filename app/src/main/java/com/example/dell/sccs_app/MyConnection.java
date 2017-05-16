package com.example.dell.sccs_app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.dell.sccs_app.Bean.DeviceListBean;
import com.example.dell.sccs_app.Bean.ElectricListBean;
import com.example.dell.sccs_app.Bean.ProjectBean;
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

import java.io.IOException;

import static com.example.dell.sccs_app.StaticValue.ElectricListData;
import static com.example.dell.sccs_app.StaticValue.askConcentratorUrl;
import static com.example.dell.sccs_app.StaticValue.askElectricUrl;
import static com.example.dell.sccs_app.StaticValue.askProjectListUrl;
import static com.example.dell.sccs_app.StaticValue.deviceListData;
import static com.example.dell.sccs_app.StaticValue.logout;
import static com.example.dell.sccs_app.StaticValue.projectData;
import static com.example.dell.sccs_app.StaticValue.projectTemp;
import static com.example.dell.sccs_app.StaticValue.project_get;
import static com.example.dell.sccs_app.StaticValue.sendUrl;
import static com.example.dell.sccs_app.StaticValue.sid;
import static com.example.dell.sccs_app.StaticValue.stationquery;
import static com.example.dell.sccs_app.StaticValue.stationquery_all;

/**
 * Created by dell on 2017/5/15.
 */

public class MyConnection{



}
