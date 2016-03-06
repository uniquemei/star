package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.db.DBHelper;
import com.fly.jiejing.adapter.CommonPlaceAdapter;
import com.fly.jiejing.entity.Address;
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

public class MyCommonPlaceActivity extends Activity implements View.OnClickListener, CommonPlaceAdapter.ondeleteChangeListenner {
    private ListView commanplacelist;
    private ImageView comman_user_back;
    private CommonPlaceAdapter adapter;
    private List<Address> mData;
    private List<Address> gData;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    private String userId;
    private ImageView add_user_place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_common_place);
        //   ButterKnife.bind(this);
        requestQueue = MainApplication.getApplication().getQueue();
        initView();
        initData();
        comman_user_back.setOnClickListener(this);
        add_user_place.setOnClickListener(this);
        adapter.setOndeleteListenner(this);
    }

    //数据的初始化
    public void initView() {
        commanplacelist = (ListView) findViewById(R.id.commanplacelist);
        comman_user_back = (ImageView) findViewById(R.id.comman_user_back);
        add_user_place = (ImageView) findViewById(R.id.add_user_place);
        mData = new ArrayList<>();
        gData = new ArrayList<>();
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        userId = sp.getString("PHONE_NUMBER", null);
        adapter = new CommonPlaceAdapter(mData, this);
        commanplacelist.setAdapter(adapter);
    }

    //加载数据
    public void initData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.COMMONPLACE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson g = new Gson();
                gData = g.fromJson(s, new TypeToken<ArrayList<Address>>() {
                }.getType());
                if (gData != null&&gData.size()>0) {
                    mData.clear();
                    mData.addAll(gData);
                    adapter.notifyDataSetChanged();

                    //存入本地数据库
                    Dao<Address, Integer> addresseDao = DBHelper.getInstance(MyCommonPlaceActivity.this).getaddresseDao();

                    try {
                 //       addresseDao.deleteBuilder().where().eq("user_name",MainApplication.getApplication().getUser().getUser_id()).query();
                        //创建数据
                        addresseDao.queryRaw("delete from address");
                        addresseDao.create(gData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyCommonPlaceActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                //请求本地数据库
                mData.clear();
                Dao<Address, Integer> addresseDao = DBHelper.getInstance(MyCommonPlaceActivity.this).getaddresseDao();
                try {
                    if (addresseDao.countOf() > 0) {
                        mData.addAll(addresseDao.queryForAll());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if (mData != null && mData.size() > 0) {
                    adapter.notifyDataSetChanged();
                }
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", userId);
        requestQueue.add(postRequest);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comman_user_back:
                finish();
                break;
            case R.id.add_user_place:
                Intent intent = new Intent(MyCommonPlaceActivity.this, AddCommonPlaceActivity.class);
                startActivity(intent);
                break;
        }
    }

    //删除数据
    @Override
    public void deleteChangeListenner(Address a) {
        StringPostRequest post = new StringPostRequest(UrlUtils.COMMONPLACE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                initData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MyCommonPlaceActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        post.putParams("action", "delete");
        post.putParams("userid", userId);
        post.putParams("addressid", a.getAddress_id());
        requestQueue.add(post);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }
}
