package com.example.dell.sccs_app;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add_server extends AppCompatActivity {

    private EditText mAddress;
    private EditText mPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_server);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mAddress = (EditText) findViewById(R.id.Address);
        mPort = (EditText) findViewById(R.id.port);

        Button ok = (Button) findViewById(R.id.positiveButton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Address = mAddress.getText().toString();
                String Port = mPort.getText().toString();
                Intent intent=new Intent();
                intent.setClass(Add_server.this, Setting.class);

                Bundle bundle= new Bundle();
                bundle.putString("server",Address+":"+Port);
                intent.putExtras(bundle);
                setResult(0x2,intent);
                finish();
            }
        });


        Button cancel = (Button) findViewById(R.id.negativeButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
