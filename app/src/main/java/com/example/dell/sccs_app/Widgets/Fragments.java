package com.example.dell.sccs_app.Widgets;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dell.sccs_app.FragmentDesign.MapFragment;
import com.example.dell.sccs_app.R;

/**
 * Created by dell on 2017/5/4.
 */

public class Fragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener,RecyclerViewAdapter.OnItemClickListener {
    private static final int List = 0;
    private static final int Map = 1;
    private static final int History = 2;

    private static final int SPAN_COUNT = 2;
    private int flag = 0;

    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerViewAdapter mRecyclerViewAdaptor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.frag, container, false);
        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.id_recyclerview);

        flag = (int) getArguments().get("flag");
        configRecyclerView();

        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.background, R.color.colorPrimary );
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void configRecyclerView(){
        switch (flag){
            case Map:

                break;
            case List:
                Log.i("List test","in");
                //mLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

                break;
            case History:
                Log.i("History test","in");
                break;
            default:
                break;
        }
        //mRecyclerViewAdaptor = new RecyclerViewAdapter(getActivity());
        //mRecyclerViewAdaptor.setOnItemClickListener(this);
        //mRecyclerView.setAdapter(mRecyclerViewAdaptor);
        //mRecyclerView.setLayoutManager(mLayoutManager);

    }

    @Override
    public void onRefresh(){
        Log.i("Refresh test","in");
    }
    @Override
    public void onItemClick(View view, int position) {
        Snackbar.make(mRecyclerView, getString(R.string.list), 0);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Snackbar.make(mRecyclerView, getString(R.string.map), 0);
    }

}
