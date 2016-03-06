package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.adapter.BlackCleanerAdapter;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单
 */

public class MyBlackPeopleActivity extends Activity implements View.OnClickListener, BlackCleanerAdapter.onPeopleSelectedChangeListener {
    private ListView listView;
    private List<Cleaner> mData;
    private List<Cleaner> selecData;
    private BlackCleanerAdapter adapter;
    private ImageView black_user_back;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    private Button claer_black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_black_people);
        requestQueue = MainApplication.getApplication().getQueue();
        initView();
    }

    //初始化数据
    public void initView() {
        claer_black = (Button) findViewById(R.id.claer_black);
        black_user_back = (ImageView) findViewById(R.id.black_user_back);
        listView = (ListView) findViewById(R.id.black_list);
        mData = new ArrayList<>();
        selecData = new ArrayList<>();
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        initData();
        adapter = new BlackCleanerAdapter(this, mData);
        adapter.setListener(this);
        listView.setAdapter(adapter);
        black_user_back.setOnClickListener(this);
        claer_black.setOnClickListener(this);
    }

    //网络加载黑名单数据
    public void initData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.BLACKCLEANER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Cleaner> gData = gson.fromJson(s, new TypeToken<ArrayList<Cleaner>>() {
                }.getType());
                mData.clear();
                if (gData != null && gData.size() > 0) {
                    mData.addAll(gData);
                }else {
                    claer_black.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyBlackPeopleActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", sp.getString("PHONE_NUMBER", null));
        postRequest.putParams("isblack", "1");
        requestQueue.add(postRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.black_user_back:
                finish();
                break;
            case R.id.claer_black:
                cleareBlack();
                break;
        }
    }

    //实时监听选中的黑名单名单
    @Override
    public void onPeopleSelectedChange(Cleaner p) {
        if (p.isSelect()) {
            selecData.add(p);
        } else {
            selecData.remove(p);
        }
    }

    //移除黑名单
    public void cleareBlack() {
        for (int i = 0; i < selecData.size(); i++) {
            int cleareId = selecData.get(i).getCleanerid();
            StringPostRequest postRequest = new StringPostRequest(UrlUtils.BLACKCLEANER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    initData();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MyBlackPeopleActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "deleBlack");
            postRequest.putParams("userid", sp.getString("PHONE_NUMBER", null));
            postRequest.putParams("cleanerid", "" + cleareId);
            requestQueue.add(postRequest);
        }
    }
}
