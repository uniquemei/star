package yu.cleaner.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import yu.cleaner.R;
import yu.cleaner.util.Constant;
import yu.cleaner.util.MainApplication;
import yu.cleaner.util.StringPostRequest;
import yu.cleaner.util.TextUtil;
import yu.cleaner.util.UrlUtils;


public class LoginActivity extends Activity implements View.OnClickListener {
    View view;
    TextView register;
    TextView forget_pwd;
    TextView name;

    TextView pwd;
    Button login;
    SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
        sp = getSharedPreferences(Constant.SP_FILE_NAME,
                MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        //自动记住密码
        if (!(sp.getString("USER_ID", "").equals("") && !(sp.getString("USER_PWD", "")).equals(""))) {
            name.setText(sp.getString("USER_ID", ""));
            pwd.setText(sp.getString("USER_PWD", ""));
        }
    }

    public void initView() {
        name = (TextView) findViewById(R.id.login_user_name);
        register = (TextView) findViewById(R.id.register);
        forget_pwd = (TextView) findViewById(R.id.forget_pwd);
        pwd = (TextView) findViewById(R.id.login_user_pwd);
        login = (Button) findViewById(R.id.cleaner_login);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cleaner_login:
                //存在该用户则销毁当前页面,返回登陆信息
               login();
                break;
            case R.id.register:
                //存在该用户则销毁当前页面,返回登陆信息
//                View view = LayoutInflater.from(this).inflate(R.layout.yaoqingma, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setView(view).create();
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.forget_pwd:
                //存在该用户则销毁当前页面,返回登陆信息
                startActivity(new Intent(this, ChangePwdActivity.class));
                break;
        }
    }

    //需要判断用户是否存在，密码是否正确
//加载数据
    public void login() {
        if (TextUtil.isEmpty(name.getText().toString())) {
            Toast.makeText(this, "请填写用户名", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtil.isEmpty(pwd.getText().toString())) {
            Toast.makeText(this, "请填写密码", Toast.LENGTH_SHORT).show();
            return;
        } else {
            final String cleanerName = name.getText().toString();
            final String cleanerPwd = pwd.getText().toString();
            StringPostRequest postRequest = new StringPostRequest(UrlUtils.CLEANER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.equals("1")) {
                        //             Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                        //账号和密码存入本地
                        SharedPreferences.Editor e = sp.edit();
                        e.putString("USER_ID", cleanerName);
                        e.putString("USER_PWD", cleanerPwd);
                        e.commit();
                        //跳转到主页
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (s.equals("0")) {
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    } else if (s.equals("2")) {
                        Toast.makeText(LoginActivity.this, "不存在该用户", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(LoginActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "login");
            postRequest.putParams("cleanerid", cleanerName);
            postRequest.putParams("pwd", pwd.getText().toString());
            MainApplication.getApplication().getQueue().add(postRequest);
        }
    }
}
