package com.example.dell.sccs_app.FragmentDesign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.sccs_app.R;

/**
 * Created by dell on 2017/5/4.
 */

public class MapFragment extends Fragment {
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.project_list, container, false);

            TextView tv_tabName = (TextView) rootView.findViewById(R.id.tv_tabName);

            Bundle bundle = getArguments();

            tv_tabName.setText("Test");

            return rootView;
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onActivityCreated(savedInstanceState);
        }

    }

