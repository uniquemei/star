package com.fly.jiejing.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Order;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.UrlUtils;

public class MyOrderInfoActivity extends Activity implements View.OnClickListener {
    private Order order;
    private TextView orderInfo_service_time;
    private TextView orderInfo_id;
    private TextView orderinfo_state;
    private TextView orderInfo_service_palace;
    private TextView orderInfo_service_special;
    private ImageView orderInfo_back;
    private int state;
    private ImageView pic;
    private TextView serviceName;
    private TextView orderInfo_service_money;

    LinearLayout moreorderInfo_end_time;
    TextView orderInfo_cleanerid;
    TextView orderInfo_end_time;
    TextView orderInfo_create_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_info);
        order = (Order) getIntent().getSerializableExtra("myOrder");
        initView();

    }

    //数据的初始化
    public void initView() {
        orderInfo_cleanerid = (TextView) findViewById(R.id.orderInfo_cleanerid);
        orderInfo_end_time = (TextView) findViewById(R.id.orderInfo_end_time);
        orderInfo_back = (ImageView) findViewById(R.id.orderInfo_back);
        orderInfo_service_time = (TextView) findViewById(R.id.orderInfo_service_time);
        orderInfo_id = (TextView) findViewById(R.id.orderInfo_id);
        orderInfo_service_palace = (TextView) findViewById(R.id.orderInfo_service_palace);
        orderInfo_service_special = (TextView) findViewById(R.id.orderInfo_service_special);
        orderInfo_service_money = (TextView) findViewById(R.id.orderInfo_service_money);
        orderinfo_state = (TextView) findViewById(R.id.orderinfo_state);
        pic = (ImageView) findViewById(R.id.order_img);
        serviceName = (TextView) findViewById(R.id.serviceName);
        moreorderInfo_end_time = (LinearLayout) findViewById(R.id.moreorderInfo_end_time);
        orderInfo_create_time = (TextView) findViewById(R.id.orderInfo_create_time);
        initEvent();
    }
    public void initEvent() {
        orderinfo_state.setVisibility(View.VISIBLE);
        orderInfo_back.setOnClickListener(this);
        if (order != null) {
            state = order.getState();

            Log.e("----", order.getId() + "");
            orderInfo_id.setText(order.getId() + "");
            orderInfo_service_time.setText(order.getTime() + "");
            orderInfo_service_palace.setText(order.getAddress() + "");
            Log.e("----", order.getCleaner_id() + "");
            orderInfo_cleanerid.setText(order.getCleaner_id()+"");
            String remark = "";
            if (order.getRemark() == null) {
                remark = "";
            } else {
                remark = order.getRemark();
            }
            Log.i("---",order.getCreatetime()+"");
            orderInfo_create_time.setText(order.getCreatetime()+"");
            orderInfo_service_special.setText(remark);
            serviceName.setText(order.getName());
            orderInfo_service_money.setText(order.getPrice());
            if (state == 0) {
                orderinfo_state.setText("待接单");
            } else if (state == 1) {
                orderinfo_state.setText("服务中");
            } else if (state == 2) {
                orderinfo_state.setText("已取消");
            } else if (state == 3) {
                orderinfo_state.setText("已完成");
                moreorderInfo_end_time.setVisibility(View.VISIBLE);
                orderInfo_end_time.setText(order.getEndtime());
            }
            //获得图片
            String urlImag = UrlUtils.IMG_URL + order.getPic_check();
            ImageLoaderUtil.display(urlImag, pic);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.orderInfo_back:
                finish();
                break;
        }
    }
}
