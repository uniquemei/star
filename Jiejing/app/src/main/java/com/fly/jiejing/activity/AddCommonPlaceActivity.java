package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

//增加地址的Activity
public class AddCommonPlaceActivity extends Activity implements View.OnClickListener {
    private EditText add_select_street;
    private EditText add_select_detail;
    private TextView addAddress;
    private SharedPreferences sp;
    private ImageView addPlaceBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_common_place_layout);
        initView();
        addPlaceBack.setOnClickListener(this);
        addAddress.setOnClickListener(this);
    }

    public void initView() {
        add_select_street = (EditText) findViewById(R.id.add_select_street);
        add_select_detail = (EditText) findViewById(R.id.add_select_detail);
        addAddress = (TextView) findViewById(R.id.addAddress);
        addPlaceBack = (ImageView) findViewById(R.id.addPlaceBack);
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
    }

    //增加地址
    public void requestPlace() {
        if (TextUtil.isEmpty(add_select_street.getText().toString())) {
            Toast.makeText(AddCommonPlaceActivity.this, "请填写您在的地址", Toast.LENGTH_SHORT).show();

            return;
        } else if (TextUtil.isEmpty(add_select_detail.getText().toString())) {
            Toast.makeText(AddCommonPlaceActivity.this, "请填写详细地址", Toast.LENGTH_SHORT).show();

            return;
        }else {
            StringPostRequest postRequest = new StringPostRequest(UrlUtils.COMMONPLACE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (!s.equals("0")) {
                        Toast.makeText(AddCommonPlaceActivity.this, "增加地址成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddCommonPlaceActivity.this, "增加地址失败，请重新填写地址！", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(AddCommonPlaceActivity.this, "网络连接失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "add");
            postRequest.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
            postRequest.putParams("city", sp.getString("DEFAULE_CITY", "烟台"));
            postRequest.putParams("street", add_select_street.getText().toString());
            postRequest.putParams("detail", add_select_detail.getText().toString());
            MainApplication.getApplication().getQueue().add(postRequest);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addAddress:
                requestPlace();
                break;
            case R.id.addPlaceBack:
                finish();
                break;
        }
    }
}
