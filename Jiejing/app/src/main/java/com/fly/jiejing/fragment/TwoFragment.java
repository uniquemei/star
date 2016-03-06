package com.fly.jiejing.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.CleanerDetailAct;
import com.fly.jiejing.adapter.RankCleanerAdapter;
import com.fly.jiejing.adapter.RankItemsAdapter;
import com.fly.jiejing.dao.CleanerDao;
import com.fly.jiejing.dao.ItemOrderDao;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/1.
 */
public class TwoFragment extends Fragment implements PullToRefreshBase.OnRefreshListener<ListView>, RankCleanerAdapter.onSaveListener {

    public User user;
    RadioButton radio1;
    RadioButton radio2;
    RadioGroup top;
    PullToRefreshListView myList;
    List<Cleaner> mData;
    List<Items> oData;
    View view;
    RankCleanerAdapter cleanerAdapter;
    RankItemsAdapter itemsAdapter;
    int check = 0;
    Boolean flag = true;//相同服务项目综合在一起
    List<Cleaner> commonCleaners;
    List<Cleaner> BlackCleaners;
    String city;
    SharedPreferences sp;
    TextView two_server_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData = new ArrayList<>();
        oData = new ArrayList<>();
        commonCleaners = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.two_layout, null);
        initView();
        return view;
    }

    public void initView() {
        sp = getActivity().getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_PRIVATE);
        radio1 = (RadioButton) view.findViewById(R.id.radio1);
        radio2 = (RadioButton) view.findViewById(R.id.radio2);
        top = (RadioGroup) view.findViewById(R.id.two_top);
        //      two_server_list = (TextView) view.findViewById(R.id.two_server_list);
        myList = (PullToRefreshListView) view.findViewById(R.id.twoList);
        user = MainApplication.getApplication().getUser();
        initEvent();
    }

    public void initEvent() {
        //刷新方向
        myList.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        myList.setOnRefreshListener(this);
        itemsAdapter = new RankItemsAdapter(getActivity(), oData);
        cleanerAdapter = new RankCleanerAdapter(getActivity(), mData);
        myList.setAdapter(cleanerAdapter);
        cleanerAdapter.setListener(this);
        //    initDate();//初始换数据
        oncheckRadio();
        if (null != user && user.getUser_id().length() > 0) {
            getUserCommon(user);   //得到用户的常用阿姨
        }

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (check == 0) {
                    Intent intent = new Intent(getActivity(), CleanerDetailAct.class);
                    intent.putExtra("cleaner", (Cleaner) cleanerAdapter.getItem(position - 1));
                    startActivity(intent);
                }
            }
        });
//        two_server_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    //监听事件
    public void oncheckRadio() {
        ((RadioButton) top.getChildAt(0)).setChecked(true);
        top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        if (checkedId == R.id.radio1) {
                            check = 0;
                            cleanerAdapter = new RankCleanerAdapter(getActivity(), mData);
                            myList.setAdapter(cleanerAdapter);
                            initCleaner();
                        } else if (checkedId == R.id.radio2) {
                            check = 1;
                            itemsAdapter = new RankItemsAdapter(getActivity(), oData);
                            myList.setAdapter(itemsAdapter);
                            initItems();
                        }
                    }
                }
            }
        });
    }

    //网络加载清洁工数据
    public void initCleaner() {
  //      myList.setRefreshing();
        city = sp.getString("DEFAULE_CITY", "烟台");
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.CLEANER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Cleaner> gData = gson.fromJson(s, new TypeToken<ArrayList<Cleaner>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    mData.clear();
                    for (Cleaner c : gData) {
                        c.setSave(0);
                        if (commonCleaners != null && commonCleaners.size() > 0) {
                            for (Cleaner common : commonCleaners) {
                                if (common.getCleanerid() == c.getCleanerid()) {
                                    if (common.getIsblack() == 1) {
                                        c.setSave(2);
                                    }
                                    if (common.getIscommon() == 1) {
                                        c.setSave(1);//已经收藏的阿姨
                                    }
                                }
                            }
                        }
                        mData.add(c);
                    }
                    cleanerAdapter.notifyDataSetChanged();
//                    updateLocalCleaner(mData);
                    CleanerDao dao = new CleanerDao(getActivity().getApplicationContext());
                    dao.addAllData(mData);
                }
     //           myList.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                CleanerDao cleanerDao = new CleanerDao(getActivity().getApplicationContext());
                mData.clear();
                if (null != cleanerDao.findAll() && cleanerDao.findAll().size() > 0) {
                    mData.clear();
                    mData.addAll(cleanerDao.findAll());
                }
     //           myList.onRefreshComplete();
            }
        });
        postRequest.putParams("action", "cleaner");
        postRequest.putParams("city", city);
        MainApplication.getApplication().getQueue().add(postRequest);
    }


    //请求用户的常用阿姨，并进行匹配,,请求与用户有关的阿姨
    public void getUserCommon(User u) {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.BLACKCLEANER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Cleaner> gData = gson.fromJson(s, new TypeToken<ArrayList<Cleaner>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    commonCleaners.addAll(gData);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", u.getUser_id());
        MainApplication.getApplication().getQueue().add(postRequest);
    }

    //    //请求用户的常用阿姨，并进行匹配
//    public void getUserBlack(User u) {
//        StringPostRequest postRequest = new StringPostRequest(UrlUtils.BLACKCLEANER_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Gson gson = new Gson();
//                List<Cleaner> gData = gson.fromJson(s, new TypeToken<ArrayList<Cleaner>>() {
//                }.getType());
//                if (gData != null && gData.size() > 0) {
//                    blackCleaners.addAll(gData);
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//        postRequest.putParams("action", "select");
//        postRequest.putParams("userid", u.getUser_id());
//        postRequest.putParams("isblack", "1");
//        MainApplication.getApplication().getQueue().add(postRequest);
//    }
    //网络加载服务项目数据
    public void initItems() {
        myList.setRefreshing();
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Items> gData = gson.fromJson(s, new TypeToken<ArrayList<Items>>() {
                }.getType());
                List<Items> datas = new ArrayList<>();
                if (gData != null && gData.size() > 0) {
                    for (int i = 0; i < gData.size(); i++) {
                        flag = true;
                        for (int j = 0; j < datas.size(); j++) {
                            if (gData.get(i).getName().equals(datas.get(j).getName())) {
                                flag = false;
                                datas.get(j).setNum(datas.get(j).getNum() + 1);
                                break;
                            }
                        }
                        if (flag) {
                            gData.get(i).setNum(1);
                            datas.add(gData.get(i));
                        }
                    }
                    ItemOrderDao orderDao = new ItemOrderDao(getActivity());
                    orderDao.addAllData(datas);

                    if (null != orderDao.findAll() && orderDao.findAll().size() > 0) {
                        oData.clear();
                        oData.addAll(orderDao.findAll());
                        itemsAdapter.notifyDataSetChanged();
                    }
                }
                myList.onRefreshComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                ItemOrderDao orderDao = new ItemOrderDao(getActivity());
                if (null != orderDao.findAll() && orderDao.findAll().size() > 0) {
                    oData.clear();
                    oData.addAll(orderDao.findAll());
                    itemsAdapter.notifyDataSetChanged();
                }
                myList.onRefreshComplete();
            }
        });
        postRequest.putParams("action", "order3");
        MainApplication.getApplication().getQueue().add(postRequest);
    }

// 缓存清洁人员排行
//    public void updateLocalCleaner(final List<Cleaner> cleaners) {
//        ExecutorManager.getInstance().execute(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
//    }

    private void refreshData() {

        if (check == 0) {
            initCleaner();
        } else if (check == 1) {
            initItems();
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        refreshData();
    }

    @Override
    public void onSave(final Cleaner p) {
        //收藏存入数据库
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示").setMessage("确定收藏清洁人员\n编号：" + p.getCleanerid());
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringPostRequest request = new StringPostRequest(UrlUtils.BLACKCLEANER_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //改变收藏状态为1
                        CleanerDao dao = new CleanerDao(getActivity().getApplicationContext());
                        p.setSave(1);
                        dao.updateSave(p);
                        cleanerAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
                });
                request.putParams("action", "insertCommom");
                request.putParams("cleanerid", p.getCleanerid() + "");
                request.putParams("userid", p.getUserid());
                MainApplication.getApplication().getQueue().add(request);

                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }
}
