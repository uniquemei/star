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
import com.fly.jiejing.adapter.MyOrderCompleteAdapter;
import com.fly.jiejing.entity.Items;
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

//订单列表
public class MyOrderCompleteAct extends Activity implements View.OnClickListener, MyOrderCompleteAdapter.onSetDeleteOrder, PullToRefreshBase.OnRefreshListener2<ListView> {
    private ImageView order_back;
    private PullToRefreshListView listView;
    private MyOrderCompleteAdapter adapter;
    private List<Order> mData;
    private Order order;
    private Intent intent;
    private RequestQueue requestQueue;
    private SharedPreferences sp;
    String phoneName;
    private Handler handler = new Handler();
    int num = 6;
    Items items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_complete);
        initView();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initData(num);
            }
        }, 500);
        order_back.setOnClickListener(this);

    }

    //初始化数据
    public void initView() {
        order_back = (ImageView) findViewById(R.id.complet_order_back);
        listView = (PullToRefreshListView) findViewById(R.id.complet_order_list);

        mData = new ArrayList<>();
        requestQueue = MainApplication.getApplication().getQueue();
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        phoneName = sp.getString("PHONE_NUMBER", null);
        adapter = new MyOrderCompleteAdapter(mData, this);
        //删除监听
        adapter.setListenner(this);
        //刷新方向
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        //设置刷新监听
        listView.setOnRefreshListener(this);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                order = (Order) adapter.getItem(position - 1);
                intent = new Intent(MyOrderCompleteAct.this, MyOrderInfoActivity.class);
                intent.putExtra("myOrder", order);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complet_order_back:
                finish();
                break;
        }
    }

    //网络请求加载数据
    public void initData(int num) {
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
                    Dao<Order, Integer> orderDao = DBHelper.getInstance(MyOrderCompleteAct.this).getorderDao();
                    try {
                        //清空数据库
                        orderDao.deleteBuilder().where().eq("state", gData.get(0).getState()).query();

                        orderDao.create(mData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } // 更新数据后停住刷新
                adapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //请求本地数据库
                mData.clear();
                Dao<Order, Integer> orderDao = DBHelper.getInstance(MyOrderCompleteAct.this).getorderDao();
                try {
                    if (orderDao.countOf() > 0) {
                        mData.addAll(orderDao.queryBuilder().where().eq("state", "3").query());
                        adapter.notifyDataSetChanged();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                listView.onRefreshComplete();
                Toast.makeText(MyOrderCompleteAct.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "order3");
        postRequest.putParams("userid", phoneName);
        postRequest.putParams("num", num + "");
        requestQueue.add(postRequest);
    }

    //此时是下订单
    @Override
    public void deletOrder(Order orders) {
        changeOrderToItems(orders);
        Intent intent = new Intent(MyOrderCompleteAct.this, MainOrderAct.class);
        intent.putExtra("item", items);
        startActivity(intent);
    }

    //评论订单
    @Override
    public void commentOrder(Order orders) {
        changeOrderToItems(orders);
        Intent intent = new Intent(MyOrderCompleteAct.this, MyOrderCommentAct.class);
        intent.putExtra("item", items);
        startActivity(intent);
    }

    //从order中获取信息为item
    private void changeOrderToItems(Order orders) {
        items = new Items();
        items.setName(orders.getName());
        items.setCityName(orders.getCity());
        items.setPrice(orders.getPrice());
        items.setId(orders.getId());
        items.setType(orders.getItemtype());
        items.setPid(orders.getPid());
    }
    //下拉刷新

    @Override
    public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
        num = 6;
        initData(num);
    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(final PullToRefreshBase<ListView> refreshView) {
        num++;
        initData(num);
    }
}
