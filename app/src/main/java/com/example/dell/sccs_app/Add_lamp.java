package com.example.dell.sccs_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dell.sccs_app.Util.DensityUtil;
import com.google.zxing.client.android.CaptureActivity;

public class Add_lamp extends AppCompatActivity {

    private TextView mTextMessage;
    private com.getbase.floatingactionbutton.FloatingActionButton scan;
    private com.getbase.floatingactionbutton.FloatingActionButton input;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_add_lamp, null);
        setContentView(contentView);

        scan = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.scan);
        input = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.input);
        //scan.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_button_scan_2));
        //input.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_button_input));

        //scan.getSpringingHandlerController().addSpringingHandler(new SpringingTouchScaleHandler(this, simg_avatarMan));

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        scan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setClass(Add_lamp.this, CaptureActivity.class);
                startActivity(intent);
            }
        });

    }

}
