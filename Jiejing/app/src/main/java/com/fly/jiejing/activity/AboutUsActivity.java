package com.fly.jiejing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.jiejing.R;

public class AboutUsActivity extends Activity implements View.OnClickListener {
    private TextView us_web;
    private TextView us_weibo;
    private ImageView about_back;
    private TextView us_service_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initView();
        aotoLinkMethod(us_web, "www.xinxinjiajie.com");
        aotoLinkMethod(us_weibo, "weibo.com");
        aotoLinkMethod(us_service_phone, "18363820127");
        about_back.setOnClickListener(this);
    }

    //数据的初始化
    public void initView() {
        us_web = (TextView) findViewById(R.id.us_web);
        about_back = (ImageView) findViewById(R.id.about_back);
        us_weibo = (TextView) findViewById(R.id.us_weibo);
        us_service_phone = (TextView) findViewById(R.id.us_service_phone);

    }

    public void aotoLinkMethod(TextView v, String s) {
        v.setText(s);
        v.setAutoLinkMask(Linkify.ALL);
        v.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_back:
                finish();
                break;
        }
    }
}
