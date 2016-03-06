package com.fly.jiejing.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;

//提交建议的Activity
public class MySuggestionActivity extends Activity implements View.OnClickListener{
   private ImageView sugession_back;
    private EditText sugession_con;
    private TextView send;
    private RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_suggestion);
        initView();
        sugession_back.setOnClickListener(this);
        send.setOnClickListener(this);
    }
    public void initView(){
        request= MainApplication.getApplication().getQueue();
        sugession_back=(ImageView)findViewById(R.id.sugession_back);
        sugession_con=(EditText)findViewById(R.id.sugession_con);
        send=(TextView)findViewById(R.id.send);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sugession_back:
                finish();
                break;
            case R.id.send:
                initData();
                break;
        }
    }
    //网络上传数据
    public void initData(){
        if (TextUtils.isEmpty(sugession_con.getEditableText().toString())) {
            Toast.makeText(MySuggestionActivity.this, "您的意见不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        }else{
            StringPostRequest postRequest = new StringPostRequest(UrlUtils.SHOW_ITEMS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    finish();
                    Toast.makeText(MySuggestionActivity.this, "您的意见已提交，我们会尽快回复！", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MySuggestionActivity.this, "连接服务器失败，您的建议暂不能提交", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "advice");
            postRequest.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
            postRequest.putParams("advice", sugession_con.getText().toString());
            request.add(postRequest);
        }

    }
}
