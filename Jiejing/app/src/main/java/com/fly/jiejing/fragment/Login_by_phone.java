package com.fly.jiejing.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.MainActivity;
import com.fly.jiejing.activity.RegisterActivity;
import com.fly.jiejing.pickerlib.util.TextUtil;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by CrosX on 2015/10/13.
 */
//验证码登录
public class Login_by_phone extends Fragment implements View.OnClickListener {
    private static final String APPKEY = "af9e93920524";
    private static final String APPSECRET = "64e9598b06753a3e3333c3ab9d9fe03e";
    private Button toEnsure;
    private Button btnLogin;
    private Button register;
    private EditText etPhone;
    private EditText etPsw;
    private String phoneNums;
    private String psw;
    private boolean ready;
    private int time = 60;
    private SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_by_phone_fragment_layout, null);
        toEnsure = (Button) view.findViewById(R.id.toEnsure);
        btnLogin = (Button) view.findViewById(R.id.login_user_phone_login);
        register = (Button) view.findViewById(R.id.login_user_phone_register);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etPsw = (EditText) view.findViewById(R.id.etPsw);
        toEnsure.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        register.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        phoneNums = etPhone.getText().toString();
        psw = etPsw.getText().toString();
        switch (v.getId()) {
            case R.id.toEnsure:
                //存在该用户成功登陆
                getPass();
                break;
            case R.id.login_user_phone_login:
                checkCord();
                //检查是否存在该用户。不存在则跳入注册
                break;
            case R.id.login_user_phone_register:
                getActivity().finish();
                startActivity(new Intent(getActivity(), RegisterActivity.class));
                break;
        }
    }

    //获取验证码
    public void getPass() {
        // 初始化短信SDK
        SMSSDK.initSDK(getActivity(), APPKEY, APPSECRET);
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
        ready = true;
        if (!TextUtils.isEmpty(phoneNums.trim())) {
            if (phoneNums.trim().length() == 11) {
                SMSSDK.getVerificationCode("86", phoneNums);
                etPsw.requestFocus();
            } else {
                Toast.makeText(getActivity(), "请输入完整的电话号码", Toast.LENGTH_SHORT).show();
                etPhone.requestFocus();
            }
        } else {
            Toast.makeText(getActivity(), "请输入您的电话号码", Toast.LENGTH_LONG).show();
            etPhone.requestFocus();
        }
    }

    //判断验证码
    public void checkCord() {
        if (!TextUtils.isEmpty(psw.trim())) {
            if (psw.trim().length() == 4) {
                SMSSDK.submitVerificationCode("86", phoneNums, psw);
            } else {
                Toast.makeText(getActivity(), "请输入完整的验证码", Toast.LENGTH_SHORT).show();
                etPsw.requestFocus();
            }
        } else {
            Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_LONG).show();
            etPsw.requestFocus();
        }
    }

    //验证码获取后发送提示
    public void reminderText() {
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    Handler handlerText = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (time > 0) {
                    toEnsure.setText("验证码已发送" + time + "秒");
                    toEnsure.setEnabled(false);
                    toEnsure.setBackgroundColor(Color.parseColor("#e6e6fa"));
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                } else {
                    toEnsure.setText("获取验证码");
                    toEnsure.setEnabled(true);
                    toEnsure.setBackgroundColor(Color.parseColor("#fffacd"));
                    time = 60;
                    toEnsure.setVisibility(View.VISIBLE);
                }
            } else {
                etPsw.setText("");
                toEnsure.setText("获取验证码");
                toEnsure.setEnabled(true);
                toEnsure.setBackgroundColor(Color.parseColor("#fffacd"));
                time = 60;
                toEnsure.setVisibility(View.VISIBLE);
            }
        }
    };
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
                    handlerText.sendEmptyMessage(2);
                    login();
                 //   startActivity(new Intent(getActivity(), MainActivity.class));
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getActivity(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表
                    Toast.makeText(getActivity(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                etPhone.requestFocus();
            }
        }
    };

    @Override
    public void onDestroyView() {
        // 销毁回调监听接口
        if (ready) {
            SMSSDK.unregisterAllEventHandler();
        }
        super.onDestroyView();
    }

    //需要判断是否存在该用户，不存在的时候也不能登陆，需要者注册

    //加载数据
    public void login() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {
                    Toast.makeText(getActivity(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    //传递数据
                    sp = getActivity().getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_APPEND);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("PHONE_NUMBER", phoneNums);
                    editor.commit();
                    //   Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else if (s.equals("0")) {
                    Toast.makeText(getActivity(), "请用户先注册", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }

        });
        postRequest.putParams("action", "login");
        postRequest.putParams("type", "2");
        postRequest.putParams("userid", etPhone.getText().toString());
        MainApplication.getApplication().getQueue().add(postRequest);

    }
}
