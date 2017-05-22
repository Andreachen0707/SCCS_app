package com.example.dell.sccs_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.dell.sccs_app.StaticValue.StationData;
import static com.example.dell.sccs_app.StaticValue.basicURL;
import static com.example.dell.sccs_app.StaticValue.connectURL;

public class Project_list extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list);

        Intent intent = getIntent();

        Toolbar bar=(Toolbar) findViewById(R.id.toolbar);
        bar.setTitle(intent.getExtras().getString("name"));
        bar.setTitleTextColor(getResources().getColor(R.color.white));
        bar.setTitleTextAppearance(this,R.style.ToolBar);
        setSupportActionBar(bar);

        bar.setNavigationIcon(R.drawable.ic_action_back);
        bar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Project_list.this,Project.class);
                startActivity(intent);
                Toast.makeText(Project_list.this,"UP",Toast.LENGTH_SHORT).show();
            }
        });

        initview();

    }

    private void initview(){
        mListView = (ListView) findViewById(R.id.controller_list);
        mArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        mListView.setAdapter(mArrayAdapter);
        for(int i = 0;i<StationData.size();i++) {
            list.add(StationData.get(i).getName());
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //intent.setClass(Project_list.this,)
            }
        });


    }
}
