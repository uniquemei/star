package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.pickerlib.util.TextUtil;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends Activity implements View.OnClickListener {
    private static final String APPKEY = "af9e93920524";
    private static final String APPSECRET = "64e9598b06753a3e3333c3ab9d9fe03e";
    TextView pwd;
    TextView ensure;
    TextView name;
    Button btnRegister;
    Button send;
    ImageView back_register;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        pwd = (TextView) findViewById(R.id.etPwd_Register);
        ensure = (TextView) findViewById(R.id.etEnsure_Register);
        name = (TextView) findViewById(R.id.etPhone_Register);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        send = (Button) findViewById(R.id.btn_send);
        back_register = (ImageView) findViewById(R.id.back_register);
        back_register.setOnClickListener(this);
        send.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_register:
                finish();
                break;
            case R.id.btn_send:
                getPass();
                break;
            case R.id.btnRegister:
                //注册成功
                checkCord();

                break;
        }
    }

    //判断是否已经注册，没有注册则上传注册信息，
    public void goRegister() {
//加载数据
        if (TextUtil.isEmpty(name.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "请填写用户名", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtil.isEmpty(pwd.getText().toString())) {
            Toast.makeText(RegisterActivity.this, "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        } else {
            StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.equals("1")) {
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, UserLoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (s.equals("0")) {
                        Toast.makeText(RegisterActivity.this, "注册不成功", Toast.LENGTH_SHORT).show();

                    } else if (s.equals("2")) {
                        Toast.makeText(RegisterActivity.this, "已存在该用户，忘记密码可找回", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            postRequest.putParams("action", "insert");
            postRequest.putParams("userid", name.getText().toString());
            postRequest.putParams("pwd", pwd.getText().toString());
            MainApplication.getApplication().getQueue().add(postRequest);
        }
    }

    //发送验证码
    public void getPass() {
        // 初始化短信SDK
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
//        // final Handler handler = new Handler(this);
        EventHandler eventHandler = new EventHandler() {
            /**
             * 在操作之后被触发
             *
             * @param event
             *            参数1
             * @param result
             *            参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.
             *            RESULT_ERROR表示操作失败
             * @param data
             *            事件操作的结果
             */
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
        if (!TextUtils.isEmpty(name.getText().toString().trim())) {
            if (name.getText().toString().trim().length() == 11) {
                SMSSDK.getVerificationCode("86", name.getText().toString().trim());
            } else {
                Toast.makeText(RegisterActivity.this, "请输入完整的电话号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
        }
    }

    //判断验证码
    public void checkCord() {
        if (!TextUtils.isEmpty(ensure.getText().toString().trim())) {
            if (ensure.getText().toString().trim().length() == 4) {
                SMSSDK.submitVerificationCode("86", name.getText().toString(), ensure.getText().toString());
            } else {
                Toast.makeText(RegisterActivity.this, "请输入完整的验证码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    goRegister();
//                    //传递数据
//                    sp = RegisterActivity.this.getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_APPEND);
//                    SharedPreferences.Editor editor = sp.edit();
//                    editor.putString("PHONE_NUMBER",name.getText().toString() );
//                    editor.commit();
                    //   startActivity(new Intent(getActivity(), MainActivity.class));
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功

                    Toast.makeText(RegisterActivity.this, "验证码已经发送", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
