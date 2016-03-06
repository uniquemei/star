package com.fly.jiejing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;

import com.fly.jiejing.adapter.OrderCommonPlaceAdapter;
import com.fly.jiejing.entity.Address;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.entity.Order;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.smssdk.gui.GroupListView;

/**
 * 下订单
 * android语言识别功能
 * 特别注意：如果手机有语言设别功能，请开启网络，因为系统会根据你的声音数据到google云端获取声音数据
 */
public class MainOrderAct extends Activity implements View.OnClickListener {

    private TextView itemNameActionBar;
    private Items item;
    private TextView priceIntroduce;
    private TextView confirmTime;
    //   private TextView confirmWeek;
    private TextView confirmDate;
    private TextView confirmCity;
    private TextView confirmArea;
    //  private ImageView soundRecord;
    private TextView confirmHint;
    private EditText confirmRemark;
    FrameLayout confirmFragment;
    private SharedPreferences sp;
    String userid = null;
    String time = null;
    String remark = null;
    Address address;
    String streetDetail;
    ListView myList;
    List<Cleaner> mData;
    OrderCommonPlaceAdapter adapter;
    TextView confirmCleanerId;
    ImageView confirmNotChoose;
    TextView commonCleanerList_null;
    Button all_cleaner;
    ImageView itemDetail;
    RelativeLayout confirmTypeContainer;
    TextView confirmType;
    EditText confirmTypeNum;
    boolean flag = true;//当为false的时候，表示item的type为空，不显示
    String special;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_order_layout);
        initView();//初始化所有對象
    }

    private void initView() {
        address = new Address();
        mData = new ArrayList<>();
        order = new Order();
        commonCleanerList_null = (TextView) findViewById(R.id.commonCleanerList_null);
        itemDetail = (ImageView) findViewById(R.id.itemDetail);
        userid = MainApplication.getApplication().getUser().getUser_id();
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        item = (Items) getIntent().getSerializableExtra("item");
        itemNameActionBar = (TextView) findViewById(R.id.itemNameTitle);
        priceIntroduce = (TextView) findViewById(R.id.priceIntroduce);
        confirmTime = (TextView) findViewById(R.id.confirmTime);
        //    confirmWeek = (TextView) findViewById(R.id.confirmWeek);
        confirmDate = (TextView) findViewById(R.id.confirmDate);
        confirmCity = (TextView) findViewById(R.id.confirmCity);
        confirmArea = (TextView) findViewById(R.id.confirmArea);
        //     soundRecord = (ImageView) findViewById(R.id.soundRecord);
        confirmRemark = (EditText) findViewById(R.id.confirmRemark);
        confirmHint = (TextView) findViewById(R.id.confirmHint);
        confirmFragment = (FrameLayout) findViewById(R.id.confirmFragment);
        //特殊情况的类型，比如服务时长，窗户个数，厕所间数
        confirmTypeContainer = (RelativeLayout) findViewById(R.id.confirmTypeContainer);
        confirmType = (TextView) findViewById(R.id.confirmType);
        confirmTypeNum = (EditText) findViewById(R.id.confirmTypeNum);
        myList = (ListView) findViewById(R.id.commonCleanerList);
        confirmCleanerId = (TextView) findViewById(R.id.confirmCleanerId);
        all_cleaner = (Button) findViewById(R.id.all_cleaner);
        confirmNotChoose = (ImageView) findViewById(R.id.confirmNotChoose);
        initEvent();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    public void initEvent() {
        priceIntroduce.setText(item.getPrice());
        confirmCity.setText(item.getCityName());
        viewShow();//语音识别執行操作
        //加载常用阿姨，与地址的公用
        adapter = new OrderCommonPlaceAdapter(this, mData);
        myList.setAdapter(adapter);
        initData();//得到常用阿姨
        myList.setOnItemClickListener(itemClickListener);
        confirmNotChoose.setOnClickListener(this);
        confirmFragment.setOnClickListener(this);
        //项目详情
        itemDetail.setOnClickListener(this);
        if (item.getType() != null && item.getType().length() > 0) {
            flag = false;
            confirmTypeContainer.setVisibility(View.VISIBLE);
            confirmType.setText(item.getType() + ":");
            Log.e("---", item.getType() + "");
            order.setType(item.getType());
        }

        all_cleaner.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itemDetail:
                Intent intent = new Intent(MainOrderAct.this, ShowItemDetailAct.class);
                intent.putExtra("item", item);
                Log.e("---", item.getCityName() + "");
                startActivity(intent);
                break;
            case R.id.confirmNotChoose:
                confirmCleanerId.setText("");
                confirmCleanerId.setHint("（可以不选，商家指派）");
                break;
            case R.id.confirmFragment:
                startActivityForResult(new Intent(MainOrderAct.this, MainOrderTimeAct.class), 1);
                break;
            case R.id.all_cleaner:
                Intent intent1 = new Intent(MainOrderAct.this, CleanerListActivity.class);
                intent1.putExtra("item", item);
                startActivity(intent1);
                break;
        }
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int cleanerid = ((Cleaner) adapter.getItem(position)).getCleanerid();
            confirmCleanerId.setText(cleanerid + "");
        }
    };

    //请求常用阿姨名单
    public void initData() {
        StringPostRequest postRequest = new StringPostRequest(UrlUtils.BLACKCLEANER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Cleaner> gData = gson.fromJson(s, new TypeToken<ArrayList<Cleaner>>() {
                }.getType());
                if (gData != null && !gData.isEmpty()) {
                    mData.clear();
                    for (int i = 0; i < gData.size(); i++) {
                        //当该服务人员属于该城市的时候并且是对该项服务进行服务时则显示
                        if (gData.get(i).getCity().equals(confirmCity.getText().toString()) && gData.get(i).getPid() == item.getPid()) {
                            mData.add(gData.get(i));
                        }
                    }
                    if (mData != null && mData.size() > 0) {
                        commonCleanerList_null.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainOrderAct.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
            }
        });
        postRequest.putParams("action", "select");
        postRequest.putParams("userid", sp.getString("PHONE_NUMBER", null));
        postRequest.putParams("iscommon", "1");
        MainApplication.getApplication().getQueue().add(postRequest);
    }




    AlertDialog alertDialog;

    String cleaner_id;

    //提交订单
    public void submitOrder(View v) {
        remark = confirmRemark.getText().toString();
        streetDetail = confirmArea.getText().toString().trim();
        cleaner_id = confirmCleanerId.getText().toString();
        if (userid == null || userid.length() <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("您未登陆").setMessage("您是否现在去登陆");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    return;
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    startActivity(new Intent(MainOrderAct.this, UserLoginActivity.class));
                    return;
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        } else if (time == null) {
            Toast.makeText(this, "您没有填写时间", Toast.LENGTH_SHORT).show();
            return;
        } else if (streetDetail == null) {
            Toast.makeText(this, "您没有填写地址", Toast.LENGTH_SHORT).show();
            return;
        } else if (flag && confirmTypeNum.getText() == null) {
            Toast.makeText(this, "您没有填写" + item.getType(), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (flag && confirmTypeNum.getText() == null) {
                special = "";
            } else {
                special = confirmTypeNum.getText().toString();
            }
            order.setPic_check(item.getPic_check());
            order.setName(item.getName());
            order.setPid(item.getPid());
            order.setTime(time);
            if (cleaner_id==null){
                order.setCleaner_id(0);
            }else {
                order.setCleaner_id(Integer.valueOf(cleaner_id));
            }

            order.setAddress(streetDetail + "");
            order.setRemark(remark + "");
            order.setCity(item.getCityName());
            String cleanerId = confirmCleanerId.getText().toString();
            StringPostRequest request = new StringPostRequest(UrlUtils.ORDER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    if (s.equals("0")) {
                        Toast.makeText(MainOrderAct.this, "下单失败，请重新下单", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainOrderAct.this, "下单成功，等待接单", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainOrderAct.this, MainOrderInfoActivity.class);
                        Gson gson = new Gson();
                        List<Order> gData = gson.fromJson(s, new TypeToken<ArrayList<Order>>() {
                        }.getType());
                        if (gData != null && gData.size() > 0) {
                            Log.e("---", gData.get(0).getCreatetime() + "," + gData.get(0).getId()
                                    + "," + gData.get(0).getPrice_num());
                            order.setCreatetime(gData.get(0).getCreatetime());
                            order.setId(gData.get(0).getId());
                            order.setPrice_num(gData.get(0).getPrice_num());
                            intent.putExtra("myOrder", order);
                            startActivity(intent);
                        }

                    }
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(MainOrderAct.this, "下单失败，请重新下单", Toast.LENGTH_SHORT).show();
                }
            });
            request.putParams("action", "insert");
            request.putParams("userid", userid);
            request.putParams("uid", item.getUid() + "");
            request.putParams("city", item.getCityName() + "");
            request.putParams("address", streetDetail);
            request.putParams("time", time);
            request.putParams("remark", remark + "");
            if (!special.equals("")) {
                request.putParams("special", special);
            }
            request.putParams("cleanerid", cleanerId + "");
            MainApplication.getApplication().getQueue().add(request);
        }
    }

    private void viewShow() {
        findViewById(R.id.backItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        itemNameActionBar.setText(item.getName());

        confirmArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainOrderAct.this, MainOrderPlaceAct.class);
                intent.putExtra("city", item.getCityName());

                startActivityForResult(intent, 0);
            }
        });
    }

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            List<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < matches.size(); i++) {
                result.append(matches.get(i).toString());
            }
            confirmRemark.setText(result);
            remark = result.toString();
        }
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String a = data.getStringExtra("address");
            confirmArea.setTextColor(Color.parseColor("#ffcf00"));
            confirmArea.setText(a);
            streetDetail = a;
        }

        if (requestCode == 1 && resultCode == 2) {
            ArrayList<String> a = data.getStringArrayListExtra("ORDERTIME");
            if (a != null && a.size() > 0) {
                confirmDate.setText(a.get(0));
                confirmTime.setText(a.get(1));
                time = a.get(0) + " " + a.get(1);
                confirmHint.setVisibility(View.GONE);
            }

        }
    }

}
