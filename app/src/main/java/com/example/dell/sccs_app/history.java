package com.example.dell.sccs_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import static com.example.dell.sccs_app.StaticValue.deletecontrolnum;
import static com.example.dell.sccs_app.StaticValue.deletelampnum;
import static com.example.dell.sccs_app.StaticValue.numbercontrol;
import static com.example.dell.sccs_app.StaticValue.numberlamp;

public class history extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView addcontrol;
    private TextView addlamp;
    private TextView deletecontrol;
    private TextView deletelamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initview();
        configview();

    }

    private void initview(){
        mToolbar = (Toolbar)findViewById(R.id.mtoolbar);
        addcontrol = (TextView)findViewById(R.id.addcontrolnum);
        addlamp = (TextView)findViewById(R.id.addlampnum);
        deletecontrol = (TextView)findViewById(R.id.deletecontrolnum);
        deletelamp = (TextView)findViewById(R.id.deletelampnum);


    }

    private void configview(){
        mToolbar.setTitle(getResources().getString(R.string.hist));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_action_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(history.this,Project.class);
                startActivity(intent);
            }
        });


        addlamp.setText(numberlamp);
        addlamp.setTextSize(56);
        addcontrol.setText(numbercontrol);
        addcontrol.setTextSize(56);
        deletecontrol.setText(String.valueOf(deletecontrolnum));
        deletecontrol.setTextSize(56);
        deletelamp.setText(String.valueOf(deletelampnum));
        deletelamp.setTextSize(56);

    }
}
