package com.example.dell.sccs_app;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.Locale;

public class Language extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        //   final SharedPreferences languagePre= getBaseContext().getSharedPreferences("language_choice", getBaseContext().MODE_PRIVATE);
        //   final int id=languagePre.getInt("id", 0);


        RadioGroup language=(RadioGroup)this.findViewById(R.id.langGroup);
        language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup arg0,int arg1) {
                int radioId = arg0.getCheckedRadioButtonId();
                int languageId;
                switch (radioId) {
                    case R.id.en:
                        languageId = 1;
                        break;
                    case R.id.zh:
                        languageId = 2;
                        break;
                    default:
                        languageId = 0;
                        break;
                }
                //       languagePre.edit().putInt("id", languageId).commit();

                //       ((Activity) getBaseContext()).finish();
                changeLanguage(languageId);
                Intent intent=new Intent();
                intent.setClass(Language.this, LoginActivity.class);

                Bundle bundle= new Bundle();
                bundle.putInt("id",languageId);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    public void changeLanguage(int id){
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Log.d("MainActivity","language_id="+id);
        switch (id){
            case 0:
                config.locale = Locale.getDefault();
                break;
            case 1:
                config.locale = Locale.ENGLISH;
                break;
            case 2:
                config.locale = Locale.SIMPLIFIED_CHINESE;
                break;
            default:
                config.locale = Locale.getDefault();
                break;
        }
        resources.updateConfiguration(config, dm);
    }
}
