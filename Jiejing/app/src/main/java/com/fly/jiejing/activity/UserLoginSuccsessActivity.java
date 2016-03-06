package com.fly.jiejing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

//用户登录成功后跳到的页面
public class UserLoginSuccsessActivity extends Activity implements View.OnClickListener {
    private ImageView login_sucess_back;
    private TextView user_phone_number;
    private SharedPreferences sp;
    private String phoneNumber;
    private TextView leave;
    private RequestQueue requestQueue;
    private List<User> gData;
    private TextView user_phone_score;//积分
    private TextView user_phone_coupon;
    private TextView user_balance;
    private TextView user_phone_invitation_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_user_login_sucess);
        initView();
        login_sucess_back.setOnClickListener(this);
        phoneNumber = sp.getString("PHONE_NUMBER", null);
        if (phoneNumber != null) {
            user_phone_number.setText(phoneNumber);
        } else {
            Toast.makeText(getApplicationContext(), "还没有注册", Toast.LENGTH_SHORT).show();
        }
        initData();
        leave.setOnClickListener(this);
    }

    //数据初始化
    public void initView() {
        login_sucess_back = (ImageView) findViewById(R.id.login_sucess_back);
        user_phone_number = (TextView) findViewById(R.id.user_phone_number);
        leave = (TextView) findViewById(R.id.leave);
        user_phone_score = (TextView) findViewById(R.id.user_phone_score);
        user_balance = (TextView) findViewById(R.id.user_balance);
        user_phone_coupon = (TextView) findViewById(R.id.user_phone_coupon);
        user_phone_invitation_id = (TextView) findViewById(R.id.user_phone_invitation_id);
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        requestQueue = MainApplication.getApplication().getQueue();
        gData = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_sucess_back:
                finish();
                break;
            case R.id.leave:
                AlertDialog.Builder bulider = new AlertDialog.Builder(this);
                bulider.setTitle("提示信息").setMessage("确定要退出系统？");
                bulider.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("PHONE_NUMBER", null);
                                editor.commit();
                                dialog.dismiss();
                                finish();
                            }
                        }

                );
                bulider.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = bulider.create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                break;
        }
    }

    //网络加载数据
    private void initData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson g = new Gson();
                gData = g.fromJson(s, new TypeToken<ArrayList<User>>() {
                }.getType());
                if (gData != null&gData.size()>0) {
                    User user = gData.get(0);
                    user_phone_score.setText(user.getScore());
                    user_phone_number.setText(user.getUser_id());
                    user_balance.setText(user.getBalance()+"");
                    user_phone_coupon.setText(user.getCoupon_num()+"");
                    user_phone_invitation_id.setText(user.getInvitation_id()+"");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UserLoginSuccsessActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", phoneNumber);
        requestQueue.add(postRequest);
    }
}
