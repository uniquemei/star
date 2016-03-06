package yu.cleaner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import yu.cleaner.R;
import yu.cleaner.entity.Order;
import yu.cleaner.util.ImageLoaderUtil;
import yu.cleaner.util.UrlUtils;


public class MainOrderInfoActivity extends Activity implements View.OnClickListener {
    private Order order;
    private TextView orderInfo_service_time;
    private TextView orderInfo_id;

    private TextView orderInfo_service_palace;
    private TextView orderInfo_service_special;
    private TextView orderInfo_create_time;
    private ImageView orderInfo_back;

    private ImageView pic;
    private TextView serviceName;
    private TextView orderInfo_service_money;
    TextView orderInfo_cleanerid;
    TextView orderInfo_end_time;
    LinearLayout moreorderInfo_end_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order_info);
        order = (Order) getIntent().getSerializableExtra("myOrder");
        initView();

    }

    //数据的初始化
    public void initView() {

        orderInfo_create_time = (TextView) findViewById(R.id.orderInfo_create_time);
        orderInfo_back = (ImageView) findViewById(R.id.orderInfo_back);
        orderInfo_service_time = (TextView) findViewById(R.id.orderInfo_service_time);
        orderInfo_id = (TextView) findViewById(R.id.orderInfo_id);
        orderInfo_cleanerid = (TextView) findViewById(R.id.service_cleaner);
        orderInfo_service_palace = (TextView) findViewById(R.id.orderInfo_service_palace);
        orderInfo_service_special = (TextView) findViewById(R.id.orderInfo_service_special);
        orderInfo_service_money = (TextView) findViewById(R.id.orderInfo_service_money);
        orderInfo_end_time = (TextView) findViewById(R.id.orderInfo_end_time);
        moreorderInfo_end_time = (LinearLayout) findViewById(R.id.moreorderInfo_end_time);

        pic = (ImageView) findViewById(R.id.order_img);
        serviceName = (TextView) findViewById(R.id.serviceName);
        initEvent();
    }

    public void initEvent() {
        orderInfo_back.setOnClickListener(this);
        if (order != null) {
            orderInfo_create_time.setText(order.getCreatetime());
            orderInfo_id.setText(order.getId() + "");
            orderInfo_service_time.setText(order.getTime() + "");
            orderInfo_service_palace.setText(order.getAddress() + "");
            if (order.getLogin_id() == 0) {
                orderInfo_cleanerid.setVisibility(View.VISIBLE);
                if (order.getCleaner_id() == 0) {
                    orderInfo_cleanerid.setText("服务人员：待指派");
                } else {
                    orderInfo_cleanerid.setText("服务人员编号：" + order.getCleaner_id());
                }

            }

            String remark = "";
            if (order.getRemark() == null) {
                remark = "";
            } else {
                remark = order.getRemark();
            }
            orderInfo_service_special.setText(remark);
            serviceName.setText(order.getName());
            if (order.getPrice_num() == 0) {
                orderInfo_service_money.setText("待商议");
            } else {
                orderInfo_service_money.setText(order.getPrice_num() + "元");
            }
            if (order.getState() == 3) {
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
