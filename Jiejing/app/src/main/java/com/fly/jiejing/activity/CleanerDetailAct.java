package com.fly.jiejing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.db.DBHelper;
import com.fly.jiejing.adapter.CleanerCommentAdapter;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.Order;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CleanerDetailAct extends Activity implements PullToRefreshBase.OnRefreshListener2<ListView> {

    Cleaner cleaner;
    PullToRefreshListView myList;
    CleanerCommentAdapter adapter;
    private List<Order> mData;
    int num = 6;
    private ImageView cleanerDetail_back;
    private  TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleaner_detail);
        initView();
        cleanerDetail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData(num);
    }

    private void initView() {
        mData = new ArrayList<>();
        text= (TextView) findViewById(R.id.cleaner_comment_list_null);
        cleanerDetail_back = (ImageView) findViewById(R.id.cleanerDetail_back);
        cleaner = (Cleaner) getIntent().getSerializableExtra("cleaner");
        myList = (PullToRefreshListView) findViewById(R.id.cleaner_comment_list);
        myList.setMode(PullToRefreshBase.Mode.BOTH);
        myList.setOnRefreshListener(this);
        adapter = new CleanerCommentAdapter(this, mData);
        myList.setAdapter(adapter);

    }

    private void initData(int n) {
        myList.isRefreshing();
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Order> gData = gson.fromJson(s, new TypeToken<ArrayList<Order>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    mData.clear();
                    mData.addAll(gData);
                    text.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }else {
                    text.setVisibility(View.VISIBLE);
                }
                myList.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(CleanerDetailAct.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                myList.onRefreshComplete();
            }
        });
        postRequest.putParams("action", "cleanercomment");
        postRequest.putParams("cleanerid", cleaner.getCleanerid() + "");
        postRequest.putParams("num", n + "");
        MainApplication.getApplication().getQueue().add(postRequest);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        num = 6;
        initData(num);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        num++;
        initData(num);
    }
}
