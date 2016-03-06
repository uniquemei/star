package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fly.jiejing.R;
//第四块的更多功能
public class MyMoreFunctionActivity extends Activity implements View.OnClickListener{
    private Button aboutUs;
 //   private Button daohang;
    private ImageView more_back;
    private LinearLayout more_version;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_more_function);
        initView();
        aboutUs.setOnClickListener(this);
        more_back.setOnClickListener(this);
        more_version.setOnClickListener(this);
 //       daohang.setOnClickListener(this);
    }
    //数据的初始化
    private void initView(){
        aboutUs = (Button)findViewById(R.id.about_us);

  //      daohang=(Button)findViewById(R.id.daohang);
        more_back=(ImageView)findViewById(R.id.more_back);
        more_version=(LinearLayout)findViewById(R.id.more_version);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_us:
                intent=new Intent(MyMoreFunctionActivity.this,AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.more_back:
                finish();
                break;
            case R.id.more_version:
                Toast.makeText(this,"没有检测到新版本",Toast.LENGTH_SHORT).show();
                break;
           /* case R.id.daohang:
                intent=new Intent(MyMoreFunctionActivity.this,DaoHangActivity.class);
                startActivity(intent);
                break;*/
        }
    }
}
