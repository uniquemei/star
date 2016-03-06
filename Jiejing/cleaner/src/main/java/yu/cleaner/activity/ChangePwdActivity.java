package yu.cleaner.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import yu.cleaner.R;
import yu.cleaner.util.Constant;
import yu.cleaner.util.MainApplication;
import yu.cleaner.util.MyTools;
import yu.cleaner.util.StringPostRequest;
import yu.cleaner.util.UrlUtils;

//修改密码的Activity
public class ChangePwdActivity extends Activity implements View.OnClickListener {
    //星星佳洁

    private Button change_yes;
    private EditText change_phone;//输入的手机号
    private EditText chage_code;//操作码
    private EditText change_pass;//修改后的密码
    private EditText change_pass2;//修改后的密码

    String phoneNumber;
    private SharedPreferences sp;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        initView();
    }

    //数据的初始化
    public void initView() {
        back = (ImageView) findViewById(R.id.back_changePwd);

        //  change_btn_send = (Button) findViewById(R.id.change_btn_send);
        change_yes = (Button) findViewById(R.id.change_yes);
        change_phone = (EditText) findViewById(R.id.change_etPhone_Register);
        chage_code = (EditText) findViewById(R.id.chaneg_etEnsure_Register);
        change_pass = (EditText) findViewById(R.id.change_etPwd_Register);
        change_pass2 = (EditText) findViewById(R.id.change_etPwd_Register2);
        back.setOnClickListener(this);
        change_yes.setOnClickListener(this);

    }


    Boolean isFull = false;
    String phone, code, pwd1, pwd2;

    private void infoIsEmpty() {
        phone = change_phone.getText().toString().trim();
        code = chage_code.getText().toString().trim();
        pwd1 = change_pass.getText().toString().trim();
        pwd2 = change_pass2.getText().toString().trim();
        if (MyTools.stringIsEmpty(phone) ||
                MyTools.stringIsEmpty(code) ||
                MyTools.stringIsEmpty(pwd1) ||
                MyTools.stringIsEmpty(pwd2)) {
            isFull = false;
            MyTools.showShortToast(getApplicationContext(), "请输入完整的信息");
        } else {
            isFull = true;
            if (!MyTools.isMobile(phone)) {
                MyTools.showShortToast(getApplicationContext(), "请输入正确的手机号码");
                isFull = false;
            } else if (!pwd1.equals(pwd2)) {
                MyTools.showShortToast(getApplicationContext(), "两次输入的密码不一致");
                isFull = false;
            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.change_yes:
                initPass();
                break;
            case R.id.back_changePwd:
                finish();
                break;
        }
    }

    //网络修改密码
    public void initPass() {
        infoIsEmpty();
        if (isFull) {
            StringPostRequest postRequest = new StringPostRequest(UrlUtils.CLEANER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.equals("1")) {
                        Toast.makeText(ChangePwdActivity.this, "注册成功,请重新登录", Toast.LENGTH_SHORT).show();
                        //传递数据
                        sp = ChangePwdActivity.this.getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_APPEND);
                        SharedPreferences.Editor e = sp.edit();
             //           e.putString("PHONE_NUMBER", phoneNumber);
                        e.putString("USER_ID", "");
                        e.putString("USER_PWD", "");
                        e.commit();
                        startActivity(new Intent(ChangePwdActivity.this, LoginActivity.class));
                        finish();
                    } else if (s.equals("300")) {
                        Toast.makeText(ChangePwdActivity.this, "操作码不对", Toast.LENGTH_SHORT).show();
                    }else if (s.equals("500")) {
                        Toast.makeText(ChangePwdActivity.this, "该用户不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePwdActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(ChangePwdActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "modify");
            postRequest.putParams("phone", phone);
            postRequest.putParams("pwd", pwd1);
            postRequest.putParams("code", code);
            MainApplication.getApplication().getQueue().add(postRequest);
//        }
        }
    }
}
