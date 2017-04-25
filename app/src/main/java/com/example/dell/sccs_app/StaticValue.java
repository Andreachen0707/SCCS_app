package com.example.dell.sccs_app;

import com.example.dell.sccs_app.Bean.DeviceListBean;
import com.example.dell.sccs_app.Bean.ElectricListBean;
import com.example.dell.sccs_app.Bean.ProjectBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/8.
 */

public class StaticValue {
    public static boolean connectState = false;
    public static String basicURL = "http://121.40.34.92:7070";
    public static String connectUrl = "http://121.40.34.92:7070/api/json?cmd=login&ctrl=user&version=1&lang=zh_CN";
    public static String sid;
    public static String heartUrl = "http://121.40.34.92:7070/api/json?cmd=login&ctrl=heart-beat&version=1&lang=zh_CN&sid=";
    public static String askProjectListUrl = "http://121.40.34.92:7070/api/json?cmd=project&ctrl=list&version=1&lang=zh_CN&sid=";
    public static String askStationListUrl = "http://121.40.34.92:7070/api/json?cmd=station-info&ctrl=list&version=1&lang=zh_CN&sid=";
    public static String askConcentratorUrl = "http://121.40.34.92:7070/api/json?cmd=modelRtu&ctrl=page&version=1&lang=zh_CN&sid=";
    public static String askElectricUrl = "http://121.40.34.92:7070/api/json?cmd=modelMeter&ctrl=list&version=1&lang=zh_CN&sid=";
    public static String sendUrl = "http://121.40.34.92:7070/api/json?cmd=stations&ctrl=add&version=1&lang=zh_CN&sid=";
    public static String addlamp = "http://121.40.34.92:7070/api/json?cmd=lamp&ctrl=latlng&version=1&lang=zh_CN&sid=";
    public static String addlamp2 = "http://121.40.34.92:7070/api/json?cmd=lamps&ctrl=add&version=1&lang=zh_CN&sid=";
    public static String stationquery = "http://121.40.34.92:7070/api/json?cmd=station&ctrl=get&version=1&lang=zh_CN&sid=";
    public static String stationquery_all = "http://121.40.34.92:7070/api/json?cmd=station-info&ctrl=list&version=1&lang=zh_CN&sid=";
    public static ArrayList<ProjectBean> projectData = new ArrayList<>();
    public static ArrayList<DeviceListBean> deviceListData = new ArrayList<>();
    public static ArrayList<ElectricListBean> ElectricListData = new ArrayList<>();
    public static int projectTemp = 0;
}