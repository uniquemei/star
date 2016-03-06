package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

//兑换优惠券的Activity
public class ExchangeActivity extends Activity implements View.OnClickListener {
    private EditText inviteNumber;//填写的邀请码
    private EditText conpouNumber;//优惠码
    private Button exchangeConpou;//兑换优惠券
    private Button exchangeInviteNumber;//兑换邀请码


    private TextView money;
    private ImageView coupon_back;
    private ImageView couponPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_coupon_exchange);
        initData();
        moneyData();
        exchangeConpou.setOnClickListener(this);
        coupon_back.setOnClickListener(this);
        couponPic.setOnClickListener(this);
        exchangeInviteNumber.setOnClickListener(this);
    }

    //数据初始化
    public void initData() {
        exchangeInviteNumber = (Button) findViewById(R.id.exchangeInviteNumber);
        exchangeConpou = (Button) findViewById(R.id.exchangeConpou);
        conpouNumber = (EditText) findViewById(R.id.conpouNumber);
        inviteNumber = (EditText) findViewById(R.id.inviteNumber);

        couponPic = (ImageView) findViewById(R.id.couponPic);
        coupon_back = (ImageView) findViewById(R.id.coupon_back);

        money = (TextView) findViewById(R.id.money);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exchangeConpou:
                checkCouponData();
                moneyData();
                break;
            case R.id.exchangeInviteNumber:
                checkInviteData();
                moneyData();
                break;
            case R.id.coupon_back:
                finish();
                break;
            case R.id.couponPic:
                Intent intent = new Intent(ExchangeActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
        }
    }

    //进行邀请码的判断
    public void checkInviteData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {
                    Toast.makeText(ExchangeActivity.this, "恭喜您获得10元的优惠券", Toast.LENGTH_SHORT).show();
                } else if (s.equals("0")) {
                    Toast.makeText(ExchangeActivity.this, "邀请码无效", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ExchangeActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "invite");
        postRequest.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
        postRequest.putParams("inviteId", inviteNumber.getText().toString());
        MainApplication.getApplication().getQueue().add(postRequest);
    }

    //对优惠券的判断
    public void checkCouponData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {//
                    Toast.makeText(ExchangeActivity.this, "恭喜您获得10元的优惠券", Toast.LENGTH_SHORT).show();
                } else if (s.equals("0")) {
                    Toast.makeText(ExchangeActivity.this, "优惠码无效", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ExchangeActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "coupon");
        postRequest.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
        postRequest.putParams("couponId", conpouNumber.getText().toString());
        MainApplication.getApplication().getQueue().add(postRequest);
    }

    //
    public void moneyData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<User> gData = gson.fromJson(s,new TypeToken<ArrayList<User>>() {}.getType());
                if (gData != null) {
                   User user = gData.get(0);
                    money.setText(user.getCoupon_num() * 10+"");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ExchangeActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
        MainApplication.getApplication().getQueue().add(postRequest);
    }
}
