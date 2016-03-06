package com.fly.jiejing.activity;

import android.app.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;

//用户评论已完成的订单
public class MyOrderCommentAct extends Activity implements View.OnClickListener {

    private EditText comment_score;
    private EditText comment_content;
    private TextView send;
    private RequestQueue request;
    private LinearLayout backComment;
    Items items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_comment);
        initView();
        backComment.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    public void initView() {
        items = (Items) getIntent().getSerializableExtra("item");
        request = MainApplication.getApplication().getQueue();
        backComment = (LinearLayout) findViewById(R.id.backComment);
        comment_score = (EditText) findViewById(R.id.comment_score);
        comment_content = (EditText) findViewById(R.id.comment_content);
        send = (TextView) findViewById(R.id.sendComment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backComment:
                finish();
                break;
            case R.id.sendComment:
                initData();

                break;
        }
    }

    //上传订单的评论
    public void initData() {
        if (TextUtils.isEmpty(comment_score.getEditableText().toString())) {
            Toast.makeText(MyOrderCommentAct.this, "您的评分不能为空",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(comment_content.getEditableText().toString())) {

            Toast.makeText(MyOrderCommentAct.this, "您的意见不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            StringPostRequest postRequest = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {

                @Override
                public void onResponse(String s) {
                    finish();
                    Toast.makeText(MyOrderCommentAct.this, "您的评论已提交", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MyOrderCommentAct.this, "连接服务器失败，您的评论暂不能提交", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "comment");
            postRequest.putParams("id", items.getId() + "");
            postRequest.putParams("score", comment_score.getEditableText().toString());
            postRequest.putParams("comment", comment_content.getEditableText().toString());
            request.add(postRequest);
        }
    }
}
