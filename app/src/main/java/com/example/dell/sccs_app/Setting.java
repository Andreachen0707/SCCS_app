package com.example.dell.sccs_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class Setting extends AppCompatActivity {

    private RadioGroup listgroup;
    private ImageButton add;
    private TextView test = null;

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbarsetting=(Toolbar) findViewById(R.id.toolbar);
        toolbarsetting.setTitle("");
        setSupportActionBar(toolbarsetting);

        listgroup = (RadioGroup) findViewById(R.id.ServeGroup);
        test = (TextView) findViewById(R.id.test);
        add=(ImageButton)findViewById(R.id.btnright);

        toolbarsetting.setNavigationIcon(R.drawable.ic_action_back);
        toolbarsetting.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Setting.this,LoginActivity.class);
                startActivity(intent);
                Toast.makeText(Setting.this,"UP",Toast.LENGTH_SHORT).show();
            }
        });


        add.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentnew = new Intent(Setting.this,Add_server.class);
                startActivityForResult(intentnew,0x1);
            }
        });

        listgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton now = (RadioButton)Setting.this.findViewById(radioButtonId);
                test.setText(now.getText());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((resultCode == 0x2)&&(requestCode==0x1)){
            Bundle bundle = data.getExtras();
            String server = bundle.getString("server");
            addlist(listgroup,server);
        }
    }

    public void addlist(RadioGroup radioGroup,String index){
        RadioButton button=new RadioButton(this);
        radioGroup.addView(button);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.setMargins(0,dip2px(this,22),0,0);//4个参数按顺序分别是左上右下
        button.setLayoutParams(layoutParams);
        button.setText(index);
        //radioGroup.addView(button,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}
