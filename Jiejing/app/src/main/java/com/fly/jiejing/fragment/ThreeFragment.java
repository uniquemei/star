package com.fly.jiejing.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.ThreeShowLifeAct;
import com.fly.jiejing.activity.db.DBHelper;
import com.fly.jiejing.adapter.LifeAdapter;
import com.fly.jiejing.entity.Life;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.fly.jiejing.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2015/10/1.
 */
public class ThreeFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView> {
    private RadioGroup radioGroup;
    private LifeAdapter adapter;
    private View v;
    private List<Life> mData;
    PullToRefreshListView three_listView;
    int num = 0;
    TextView Erweima;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.three_life_layout, null);
        initView();
        oncheckRadio();
        return v;
    }

    //数据的初始化
    public void initView() {
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        three_listView = (PullToRefreshListView) v.findViewById(R.id.three_listView);
        three_listView.setMode(PullToRefreshBase.Mode.BOTH);
        three_listView.setOnRefreshListener(this);
        mData = new ArrayList<>();
        Erweima= (TextView) v.findViewById(R.id.Erweima);
        Erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent1, 0);
            }
        });
        adapter = new LifeAdapter(mData, getActivity());
        three_listView.setAdapter(adapter);
        three_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Life data = (Life) adapter.getItem(position - 1);
                Intent intent = new Intent(getActivity(), ThreeShowLifeAct.class);
                intent.putExtra("life", data);
                startActivity(intent);
            }
        });
    }


    //监听事件
    public void oncheckRadio() {
        addDataByType(1);
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        num = i + 1;
                        addDataByType(i + 1);
                    }
                }
            }
        });
    }

    //加载数据
    public void addDataByType(final int typeid) {
        three_listView.setRefreshing();
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.SHOW_ITEMS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Life> gData = gson.fromJson(s, new TypeToken<ArrayList<Life>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    //存入本地数据库
                    Dao<Life, Integer> lifeDao = DBHelper.getInstance(getActivity()).getLifeDao();
                    try {
                        lifeDao.create(gData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    mData.clear();
                    for (int i = 0; i < gData.size(); i++) {
                        if (gData.get(i).getTypeid() == typeid && i < 5) {
                            mData.add(gData.get(i));
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                three_listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //orderBy:true表示升序； limit表示每次查的数量;offset表示从第几行开始查;条件：where().and().eq("","")
                Dao<Life, Integer> lifeDao = DBHelper.getInstance(getActivity()).getLifeDao();
                mData.clear();
                try {
                    if (lifeDao.countOf() > 0){
                        mData.addAll(lifeDao.queryBuilder().limit(5).orderBy("id", false).where().eq("typeid", typeid).query());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                three_listView.onRefreshComplete();
                Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "life");
        postRequest.putParams("typeid", typeid + "");
        MainApplication.getApplication().getQueue().add(postRequest);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

        addDataByType(num);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        addDataByTypeMore(num);

    }

    public void addDataByTypeMore(final int typeid) {
        three_listView.setRefreshing();
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.SHOW_ITEMS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Life> gData = gson.fromJson(s, new TypeToken<ArrayList<Life>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    //存入本地数据库
                    Dao<Life, Integer> lifeDao = DBHelper.getInstance(getActivity()).getLifeDao();
                    try {
                        lifeDao.create(gData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    mData.clear();
                    for (Life l : gData) {
                        if (l.getTypeid() == typeid) {
                            mData.add(l);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                three_listView.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //orderBy:true表示升序； limit表示每次查的数量;offset表示从第几行开始查;条件：where().and().eq("","")
                Dao<Life, Integer> lifeDao = DBHelper.getInstance(getActivity()).getLifeDao();
                mData.clear();
                try {
                    mData.addAll(lifeDao.queryBuilder().orderBy("id", false).where().and().eq("typeid", typeid).query());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                three_listView.onRefreshComplete();
                Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "life");
        postRequest.putParams("typeid", typeid + "");
        MainApplication.getApplication().getQueue().add(postRequest);
    }
}