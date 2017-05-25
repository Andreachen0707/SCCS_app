package com.example.dell.sccs_app.FragmentDesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.example.dell.sccs_app.LoginActivity;
import com.example.dell.sccs_app.LoginProcess;
import com.example.dell.sccs_app.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.sccs_app.LoginProcess.getProject;
import static com.example.dell.sccs_app.StaticValue.Lcu_lampData;
import static com.example.dell.sccs_app.StaticValue.StationData;
import static com.example.dell.sccs_app.StaticValue.first_in;
import static com.example.dell.sccs_app.StaticValue.projectData;
import static com.example.dell.sccs_app.StaticValue.projectTemp;
import static com.example.dell.sccs_app.StaticValue.project_get;
import static com.example.dell.sccs_app.StaticValue.test_ssid;

/**
 * Created by dell on 2017/5/4.
 */

public class LampListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ArrayAdapter<String> mArrayAdapter;
    private List<String> lamp_name = new ArrayList<>();

    private ListView lamp_list;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;

    private String ssid;

    private MySend mSend = new MySend();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.lamp_list, container, false);


        initView();
        onRefresh();

      //  Handler handler = new Handler();
      //  handler.postDelayed(new listhandler(), 100);


        lamp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //L修改了全局变量projecttemp
                //如何跳转到地图页面呢？
                /*getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.map,new MapFragment())
                        .commit();*/
                //testViewpager.setCurrentItem(1);
            }
        });

        return mView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swiperefreshlayout_new_2);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.background, R.color.colorPrimary );
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initView(){
        lamp_list=(ListView) mView.findViewById(R.id.lamp_list);
        mArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lamp_name);
        lamp_list.setAdapter(mArrayAdapter);
        lamp_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private class listhandler implements Runnable
    {
        public void run()
        {
            initData();
        }
    }

    private void initData() {
        ssid = StationData.get(test_ssid).getSid();
        mSend.setParam(9,null,null,ssid,null,null,null,null,0,0);
        new Thread(mSend).start();
        lamp_name.clear();
        for (int i = 0; i < Lcu_lampData.size(); i++) {
            lamp_name.add(i, Lcu_lampData.get(i).getName());
        }
        Log.i("lamp list",String.valueOf(lamp_name.size()));
        mArrayAdapter.notifyDataSetChanged();
        lamp_list.setAdapter(mArrayAdapter);

    }

    @Override
    public void onRefresh(){
        Log.i("Refresh test","in");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
        initData();
        mArrayAdapter.notifyDataSetChanged();
        lamp_list.setAdapter(mArrayAdapter);

    }

    public void onBeitieSelected(String ssid) {
        this.ssid = ssid;

        handler.postDelayed(new listhandler(),100);
        first_in=true;


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

            if("9".equals(String.valueOf(type))) {
                //onRefresh();
            }


            if("13".equals(String.valueOf(type))){
                Toast.makeText(getActivity(), "Delete Successful", Toast.LENGTH_LONG).show();

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

    private class MySend implements Runnable
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


}
