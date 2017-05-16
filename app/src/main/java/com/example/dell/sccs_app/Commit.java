package com.example.dell.sccs_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.dell.sccs_app.Util.DensityUtil;
import com.google.zxing.client.android.CaptureActivity;

/**
 * Created by dell on 2017/5/16.
 */

public class Commit extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_commit, null);
        setContentView(contentView);


        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode == 0x345)&&(requestCode==0x123)) {
            TextView scanResult = (TextView) findViewById(R.id.scan_result);
            if (null != data) {
                Bundle bundle = data.getExtras();
                if(bundle == null)
                    return;
                else {
                    String result = bundle.getString("scan_result");
                    scanResult.setText(result);

                    // String [] temp = null;
                    // temp = result.split("@");

                    // NAME.setText(temp[0]);
                    // UID.setText(temp[1]);

                    WebView browser = (WebView) findViewById(R.id.Towebtest);
                    browser.loadUrl(result);

                    browser.getSettings().setSupportZoom(true);
                    browser.getSettings().setBuiltInZoomControls(true);

                    browser.setWebViewClient(new WebViewClient() {
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                }
            }
        }
        else
            Log.i("result","fail");


    }
}
