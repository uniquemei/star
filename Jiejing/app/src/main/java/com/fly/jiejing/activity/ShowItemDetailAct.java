package com.fly.jiejing.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.entity.Life;

//在下订单的页面展示
public class ShowItemDetailAct extends Activity {

    TextView showlife_title;
    WebView showlife_web;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.three_show_life_layout);
        Items data = (Items) getIntent().getSerializableExtra("item");
        showlife_title = (TextView) findViewById(R.id.showlife_title);
        showlife_title.setText("项目详情");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showlife_web = (WebView) findViewById(R.id.showlife_web);
        showlife_web.getSettings().setJavaScriptEnabled(true);//响应JS控件
        showlife_web.loadUrl(data.getUrl());//
  //      Toast.makeText(this, data.getUrl(), Toast.LENGTH_SHORT).show();
        findViewById(R.id.showlife_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showlife_web.setWebViewClient(new WebViewClient() {
            //网页加载开始时调用，显示加载提示旋转进度条
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
//                Toast.makeText(ElecHall.this, "onPageStarted", 2).show();
            }

            //网页加载完成时调用，隐藏加载提示旋转进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            //网页加载失败时调用，隐藏加载提示旋转进度条
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                progressBar.setVisibility(View.GONE);
            }

        });

    }

}
