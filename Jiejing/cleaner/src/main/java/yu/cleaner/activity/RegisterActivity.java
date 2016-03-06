package yu.cleaner.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import yu.cleaner.R;
import yu.cleaner.util.Constant;
import yu.cleaner.util.MainApplication;
import yu.cleaner.util.MyTools;
import yu.cleaner.util.StringPostRequest;
import yu.cleaner.util.UrlUtils;

/**
 * Created by lewei on 2015/12/8.
 */
public class RegisterActivity extends Activity {
    private EditText username_edit, password_edit, passwordVerify_edit;
    private TextView register_submit;
    private EditText register_edit_name, register_code;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }


    private void initView() {
        back = (ImageView) findViewById(R.id.back_register);

        username_edit = (EditText) findViewById(R.id.register_edit_username);
        register_code = (EditText) findViewById(R.id.register_edit_code);
        password_edit = (EditText) findViewById(R.id.register_edit_psw);
        passwordVerify_edit = (EditText) findViewById(R.id.register_edit_verify_psw);
        register_submit = (TextView) findViewById(R.id.register_submit);
        register_edit_name = (EditText) findViewById(R.id.register_edit_name);
        register_submit.setOnClickListener(register_click);
        back.setOnClickListener(register_click);
    }


    private String username, psw1, psw2, code;
    private boolean isFull = false;
    String name;


    private void infoIsEmpty() {
        username = username_edit.getText().toString();
        psw1 = password_edit.getText().toString();
        psw2 = passwordVerify_edit.getText().toString();
        name = register_edit_name.getText().toString();
        code = register_code.getText().toString();

        if (MyTools.stringIsEmpty(username) ||
                MyTools.stringIsEmpty(psw1) ||
                MyTools.stringIsEmpty(psw2) ||
                MyTools.stringIsEmpty(name) ||
                MyTools.stringIsEmpty(code)) {
            isFull = false;
            MyTools.showShortToast(getApplicationContext(), "请输入完整的信息");
        } else {
            isFull = true;
            if (!MyTools.isMobile(username)) {
                MyTools.showShortToast(getApplicationContext(), "请输入正确的手机号码");
                isFull = false;
            } else if (!psw1.equals(psw2)) {
                MyTools.showShortToast(getApplicationContext(), "两次输入的密码不一致");
                isFull = false;
            }
        }
    }


    private View.OnClickListener register_click = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.register_submit:
                    infoIsEmpty();
                    if (isFull) {
                        registerPass();
                    }

                    break;
                case R.id.back_register:
                    finish();
                    break;
                default:
                    return;
            }
        }
    };

    public void registerPass() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.CLEANER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (s.equals("1")) {
                    Toast.makeText(RegisterActivity.this, "注册成功,请登录", Toast.LENGTH_SHORT).show();
                    //账号和密码存入本地
                    SharedPreferences sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("USER_ID", "");
                    e.putString("USER_PWD", "");
                    e.commit();
                    //跳转到主页
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else if (s.equals("300")) {
                    Toast.makeText(RegisterActivity.this, "操作码不对", Toast.LENGTH_SHORT).show();
                } else if (s.equals("500")) {
                    Toast.makeText(RegisterActivity.this, "该用户已存在", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                MyTools.showShortToast(RegisterActivity.this, "连接失败");
            }
        });
        postRequest.putParams("cleanerid", username);
        postRequest.putParams("pwd", psw1);
        postRequest.putParams("name", name);
        MainApplication.getApplication().getQueue().add(postRequest);
    }

}