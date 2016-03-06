package com.fly.jiejing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.fly.jiejing.R;
import com.fly.jiejing.adapter.TypeAdapter;
import com.fly.jiejing.dao.ItemDao;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.units.MainApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有服务项目数据
 */
public class MainAllTypeAct extends Activity implements TypeAdapter.onStartActivity {

    private ImageView backAllServer;
    private ListView allTypeList;
    private List<Items> datas;
    private TypeAdapter adapter;
    AlertDialog alertDialog;
    String cityname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_server_layout);
        initView();
        cityname= getIntent().getStringExtra("cityname");
        backAllServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData(cityname);
    }

    private void initView() {
        datas = new ArrayList<>();
        backAllServer = (ImageView) findViewById(R.id.backAllServer);
        allTypeList = (ListView) findViewById(R.id.allTypeList);

        adapter = new TypeAdapter(datas, this);
        allTypeList.setAdapter(adapter);
        adapter.setListener(this);
    }


    //得到所有的服务项目
    private void getData(String name) {

        ItemDao dao = new ItemDao(this);
        if(name!=null&&name.length()>0){
            List<Items> itemses = dao.findAllByCity(name);
            if (null != itemses & (!itemses.isEmpty())) {
                datas.clear();
                for (int i = 0; i < itemses.size(); i++) {
                    if (itemses.get(i).getPid() == 0) {
                        List<Items> childs = new ArrayList<>();
                        for (Items child : itemses) {
                            if (child.getPid() == itemses.get(i).getUid()) {
                                childs.add(child);
                            }
                        }
                        itemses.get(i).setChild(childs);
                        datas.add(itemses.get(i));
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }

    }

    //跳转到下服务订单
    @Override
    public void goIntent(Items item) {
        User user = MainApplication.getApplication().getUser();
        if (user != null && user.getUser_id().length() > 0) {
            Intent intent = new Intent(this, MainOrderAct.class);
            item.setCityName(cityname);
            intent.putExtra("item", item);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("登陆后才能下单").setMessage("您是否现在去登陆?");
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
                    startActivity(new Intent(MainAllTypeAct.this, UserLoginActivity.class));
                    return;
                }
            });
            alertDialog = builder.create();
            alertDialog.show();
        }

    }
}
