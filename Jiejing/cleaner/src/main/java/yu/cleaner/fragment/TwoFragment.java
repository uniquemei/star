package yu.cleaner.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import yu.cleaner.R;
import yu.cleaner.activity.MainOrderInfoActivity;
import yu.cleaner.adapter.OrderListAdapter;
import yu.cleaner.entity.Order;
import yu.cleaner.util.Constant;
import yu.cleaner.util.MainApplication;
import yu.cleaner.util.MyTools;
import yu.cleaner.util.StringPostRequest;
import yu.cleaner.util.UrlUtils;

/**
 * Created by lewei on 2015/12/7.
 */
public class TwoFragment extends Fragment implements OrderListAdapter.onSelected, PullToRefreshBase.OnRefreshListener {

    PullToRefreshListView listView;

    OrderListAdapter adapter;
    List<Order> mData;
    String cleanerId;
    TextView exit;
    private SharedPreferences sp;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, null);

        initView();
        return view;
    }

    private void initView() {
        sp = getActivity().getSharedPreferences(Constant.SP_FILE_NAME,
                getActivity().MODE_PRIVATE);
        cleanerId = sp.getString("USER_NAME", "");

        //自动记住密码

        exit = (TextView) view.findViewById(R.id.cleaner_exit);
        //listView的监听事件
        mData = new ArrayList<>();

        listView = (PullToRefreshListView) view.findViewById(R.id.order_list);
        adapter = new OrderListAdapter(mData, getActivity(), cleanerId);
        listView.setAdapter(adapter);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.setOnRefreshListener(this);
        adapter.setListener(this);

        initEvent();
    }

    Order order;

    private void initEvent() {
        //退出自动登陆
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                order = (Order) adapter.getItem(position - 1);
                Log.e("---", "order");
                Intent intent = new Intent(getActivity(), MainOrderInfoActivity.class);
                order.setLogin_id(Integer.valueOf(cleanerId));
                intent.putExtra("myOrder", order);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void initData() {
        listView.setRefreshing();
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Order> gData = gson.fromJson(s, new TypeToken<ArrayList<Order>>() {
                }.getType());
                mData.clear();
                if (gData != null && gData.size() > 0) {
                    mData.addAll(gData);
                    //存入本地数据库
                }
                adapter.notifyDataSetChanged();
                listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "连接失败", Toast.LENGTH_SHORT).show();
                listView.onRefreshComplete();
            }

        });

        postRequest.putParams("action", "cleanerorder");
        if (MyTools.isMobile(cleanerId)) {
            postRequest.putParams("phone", cleanerId + "");
        } else {
            postRequest.putParams("cleanerid", cleanerId + "");
        }
        postRequest.putParams("state", "1");
        MainApplication.getApplication().getQueue().add(postRequest);

    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {
        initData();
    }

    //更新状态
    @Override
    public void onGetOrder(Order o) {
    }

    @Override
    public void onContactUser(Order o) {

        final String tel = o.getUser_name();
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("联系").setMessage("电话：" + tel);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri telUri = Uri.parse("tel:" + tel);
                startActivity(new Intent(Intent.ACTION_DIAL, telUri));
                dialog.dismiss();
            }
        }).show();
    }
}