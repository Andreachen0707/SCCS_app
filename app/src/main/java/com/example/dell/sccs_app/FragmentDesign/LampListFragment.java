package com.example.dell.sccs_app.FragmentDesign;

import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.example.dell.sccs_app.LoginActivity;
import com.example.dell.sccs_app.LoginProcess;
import com.example.dell.sccs_app.R;

import java.math.BigDecimal;
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

    private ArrayList<ListItem> mLampList;
    private MainListViewAdapter mLampAdapter;
    public int clickPosition = -1;

    private String ssid;

    private MySend mSend = new MySend();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.lamp_list, container, false);
        mLampList = new ArrayList<ListItem>();


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
        //mArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,lamp_name);
        mLampAdapter = new MainListViewAdapter();
        lamp_list.setAdapter(mLampAdapter);
        //lamp_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
        mLampList.clear();
        for (int i = 0; i < Lcu_lampData.size(); i++) {
            ListItem item = new ListItem();
            BigDecimal bg1 = new BigDecimal(Lcu_lampData.get(i).getLat());
            BigDecimal bg2 = new BigDecimal(Lcu_lampData.get(i).getLng());
            lamp_name.add(i, Lcu_lampData.get(i).getName());
            item.setImage(getResources().getDrawable(R.drawable.icons_pot_1));
            item.setTitle(Lcu_lampData.get(i).getName());
            item.setDetail1(Lcu_lampData.get(i).getLuid());
            item.setDetail2(bg1.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            item.setDetail3(bg2.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            mLampList.add(item);
        }
        Log.i("lamp list",String.valueOf(lamp_name.size()));
        //mArrayAdapter.notifyDataSetChanged();
        //lamp_list.setAdapter(mArrayAdapter);

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
        //mArrayAdapter.notifyDataSetChanged();
        mLampAdapter.notifyDataSetChanged();
        lamp_list.setAdapter(mLampAdapter);

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

    //带图标的list
    private class ListItem {
        private Drawable image;
        private String title;
        private String detail1;
        private String detail2;
        private String detail3;

        public Drawable getImage() {
            return image;
        }

        public void setImage(Drawable image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDetail1() {
            return detail1;
        }

        public void setDetail1(String detail1){
            this.detail1 = detail1;
        }

        public String getDetail2() {
            return detail2;
        }

        public void setDetail2(String detail2){
            this.detail2 = detail2;
        }

        public String getDetail3() {
            return detail3;
        }
        public void setDetail3(String detail3){
            this.detail3 = detail3;
        }

    }

    private class ListItemView {
        ImageView imageView;
        TextView textView;
        TextView hide_1;
        TextView hide_2;
        TextView hide_3;
        TextView hide_4;
        LinearLayout hide_view;

    }

    private class MainListViewAdapter extends BaseAdapter implements View.OnClickListener{

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mLampList.size();
        }

        /**
         * 返回item的内容
         */
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mLampList.get(position);
        }

        /**
         * 返回item的id
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        /**
         * 返回item的视图
         */
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.item, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                listItemView.textView = (TextView) convertView
                        .findViewById(R.id.title);
                listItemView.hide_view = (LinearLayout) convertView.findViewById(R.id.item_hide);
                listItemView.hide_1 = (TextView) convertView.findViewById(R.id.hide_1);
                listItemView.hide_2 = (TextView) convertView.findViewById(R.id.hide_2);
                listItemView.hide_3 = (TextView) convertView.findViewById(R.id.hide_3);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            if (clickPosition == position) {
                if(listItemView.imageView.isSelected()) {
                    listItemView.imageView.setSelected(false);
                    listItemView.hide_view.setVisibility(View.GONE);
                }
                else {
                    listItemView.imageView.setSelected(true);
                    listItemView.hide_view.setVisibility(View.VISIBLE);
                }
            }
            else {
                listItemView.hide_view.setVisibility(View.GONE);
                listItemView.imageView.setSelected(false);
            }
            listItemView.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPosition = position;
                    notifyDataSetChanged();
                }
            });

            // 获取到mList中指定索引位置的资源
            Drawable img = mLampList.get(position).getImage();
            String title = mLampList.get(position).getTitle();
            String detail1 = mLampList.get(position).getDetail1();
            String detail2 = mLampList.get(position).getDetail2();
            String detail3 = mLampList.get(position).getDetail3();

            // 将资源传递给ListItemView的两个域对象
            listItemView.imageView.setImageDrawable(img);
            listItemView.textView.setText(title);
            listItemView.hide_1.setText(detail1);
            listItemView.hide_2.setText(detail2);
            listItemView.hide_3.setText(detail3);

            // 返回convertView对象
            return convertView;
        }
        public void onClick(View v){

        }

    }

}
