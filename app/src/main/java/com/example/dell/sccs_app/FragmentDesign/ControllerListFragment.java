package com.example.dell.sccs_app.FragmentDesign;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.dell.sccs_app.Project;
import com.example.dell.sccs_app.Project_list;
import com.example.dell.sccs_app.R;
import com.example.dell.sccs_app.Widgets.ViewPagerAdapter;


import java.util.ArrayList;
import java.util.List;

import static com.example.dell.sccs_app.StaticValue.StationData;
import static com.example.dell.sccs_app.StaticValue.test_ssid;


/**
 * Created by dell on 2017/5/4.
 */

public class ControllerListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    //private onButtonPressListener mListener;

    private ListView control_list;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<ListItem> mList;
    private MainListViewAdapter mTestAdapter;

    private List<String> control_name = new ArrayList<String>();

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;

    private String controlid;

    private ViewPager testViewpager;
    private PagerAdapter testAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.controller_list, container, false);
        mList = new ArrayList<ListItem>();
        testViewpager = ((Project_list)getActivity()).getmViewPager();
        testAdapter =  testViewpager.getAdapter();
        //controlid = StationData.get(0).getSid();

        initView();
        initData();


        //Handler handler = new Handler();
        //handler.postDelayed(new splashhandler(), 2000);


        control_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                test_ssid = position;
                //call.getData(StationData.get(position).getCuid());
                //controlid = StationData.get(position).getSid();
                //mListener.onOKButtonPressed(controlid);

                testViewpager.setCurrentItem(1);
                testAdapter.notifyDataSetChanged();

            }
        });

        return mView;
    }

    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (onButtonPressListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onOkButtonPressed");
        }
    }

    public interface onButtonPressListener {
        void onOKButtonPressed(String ssid);
    }
*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swiperefreshlayout_new);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.background, R.color.colorPrimary );
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        if(control_name.size()==0) {

            for (int i = 0; i < StationData.size(); i++) {
                ListItem item = new ListItem();
                control_name.add(i,StationData.get(i).getName());
                item.setImage(getResources().getDrawable(R.drawable.icons_controller_gray));
                item.setTitle(StationData.get(i).getName());
                mList.add(item);
            }
        }
        else {
            for (int i = 0; i < StationData.size(); i++) {
               control_name.set(i,StationData.get(i).getName());
            }
        }

    }

    private void initView(){
        control_list=(ListView) mView.findViewById(R.id.control_list_1);
        mTestAdapter = new MainListViewAdapter();
        mArrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,control_name);
        control_list.setAdapter(mTestAdapter);
        //control_list.setAdapter(mArrayAdapter);
        //control_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    private class splashhandler implements Runnable
    {
        public void run()
        {
            initData();
        }
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
    }

   private class ListItem {
        private Drawable image;
        private String title;

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

    }

   private class ListItemView {
        ImageView imageView;
        TextView textView;
    }

   private class MainListViewAdapter extends BaseAdapter {

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        /**
         * 返回item的内容
         */
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mList.get(position);
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
        public View getView(int position, View convertView, ViewGroup parent) {
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

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            // 获取到mList中指定索引位置的资源
            Drawable img = mList.get(position).getImage();
            String title = mList.get(position).getTitle();

            // 将资源传递给ListItemView的两个域对象
            listItemView.imageView.setImageDrawable(img);
            listItemView.textView.setText(title);

            // 返回convertView对象
            return convertView;
        }

    }



}
