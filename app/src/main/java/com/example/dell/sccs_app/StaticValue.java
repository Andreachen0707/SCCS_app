package com.example.dell.sccs_app;

import com.example.dell.sccs_app.Bean.AidiBean;
import com.example.dell.sccs_app.Bean.ConcentratorBean;
import com.example.dell.sccs_app.Bean.DeviceListBean;
import com.example.dell.sccs_app.Bean.ElectricListBean;
import com.example.dell.sccs_app.Bean.LampListBean;
import com.example.dell.sccs_app.Bean.LcuBean;
import com.example.dell.sccs_app.Bean.Lcu_lampBean;
import com.example.dell.sccs_app.Bean.OpenBean;
import com.example.dell.sccs_app.Bean.ProjectBean;
import com.example.dell.sccs_app.Bean.StationBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/8.
 */

public class StaticValue {
    public static boolean connectState = false;
    public static boolean infostate_1 = false;
    public static boolean infostate_2 = false;
    public static boolean project_get = false;
    public static String set_port = "121.40.34.92:7070";
    public static String basicURL = "http://"+set_port;
    public static String connectURL = basicURL+"/api/json?cmd=login&ctrl=user&version=1&lang=zh_CN";
    public static String sid;
    public static String logout = basicURL+"/api/json?cmd=login&ctrl=logout&version=1&lang=zh_CN&sid=";
    public static String heartUrl = basicURL+"/api/json?cmd=login&ctrl=heart-beat&version=1&lang=zh_CN&sid=";
    public static String askProjectListUrl = basicURL+"/api/json?cmd=project&ctrl=list&version=1&lang=zh_CN&sid=";
    public static String askStationListUrl = basicURL+"/api/json?cmd=station-info&ctrl=list&version=1&lang=zh_CN&sid=";
    public static String askConcentratorUrl = basicURL+"/api/json?cmd=modelRtu&ctrl=page&version=1&lang=zh_CN&sid=";
    public static String askElectricUrl = "http://121.40.34.92:7070/api/json?cmd=modelMeter&ctrl=list&version=1&lang=zh_CN&sid=";
    public static String askAidiUrl = "http://121.40.34.92:7070/api/json?cmd=modelAidi&ctrl=list&version=1&lang=zh_CN&sid=";

    public static String askOpenUrl = "http://121.40.34.92:7070/api/json?cmd=device&ctrl=list&version=1&lang=zh_CN&sid=";
    public static String askLampUrl = "http://121.40.34.92:7070/api/json?cmd=modellamp&ctrl=page&version=1&lang=zh_CN&sid=";
    public static String askLucUrl = "http://121.40.34.92:7070/api/json?cmd=modelLcu&ctrl=list&version=1&lang=zh_CN&sid=";

    public static String addContrallor= "http://121.40.34.92:7070/api/json?cmd=stations&ctrl=add&version=1&lang=zh_CN&sid=";
    public static String addLamp= "http://121.40.34.92:7070/api/json?cmd=lamps&ctrl=add&version=1&lang=zh_CN&sid=";

    public static String sendUrl = "http://121.40.34.92:7070/api/json?cmd=stations&ctrl=add&version=1&lang=zh_CN&sid=";
    public static String addlamp = "http://121.40.34.92:7070/api/json?cmd=lamp&ctrl=latlng&version=1&lang=zh_CN&sid=";
    public static String addlamp2 = "http://121.40.34.92:7070/api/json?cmd=lamps&ctrl=add&version=1&lang=zh_CN&sid=";
    public static String stationquery = "http://121.40.34.92:7070/api/json?cmd=station&ctrl=get&version=1&lang=zh_CN&sid=";
    public static String deletestation = "http://121.40.34.92:7070/api/json?cmd=station&ctrl=deletes&version=1&lang=zh_CN&sid=";
    public static String deletelamp = "http://121.40.34.92:7070/api/json?cmd=lamp&ctrl=deletes&version=1&lang=zh_CN&sid=";

    //每个控制柜的下属灯的信息
    public static String lampquery = "http://121.40.34.92:7070/api/json?cmd=map-item&ctrl=get-lamps&version=1&lang=zh_CN&sid=";


    //所有控制柜的信息
    public static String stationquery_all = "http://121.40.34.92:7070/api/json?cmd=station-info&ctrl=list&version=1&lang=zh_CN&sid=";

    //存储response的组合们
    public static ArrayList<ProjectBean> projectData = new ArrayList<>();
    public static ArrayList<DeviceListBean> deviceListData = new ArrayList<>();
    public static ArrayList<ElectricListBean> ElectricListData = new ArrayList<>();
    public static ArrayList<AidiBean> AidiData = new ArrayList<>();
    public static ArrayList<OpenBean> OpenData = new ArrayList<>();

    public static ArrayList<LampListBean> LampListData = new ArrayList<>();
    public static ArrayList<StationBean> StationData = new ArrayList<>();
    public static ArrayList<LcuBean> LcuData = new ArrayList<>();
    public static ArrayList<Lcu_lampBean> Lcu_lampData = new ArrayList<>();
    public static int projectTemp = 0;
    public static int addaction = 0;
}
