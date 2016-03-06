package com.fly.jiejing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.adapter.AddressAdapter;
import com.fly.jiejing.entity.Address;
import com.fly.jiejing.pickerlib.util.TextUtil;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.smssdk.gui.layout.ListviewTitleLayout;


public class MainOrderPlaceAct extends Activity {
    private List<Address> mData;
    private String address;
    private String city;
    private String userId;
    private ListView addressContanier;
    private SharedPreferences sp;
    private TextView cofirmAddress;
    private AddressAdapter adapter;
    EditText selectStreet;
    EditText selectDetail;
    Intent intent;
    ImageView backPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_order_place_layout);
        initView();
        city = getIntent().getStringExtra("city");
        userId = sp.getString("PHONE_NUMBER", null);

        initData();

        addressContanier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address a = (Address) adapter.getItem(position);
                intent.putExtra("address", a.getStreet() + a.getDetail());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        yesAddress();
        backPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    void initView() {
        mData = new ArrayList<>();
        intent = new Intent();
        selectStreet = (EditText) findViewById(R.id.select_street);
        selectDetail = (EditText) findViewById(R.id.select_detail);
        addressContanier = (ListView) findViewById(R.id.addressContanier);
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        cofirmAddress = (TextView) findViewById(R.id.confirmAddress);
        adapter = new AddressAdapter(mData, this);
        addressContanier.setAdapter(adapter);
        backPlace= (ImageView) findViewById(R.id.backPlace);

    }

    public void initData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.COMMONPLACE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson g = new Gson();
                List<Address> gData = g.fromJson(s, new TypeToken<ArrayList<Address>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    mData.clear();
                    mData.addAll(gData);
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainOrderPlaceAct.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", userId);
        MainApplication.getApplication().getQueue().add(postRequest);
    }


    //确认返回
    private void yesAddress() {
        cofirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtil.isEmpty(selectStreet.getText().toString()) && !TextUtil.isEmpty(selectDetail.getText().toString())) {
                    intent.putExtra("address", selectStreet.getText().toString() + selectDetail.getText().toString());
                    setResult(RESULT_OK, intent);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainOrderPlaceAct.this);
                    builder.setTitle("提示信息").setMessage("将地址设为常用地址吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPlace();
                            dialog.dismiss();
                            finish();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                } else {
                    if (selectStreet.getText().toString().equals("")) {
                        selectStreet.setFocusable(true);
                    } else {
                        selectDetail.setFocusable(true);
                    }
                    Toast.makeText(MainOrderPlaceAct.this, "您填写的信息不完整", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //增加地址
    public void requestPlace() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.COMMONPLACE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if (!s.equals("0")) {
                    Toast.makeText(MainOrderPlaceAct.this, "增加地址成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainOrderPlaceAct.this, "增加地址失败，请重新填写地址！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainOrderPlaceAct.this, "网络连接失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "add");
        postRequest.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
        postRequest.putParams("city", sp.getString("DEFAULE_CITY", "烟台"));
        postRequest.putParams("street", selectStreet.getText().toString());
        postRequest.putParams("detail", selectDetail.getText().toString());
        MainApplication.getApplication().getQueue().add(postRequest);
    }
}
