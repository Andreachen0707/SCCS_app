package com.example.dell.sccs_app.Widgets;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dell.sccs_app.R;

/**
 * Created by dell on 2017/5/4.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;

    public RecyclerViewHolder(View v){
        super(v);
        mTextView = (TextView)v.findViewById(R.id.id_recycler_test);

    }
}
