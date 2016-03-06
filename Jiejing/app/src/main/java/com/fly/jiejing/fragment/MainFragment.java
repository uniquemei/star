package com.fly.jiejing.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fly.jiejing.R;
import com.fly.jiejing.activity.MainAllTypeAct;
import com.fly.jiejing.activity.MainOrderAct;
import com.fly.jiejing.activity.ThreeShowLifeAct;
import com.fly.jiejing.activity.UserLoginActivity;
import com.fly.jiejing.adapter.CityListAdapter;
import com.fly.jiejing.adapter.ImagePgAdapter;
import com.fly.jiejing.dao.ItemDao;
import com.fly.jiejing.entity.Address;
import com.fly.jiejing.entity.DataManager;
import com.fly.jiejing.entity.ImageAdvertisement;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.entity.Life;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.request.StringPostRequest;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/1.首页
 * Ctrl+P，可以显示参数信息
 */
public class MainFragment extends Fragment implements CityListAdapter.onSelectedCity {


    private ViewPager imagePager;
    private LinearLayout indicatorContainer;
    private View view;//总布局
    private ImagePgAdapter pgAdapter;
    private List<ImageView> imageViews;
    private TextView allType;
    private ImageView callTel;
    private TextView setCity;
    //热门项目
    TextView itemTop1;
    ImageView itemTopPic1;
    TextView itemTop2;
    ImageView itemTopPic2;
    TextView itemTop3;
    ImageView itemTopPic3;
    TextView itemTop4;
    ImageView itemTopPic4;
    LinearLayout top1;
    LinearLayout top2;
    LinearLayout top3;
    LinearLayout top4;
    //设置城市，并请求为这个城市提供的服务
    List<Address> datas;
    String defaultCity;
    SharedPreferences sp;
    AlertDialog alertDialog;
    CityListAdapter adapter;
    PopupWindow pw = null;
    int positions;
    private boolean isTouched = false;
    Intent intent;
    Life life;
    String url;
    int num = 0;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_layout, null);
        initView();
        //头部的广告
        addHead();
        //页面所有的点击事件
        onAllClickListener();
        //默认城市更新数据
        getItemData(defaultCity);
        setCity.setText(defaultCity);
        return view;
    }

    //初始化
    private void initView() {
        intent = new Intent();
        life = new Life();
        datas = new ArrayList<>();
        sp = getActivity().getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_PRIVATE);
        if (sp.getString("DEFAULE_CITY", "").equals("")) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("DEFAULE_CITY", "烟台");
            editor.commit();
        }
        defaultCity = sp.getString("DEFAULE_CITY", "烟台");
        imagePager = (ViewPager) view.findViewById(R.id.imagePager);
        indicatorContainer = (LinearLayout) view.findViewById(R.id.indicatorContainer);
        allType = (TextView) view.findViewById(R.id.allType);
        callTel = (ImageView) view.findViewById(R.id.callTel);
        setCity = (TextView) view.findViewById(R.id.setCity);
        itemTop1 = (TextView) view.findViewById(R.id.itemTop1);
        itemTopPic1 = (ImageView) view.findViewById(R.id.itemTopPic1);
        itemTop2 = (TextView) view.findViewById(R.id.itemTop2);
        itemTopPic2 = (ImageView) view.findViewById(R.id.itemTopPic2);
        itemTop3 = (TextView) view.findViewById(R.id.itemTop3);
        itemTopPic3 = (ImageView) view.findViewById(R.id.itemTopPic3);
        itemTop4 = (TextView) view.findViewById(R.id.itemTop4);
        itemTopPic4 = (ImageView) view.findViewById(R.id.itemTopPic4);
        top1 = (LinearLayout) view.findViewById(R.id.top1);
        top2 = (LinearLayout) view.findViewById(R.id.top2);
        top3 = (LinearLayout) view.findViewById(R.id.top3);
        top4 = (LinearLayout) view.findViewById(R.id.top4);

    }

    //页面头部按钮的点击事件
    public void onAllClickListener() {
        //设置默认城市
        setCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringPostRequest postRequest = new StringPostRequest(UrlUtils.SHOW_ITEMS_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        List<Address> gData = gson.fromJson(s, new TypeToken<ArrayList<Address>>() {
                        }.getType());
                        if (gData != null && gData.size() > 0) {
                            datas.clear();
                            datas.addAll(gData);
                            setCity();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "连接服务器失败", Toast.LENGTH_SHORT).show();
                    }
                });
                postRequest.putParams("action", "city");
                MainApplication.getApplication().getQueue().add(postRequest);

            }
        });
        //客服电话
        callTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTel();
            }
        });

        //转向全部服务页面
        allType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainAllTypeAct.class);
                intent.putExtra("cityname", setCity.getText().toString());
                startActivity(intent);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                imagePager.setCurrentItem(positions);
            }
        }
    };

    //加载头部的广告
    private void addHead() {
        final List<ImageAdvertisement> imageList
                = DataManager.getDataManager().getAdvertisementList();
        imageViews = new ArrayList<>();
        ImageView imageView;
        for (int i = 0; i < imageList.size(); i++) {
            imageView = new ImageView(getActivity());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(imageList.get(i).getImage());
            url = imageList.get(i).getLink();
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (url != null) {
                        intent.setClass(getActivity(), ThreeShowLifeAct.class);
                        life.setUrl(url);
                        intent.putExtra("life", life);
                        startActivity(intent);
                    }
                }
            });
            imageViews.add(imageView);
        }
        pgAdapter = new ImagePgAdapter(imageViews);
        imagePager.setAdapter(pgAdapter);
        imagePager.setCurrentItem(imageViews.size() % 2);
        ImageView v;
        //imageViews.size()
        for (int i = 0; i < imageViews.size(); i++) {
            v = (ImageView) LayoutInflater.from(getActivity()).inflate(R.layout.main_indicator, null);
            if (i == imageViews.size() % 2) {
                v.setImageResource(R.mipmap.pager_dot_selected);
            } else {
                v.setImageResource(R.mipmap.pager_dot_normal);
            }
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 0, 0, 10);
            indicatorContainer.addView(v, lp);
        }
        //       positions = imageViews.size() % 2;
        //线程实现自动滑动
//        Thread change = new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                while (true) {
//                    if (!isTouched) {
//                        positions++;
//                        mHandler.sendEmptyMessage(0);
//                    }
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });

//        change.start();

//        imagePager.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View arg0, MotionEvent event) {
//                // TODO Auto-generated method stub
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_MOVE:
//                        isTouched = true;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        isTouched = false;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
//头部广告的翻阅

        imagePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < imageViews.size(); i++) {
                    ImageView iView = (ImageView) indicatorContainer.getChildAt(i);
                    num = position % imageViews.size();
                    if (i == (position % imageViews.size())) {
                        iView.setImageResource(R.mipmap.pager_dot_selected);
                    } else {
                        iView.setImageResource(R.mipmap.pager_dot_normal);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    //客服电话弹出框
    public void callTel() {
        final String tel = "18363820127";
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("联系客服").setMessage("电话：" + tel);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        builder.setPositiveButton("呼叫", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri telUri = Uri.parse("tel:" + tel);
                startActivity(new Intent(Intent.ACTION_DIAL, telUri));
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    //设置城市弹出框
    public void setCity() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.main_city_layout, null);
        pw = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ListView cityList = (ListView) view.findViewById(R.id.cityList);
        ImageView backPop = (ImageView) view.findViewById(R.id.backPop);
        pw.setFocusable(true);
        pw.setOutsideTouchable(true);
        final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.8f;
        getActivity().getWindow().setAttributes(lp);
        pw.setBackgroundDrawable(getActivity().getBaseContext()
                .getResources().getDrawable(android.R.color.white));
        pw.setAnimationStyle(R.style.mypopWindow);
        pw.showAsDropDown(setCity);

        //设置的城市的
        setDefaultCity(setCity.getText().toString());
        adapter = new CityListAdapter(datas, getActivity());
        cityList.setAdapter(adapter);
        adapter.setListener(this);
        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Address address = (Address) adapter.getItem(position);
                //改变城市
                if (!address.isSelected()) {
                    address.setSelected(1);
                    //checkBox选中其余不选中
                    for (Address d : datas) {
                        if (!d.getCity().equals(address.getCity())) {
                            d.setSelected(0);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    setAlertDialogOk(address.getCity());
                }
            }
        });
        //点击*消失
        backPop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        //pop消失的时候后面背景颜色消失
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lp.alpha = 1.0f;
                getActivity().getWindow().setAttributes(lp);
                //请求获取数据
            }
        });
    }

    private void setDefaultCity(String dc) {
        for (Address d : datas) {
            if (d.getCity().equals(dc)) {
                d.setSelected(1);
            } else {
                d.setSelected(0);
            }
        }
    }

    //确认改变城市，数据请求成功
    public void setAlertDialogOk(final String city) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置").setMessage("默认城市：" + city);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //异步载数据
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("DEFAULE_CITY", city);
                editor.commit();

                getItemData(city);

                setCity.setText(city);
                pw.dismiss();
                dialog.dismiss();


            }
        }).show();
    }


    //热门服务的跳转事件监听
    public void goIntent(Items item) {
        User user = MainApplication.getApplication().getUser();
        if (user != null && user.getUser_id().length() > 0) {
            //去下订单
            Intent intent = new Intent(getActivity(), MainOrderAct.class);
            intent.putExtra("item", item);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("登陆后才能下单").setMessage("您是否现在去登陆");
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
                    startActivity(new Intent(getActivity(), UserLoginActivity.class));
                    return;
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }
    }

    //请求城市的所有服务项目
    public void getItemData(final String city) {
        StringPostRequest request = new StringPostRequest(UrlUtils.SHOW_ITEMS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String s) {
                Gson gson = new Gson();
                List<Items> itemses = gson.fromJson(s, new TypeToken<ArrayList<Items>>() {
                }.getType());
                if (itemses != null && itemses.size() > 0) {
                    List<Items> data = new ArrayList<>();
                    for (int i = 0; i < itemses.size(); i++) {
                        if (itemses.get(i).getPid() != 0) {
                            data.add(itemses.get(i));
                        }
                         /*else {
                            SharedPreferences.Editor editor = sp.edit();
                       //     itemses.get(i).getUid(),
                            editor.putString("TYPE" ,itemses.get(i).getUid()+"");
                            editor.commit();
                        }*/
                    }
                    //存储数据
                    ItemDao dao = new ItemDao(getActivity());
                    dao.addAllData(itemses);
                    updateTop(data);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ItemDao dao = new ItemDao(getActivity());
                updateTop(dao.findAllByCity(city));
                Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
            }
        });
        request.putParams("action", "item");
        request.putParams("cityname", city + "");
        MainApplication.getApplication().getQueue().add(request);
    }

    @Override
    public void onCheckBoxChanged(Address nowChecked, Address oldChecked) {
        setAlertDialogOk(nowChecked.getCity());
    }

    //更新热门服务
    public void updateTop(final List<Items> top) {
        if (top != null && top.size() >= 3) {
            itemTop1.setText(top.get(0).getName());
            ImageLoaderUtil.display(UrlUtils.IMG_URL + top.get(0).getPic(), itemTopPic1);
            top1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goIntent(top.get(0));
                }
            });
            itemTop2.setText(top.get(1).getName());
            ImageLoaderUtil.display(UrlUtils.IMG_URL + top.get(1).getPic(), itemTopPic2);
            top2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goIntent(top.get(1));
                }
            });
            itemTop3.setText(top.get(2).getName());
            ImageLoaderUtil.display(UrlUtils.IMG_URL + top.get(2).getPic(), itemTopPic3);
            top3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goIntent(top.get(2));
                }
            });
            itemTop4.setText(top.get(3).getName());
            ImageLoaderUtil.display(UrlUtils.IMG_URL + top.get(3).getPic(), itemTopPic4);
            top4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goIntent(top.get(3));
                }
            });

        }
    }
}
