package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.db.DBHelper;
import com.fly.jiejing.adapter.BlackCleanerAdapter;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//常用阿姨的主activity
public class MyCommonUserActivity extends Activity implements View.OnClickListener, BlackCleanerAdapter.onPeopleSelectedChangeListener {
    private ListView listView;
    private List<Cleaner> mData;
    private List<Cleaner> gData;
    private List<Cleaner> selecData;
    private BlackCleanerAdapter adapter;
    private ImageView common_user_back;
    private TextView blackUser;
    private Intent intent;
    private RequestQueue requestQueue;
    private Button commanUser_delete;
    private Button commanUser_addblack;
    private SharedPreferences sp;
    private String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_common_user);
        requestQueue = MainApplication.getApplication().getQueue();
        initView();
    }

    //初始化数据
    public void initView() {
        common_user_back = (ImageView) findViewById(R.id.common_user_back);
        listView = (ListView) findViewById(R.id.commonUser_list);
        blackUser = (TextView) findViewById(R.id.blackUser);
        commanUser_addblack = (Button) findViewById(R.id.commanUser_addblack);
        commanUser_delete = (Button) findViewById(R.id.commanUser_delete);
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        mData = new ArrayList<>();
        selecData = new ArrayList<>();
        userPhone = sp.getString("PHONE_NUMBER", null);

        initData();
        adapter = new BlackCleanerAdapter(this, mData);
        adapter.setListener(this);
        listView.setAdapter(adapter);

        common_user_back.setOnClickListener(this);
        blackUser.setOnClickListener(this);
        commanUser_delete.setOnClickListener(this);
        commanUser_addblack.setOnClickListener(this);
    }

    //网络加载数据
    public void initData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.BLACKCLEANER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                gData = gson.fromJson(s, new TypeToken<ArrayList<Cleaner>>() {
                }.getType());
                mData.clear();
                if (gData != null && gData.size() > 0) {
                    mData.addAll(gData);
                    //存入本地数据库
                    Dao<Cleaner, Integer> blackCleanerDao = DBHelper.getInstance(MyCommonUserActivity.this).getBlackCleanerDao();
                    try {
                        blackCleanerDao.queryRaw("delete from cleaner");
                        blackCleanerDao.create(mData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                mData.clear();
                Toast.makeText(MyCommonUserActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                //请求本地数据库的数据
                Dao<Cleaner, Integer> blackCleanerDao = DBHelper.getInstance(MyCommonUserActivity.this).getBlackCleanerDao();
                try {
                    if (blackCleanerDao.countOf() != 0) {
                        mData.addAll(blackCleanerDao.queryForAll());
                    }
                    if (mData != null) {
                        adapter.notifyDataSetChanged();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", sp.getString("PHONE_NUMBER", null));
        postRequest.putParams("iscommon", "1");
        requestQueue.add(postRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_user_back:
                finish();
                break;
            case R.id.blackUser:
                intent = new Intent(MyCommonUserActivity.this, MyBlackPeopleActivity.class);
                startActivity(intent);
                break;
            case R.id.commanUser_delete:
                removeCommon();
                break;
            case R.id.commanUser_addblack:
                addBlack();
                break;
        }
    }

    @Override
    public void onPeopleSelectedChange(Cleaner p) {
        if (p.isSelect()) {
            selecData.add(p);
        } else {
            selecData.remove(p);
        }
    }

    //移除常用阿姨
    public void removeCommon() {
//        mData.removeAll(selecData);
//        adapter.notifyDataSetChanged();
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
                    Toast.makeText(MyCommonUserActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "deleBlack");
            postRequest.putParams("userid", sp.getString("PHONE_NUMBER", null));
            postRequest.putParams("cleanerid", "" + cleareId);
            requestQueue.add(postRequest);
        }

    }

    //添加黑名单
    public void addBlack() {
        //加载本地数据库
//        Dao<Cleaner, Integer> blackCleanerDao =
//                DBHelper.getInstance(this).getBlackCleanerDao();
//        try {
//            blackCleanerDao.create(selecData);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
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
                    Toast.makeText(MyCommonUserActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                }
            });
            postRequest.putParams("action", "updateBlack");
            postRequest.putParams("userid", sp.getString("PHONE_NUMBER", null));
            postRequest.putParams("cleanerid", "" + cleareId);
            requestQueue.add(postRequest);
        }

    }
}
