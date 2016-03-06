package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

//修改密码的Activity
public class ChangePwdActivity extends Activity implements View.OnClickListener {
    //星星佳洁
    private static final String APPKEY = "af9e93920524";
    private static final String APPSECRET = "64e9598b06753a3e3333c3ab9d9fe03e";
    private Button change_btnRegister;
    private Button change_btn_send;
    private EditText change_phone;//输入的手机号
    private EditText chage_code;//验证码
    private EditText change_pass;//修改后的密码
    ImageView change_back_register;
    String phoneNumber;
    String phoneCode;
    String phonePass;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        initView();
        change_btnRegister.setOnClickListener(this);
        change_btn_send.setOnClickListener(this);
        change_back_register.setOnClickListener(this);
    }

    //数据的初始化
    public void initView() {
        change_back_register = (ImageView) findViewById(R.id.change_back_register);
        change_btn_send = (Button) findViewById(R.id.change_btn_send);
        change_btnRegister = (Button) findViewById(R.id.change_btnRegister);
        change_phone = (EditText) findViewById(R.id.change_etPhone_Register);
        chage_code = (EditText) findViewById(R.id.chaneg_etEnsure_Register);
        change_pass = (EditText) findViewById(R.id.change_etPwd_Register);
    }

    //网络修改密码
    public void initPass() {
//        if (TextUtils.isEmpty(phoneNumber) && phoneNumber.trim().length() == 11) {
//            Toast.makeText(ChangePwdActivity.this, "请输入11位的手机号", Toast.LENGTH_SHORT).show();
//        } else if (TextUtils.isEmpty(phoneCode) && phoneCode.trim().length() == 4) {
//            Toast.makeText(ChangePwdActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
//        } else {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {
                    Toast.makeText(ChangePwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    //传递数据
                    sp = ChangePwdActivity.this.getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_APPEND);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("PHONE_NUMBER", phoneNumber);
                    editor.commit();
                } else if (s.equals("0")) {
                    Toast.makeText(ChangePwdActivity.this, "修改不成功", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ChangePwdActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "pwd");
        postRequest.putParams("userid", change_phone.getText().toString());
        postRequest.putParams("pwd", change_pass.getText().toString());
        MainApplication.getApplication().getQueue().add(postRequest);
//        }
    }

    @Override
    public void onClick(View v) {
        phoneNumber = change_phone.getText().toString();
        phoneCode = chage_code.getText().toString();
        phonePass = change_pass.getText().toString();
        switch (v.getId()) {
            case R.id.change_btnRegister:
                if (phoneNumber == null || phoneCode == null || phonePass == null) {
                    Toast.makeText(ChangePwdActivity.this, "请填写注册信息", Toast.LENGTH_SHORT).show();
                } else {
                    checkCord();
                }
                break;
            case R.id.change_btn_send:
                getPass();
                break;
            case R.id.change_back_register:
                finish();
                break;
        }
    }


    //获取验证码
    public void getPass() {
        // 初始化短信SDK
        SMSSDK.initSDK(ChangePwdActivity.this, APPKEY, APPSECRET);
//        // final Handler handler = new Handler(this);
        EventHandler eventHandler = new EventHandler() {
            /**
             * 在操作之后被触发
             * @param event  参数1
             * @param result
             *            参数2 SMSSDK.RESULT_COMPLETE表示操作成功，为SMSSDK.
             *            RESULT_ERROR表示操作失败
             * @param data     事件操作的结果
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

        if (!TextUtils.isEmpty(phoneNumber.trim())) {
            if (phoneNumber.trim().length() == 11) {
                SMSSDK.getVerificationCode("86", phoneNumber);//向手机发送验证码
            } else {
                Toast.makeText(ChangePwdActivity.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePwdActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
        }
    }

    //判断验证码
    public void checkCord() {
        if (!TextUtils.isEmpty(phoneCode.trim())) {
            if (phoneCode.trim().length() == 4) {
                SMSSDK.submitVerificationCode("86", phoneNumber, phoneCode);//验填写的验证码和手机号
            } else {
                Toast.makeText(ChangePwdActivity.this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ChangePwdActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    //上传信息
                    initPass();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功

                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    //             Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangePwdActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
