package com.fly.jiejing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.fly.jiejing.R;
import com.fly.jiejing.entity.Order;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.UrlUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/3.用户未完成订单列表
 */
public class MyOrderNotCompleteAdapter extends BaseAdapter {
    private List<Order> mData;
    private Context mContext;
    private onSetDeleteOrder listener;

    public MyOrderNotCompleteAdapter(List<Order> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    public void setListenner(onSetDeleteOrder listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {

        return mData.size();
    }

    @Override
    public Object getItem(int position) {

        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_order_item, null);
            vHolder = new ViewHolder();
            vHolder.imageView = (ImageView) convertView.findViewById(R.id.order_complete_img);
            vHolder.serviceName = (TextView) convertView.findViewById(R.id.serviceNameitem);
            vHolder.servicePlace = (TextView) convertView.findViewById(R.id.servicePlace);
            vHolder.serviceTime = (TextView) convertView.findViewById(R.id.serviceTime);
            vHolder.order_state = (TextView) convertView.findViewById(R.id.order_state);
            vHolder.deleteButton = (Button) convertView.findViewById(R.id.delete_order_button);
            vHolder.completeButton = (Button) convertView.findViewById(R.id.comment_order_button);
            convertView.setTag(vHolder);
        } else {
            vHolder = (ViewHolder) convertView.getTag();
        }
        //捆绑数据
        final Order order = mData.get(position);
        //网络加载图片
        String urlImag = UrlUtils.IMG_URL + order.getPic_check();
        ImageLoaderUtil.display(urlImag, vHolder.imageView);

        vHolder.serviceTime.setText(order.getTime());
        vHolder.servicePlace.setText(order.getAddress());
        vHolder.serviceName.setText(order.getName());

        // 定义订单的状态
        final String state = order.getState()+"";
        if (state.equals("0")) {
            vHolder.deleteButton.setText("取消订单");
            vHolder.order_state.setText("待接单");
        } else if (state.equals("1")) {
            vHolder.deleteButton.setText("退订服务");
            vHolder.order_state.setText("已接单");
            vHolder.completeButton.setText("确认完成");
            vHolder.completeButton.setVisibility(View.VISIBLE);
        } else if (state.equals("2")) {
            vHolder.order_state.setText("已取消");
        } else if (state.equals("3")) {
            vHolder.deleteButton.setText("删除订单");
            vHolder.order_state.setText("已完成");
        }

        //定义按钮状态
        final ViewHolder finalVHolder = vHolder;
        vHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null ){
                    listener.deletOrder(order);
                }
            }
        });
        vHolder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && state.equals("1")) {
                 // finalVHolder.deleteButton.setText("删除订单");
                    listener.update(order);
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        private ImageView imageView;
        private TextView serviceName;//服务的名字
        private TextView order_state;
        private TextView servicePlace;//服务地址
        private TextView serviceTime;//服务时间
        private Button deleteButton;//是否删除
        private Button completeButton;//是否删除
    }

    public interface onSetDeleteOrder {
        public void deletOrder(Order order);

        public void update(Order order);
    }

}
