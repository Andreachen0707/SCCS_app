package com.example.dell.sccs_app.FragmentDesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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

import com.example.dell.sccs_app.Project;
import com.example.dell.sccs_app.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.dell.sccs_app.StaticValue.projectData;
import static com.example.dell.sccs_app.StaticValue.projectTemp;
import static com.example.dell.sccs_app.StaticValue.project_get;

/**
 * Created by dell on 2017/5/4.
 */

public class ProjectFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private List<String> project_name = new ArrayList<String>();
    private ListView project_list;
    private TextView tempProject;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    private ArrayAdapter<String> mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.project_list, container, false);

        initView();


        Handler handler = new Handler();
        handler.postDelayed(new splashhandler(), 2000);


        project_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //L修改了全局变量projecttemp
                projectTemp = position;
                Log.i("project temp",String.valueOf(projectTemp));
                //如何跳转到地图页面呢？
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.map,new MapFragment())
                        .commit();
            }
        });

        return mView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.background, R.color.colorPrimary );
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initData() {
        if(project_name.size()==0) {
            for (int i = 0; i < projectData.size(); i++) {
                project_name.add(i,projectData.get(i).getName());
            }
        }
        else {
            for (int i = 0; i < projectData.size(); i++) {
                project_name.set(i,projectData.get(i).getName());
            }
        }

    }

    private void initView(){
        tempProject =(TextView) mView.findViewById(R.id.tempProject);
        project_list=(ListView) mView.findViewById(R.id.project_list);
        mAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_single_choice,project_name);
        project_list.setAdapter(mAdapter);
        project_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        tempProject.setText("当前项目组 | ");
    }

    private class splashhandler implements Runnable
    {
        public void run()
        {
            initData();
            if(project_get) {
                if (projectTemp >= 0) {
                    project_list.setItemChecked(projectTemp, true);
                    tempProject.setText("当前项目组 | " + project_name.get(projectTemp));
                }
            }
            else
                Toast.makeText(getActivity(),"Pull to refresh",Toast.LENGTH_SHORT).show();
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
        if(project_get) {
            if (projectTemp >= 0) {
                project_list.setAdapter(mAdapter);
                project_list.setItemChecked(projectTemp, true);
                tempProject.setText("当前项目组 | " + project_name.get(projectTemp));
            }
        }
        else
            Toast.makeText(getActivity(),"Pull to refresh",Toast.LENGTH_SHORT).show();
    }

}
