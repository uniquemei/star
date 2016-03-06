package com.fly.jiejing.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.ChangePwdActivity;
import com.fly.jiejing.activity.RegisterActivity;

import com.fly.jiejing.pickerlib.util.TextUtil;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;

/**
 * Created by CrosX on 2015/10/13.
 */
public class Login_by_account extends Fragment implements View.OnClickListener {
    View view;
    TextView name;
    TextView pwd;
    Button login;
    Button register;
    TextView forget_pwd;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_by_account_fragment_layout, null);
        initView();
        return view;
    }

    public void initView() {
        name = (TextView) view.findViewById(R.id.login_user_name);
        pwd = (TextView) view.findViewById(R.id.login_user_pwd);
        forget_pwd = (TextView) view.findViewById(R.id.forget_pwd);
        register = (Button) view.findViewById(R.id.login_user_account_register);
        login = (Button) view.findViewById(R.id.login_user_account_login);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        forget_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.login_user_account_register:
                intent.setClass(getActivity(), RegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.login_user_account_login:
                //存在该用户则销毁当前页面,返回登陆信息
                login();
                break;
            case R.id.forget_pwd://登陆用户
                intent.setClass(getActivity(), ChangePwdActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }

    }

    //需要判断用户是否存在，密码是否正确
//加载数据
    public void login() {
        if (TextUtil.isEmpty(name.getText().toString())) {
            Toast.makeText(getActivity(), "请填写用户名", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtil.isEmpty(pwd.getText().toString())) {
            Toast.makeText(getActivity(), "请填写用户名", Toast.LENGTH_SHORT).show();
            return;
        } else {

            StringPostRequest postRequest = new StringPostRequest(UrlUtils.USER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.equals("1")) {
                        //             Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                        //传递数据
                        sp = getActivity().getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_APPEND);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("PHONE_NUMBER",name.getText().toString() );
                        editor.commit();
                    } else if (s.equals("0")) {
                        Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();

                    } else if (s.equals("2")) {
                        Toast.makeText(getActivity(), "不存在该用户", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT).show();

                }
            });
            postRequest.putParams("action", "login");
            postRequest.putParams("type", "1");
            postRequest.putParams("userid", name.getText().toString());
            postRequest.putParams("pwd", pwd.getText().toString());
            MainApplication.getApplication().getQueue().add(postRequest);
        }
    }
}
