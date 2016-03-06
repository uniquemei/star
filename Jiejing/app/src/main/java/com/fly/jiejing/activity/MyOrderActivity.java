package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.db.DBHelper;
import com.fly.jiejing.adapter.MyOrderNotCompleteAdapter;
import com.fly.jiejing.entity.Order;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
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

//用户订单列表+跳转到获取订单详情
public class MyOrderActivity extends Activity implements View.OnClickListener, MyOrderNotCompleteAdapter.onSetDeleteOrder, PullToRefreshBase.OnRefreshListener<ListView> {
    private ImageView order_back;
    private PullToRefreshListView listView;
    private MyOrderNotCompleteAdapter adapter;
    private List<Order> mData;
    private Order order;
    private Intent intent;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    String phoneName;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_layout);
        initView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }, 500);
        order_back.setOnClickListener(this);
    }

    //初始化数据
    public void initView() {
        order_back = (ImageView) findViewById(R.id.order_back);
        listView = (PullToRefreshListView) findViewById(R.id.order_list);
        mData = new ArrayList<>();
        requestQueue = MainApplication.getApplication().getQueue();
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        phoneName = sp.getString("PHONE_NUMBER", null);
        adapter = new MyOrderNotCompleteAdapter(mData, this);
        listView.setAdapter(adapter);
        //删除监听
        adapter.setListenner(this);
        //刷新方向
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        //设置刷新监听
        listView.setOnRefreshListener(this);

//点击获取订单详情
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                order = (Order) adapter.getItem(position - 1);
                intent = new Intent(MyOrderActivity.this, MyOrderInfoActivity.class);
                intent.putExtra("myOrder", order);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.order_back:
                finish();
                break;
        }
    }

    //当删除订单或者更新状态的时候网络请求加载数据
    public void initData2() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Order> gData = gson.fromJson(s, new TypeToken<ArrayList<Order>>() {
                }.getType());
                mData.clear();
                if (gData != null && !gData.isEmpty()) {
                    mData.addAll(gData);
                    //存入本地数据库
                    Dao<Order, Integer> orderDao = DBHelper.getInstance(MyOrderActivity.this).getorderDao();
                    try {
                        //清空数据库
                        orderDao.queryRaw("delete from order");
                        orderDao.create(mData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //加载本地数据
                Dao<Order, Integer> orderDao = DBHelper.getInstance(MyOrderActivity.this).getorderDao();
                try {
                    if (orderDao.countOf() > 0) {
                        mData.addAll(orderDao.queryForAll());
                        adapter.notifyDataSetChanged();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MyOrderActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "order0");
        postRequest.putParams("userid", phoneName);
        requestQueue.add(postRequest);
    }

    //刷新数据
    public void initData() {
        listView.setRefreshing();
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Order> gData = gson.fromJson(s, new TypeToken<ArrayList<Order>>() {
                }.getType());
                mData.clear();
                if (gData != null && !gData.isEmpty()) {
                    mData.addAll(gData);
                    // 更新数据后停住刷新
                    //存入本地数据库
                    Dao<Order, Integer> orderDao = DBHelper.getInstance(MyOrderActivity.this).getorderDao();
                    try {
                        //清空数据库,删除不等于3的状态
                        orderDao.deleteBuilder().where().ne("state", "3").query();
                        //             orderDao.queryRaw("delete from order");
                        orderDao.create(mData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //加载本地数据
                mData.clear();
                Dao<Order, Integer> orderDao = DBHelper.getInstance(MyOrderActivity.this).getorderDao();
                try {
                    if (orderDao.countOf() > 0) {
                        mData.addAll(orderDao.queryForAll());
                        adapter.notifyDataSetChanged();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Toast.makeText(MyOrderActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete();

            }
        }

        );
        postRequest.putParams("action", "order0");
        postRequest.putParams("userid", phoneName);
        requestQueue.add(postRequest);
    }

    @Override
    public void deletOrder(Order order) {
        StringPostRequest post = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                initData2();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(MyOrderActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        post.putParams("action", "delete");
        post.putParams("userid", MainApplication.getApplication().getUser().getUser_id());
        post.putParams("id", order.getId() + "");
        requestQueue.add(post);
    }

    @Override
    public void update(Order order) {
        StringPostRequest post = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                initData2();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Toast.makeText(MyOrderActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        post.putParams("action", "update");
        post.putParams("state", "3");
        post.putParams("id", order.getId() + "");
        requestQueue.add(post);
    }

//    //下拉刷新
//
//    @Override
//    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
//
//    }
//
//    //下拉加载
//    @Override
//    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mData.clear();
//                initData();
//
//            }
//        }, 500);
//    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        initData();
    }
}
