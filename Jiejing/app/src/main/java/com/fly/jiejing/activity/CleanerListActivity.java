package com.fly.jiejing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.adapter.CleanerAdapter;
import com.fly.jiejing.dao.CleanerDao;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CleanerListActivity extends Activity implements CleanerAdapter.onSaveListener {
    List<Cleaner> mData;
    List<Cleaner> commonCleaners;
    ListView cleanerList;
    TextView cleanerType;
    Items item;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_list);
        item = (Items) getIntent().getSerializableExtra("item");
        initView();
    }

    CleanerAdapter adapter;
    SharedPreferences sp;
    public User user;

    public void initView() {
        sp = getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_PRIVATE);
        mData = new ArrayList<>();
        commonCleaners = new ArrayList<>();
        cleanerList = (ListView) findViewById(R.id.cleanerList);
        cleanerType = (TextView) findViewById(R.id.cleanerType);
        back = (ImageView) findViewById(R.id.back_cleanerList);

        adapter = new CleanerAdapter(this, mData);
        cleanerList.setAdapter(adapter);
        cleanerType.setText(getPid(item.getPid()) + "人员列表");

        user = MainApplication.getApplication().getUser();
        if (null != user && user.getUser_id().length() > 0) {
            getUserCommon(user);   //得到用户的常用阿姨
        }
        adapter.setListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cleanerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CleanerListActivity.this, CleanerDetailAct.class);
                intent.putExtra("cleaner", (Cleaner) adapter.getItem(position - 1));
                startActivity(intent);
            }

        });

    }


    public String getPid(int pid) {
        switch (pid) {
            case 1:
                return "家庭清洁";

            case 2:
                return "家电清洗";

            case 3:
                return "家居养护";

            case 4:
                return "洗护服务";

            default:
                return "水电服务";
        }

    }

    //网络加载清洁工数据
    public void initCleaner() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.CLEANER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Cleaner> gData = gson.fromJson(s, new TypeToken<ArrayList<Cleaner>>() {
                }.getType());
                if (gData != null && gData.size() > 0) {
                    Log.e("---", s);
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
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        postRequest.putParams("action", "cleaner");
        postRequest.putParams("city", item.getCityName());
        postRequest.putParams("type", String.valueOf(item.getPid()));
        MainApplication.getApplication().getQueue().add(postRequest);
        Log.e("---", "CLEANER_URL");
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
                    initCleaner();
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


    @Override
    public void onSave(final Cleaner p) {
        //收藏存入数据库
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        CleanerDao dao = new CleanerDao(getApplicationContext());
                        p.setSave(1);
                        dao.updateSave(p);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "收藏成功", Toast.LENGTH_SHORT).show();
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
