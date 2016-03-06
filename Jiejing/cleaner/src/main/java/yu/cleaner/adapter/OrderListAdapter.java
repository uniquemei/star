package yu.cleaner.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import yu.cleaner.R;
import yu.cleaner.entity.Order;

/**
 * Created by Administrator on 2015/10/8.在首页加载城市列表
 */
public class OrderListAdapter extends BaseAdapter {

    private List<Order> datas;
    private Context context;
    private onSelected listener;
    private String cleanerid;

    public onSelected getListener() {
        return listener;
    }

    public void setListener(onSelected listener) {
        this.listener = listener;
    }

    public OrderListAdapter(List<Order> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public OrderListAdapter(List<Order> datas, Context context, String cleanerid) {
        this.datas = datas;
        this.context = context;
        this.cleanerid = cleanerid;
    }

    @Override
    public int getCount() {
        return this.datas == null ? 0 : this.datas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.datas == null ? null : this.datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    int i = 0;

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.order_items, null);
//            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100);
//            view.setLayoutParams(lp);
            holder.name = (TextView) view.findViewById(R.id.order_uid_name);
            holder.time = (TextView) view.findViewById(R.id.order_time);
            holder.place = (TextView) view.findViewById(R.id.order_place);
            holder.contactUser = (Button) view.findViewById(R.id.call_user);
            holder.getOrder = (Button) view.findViewById(R.id.accept_order);
            holder.order_cleanerid = (TextView) view.findViewById(R.id.order_cleanerid);
            holder.contanier = (RelativeLayout) view.findViewById(R.id.contanier);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Order order = this.datas.get(position);
        holder.name.setText("类型：" + order.getName());
        holder.time.setText("时间：" + order.getTime());
        holder.place.setText("地址：" + order.getAddress());

        holder.contactUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onContactUser(order);
                        }
                    }
                }
        );

        holder.getOrder.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onGetOrder(order);
                        }
                    }
                }
        );
//        if (cleanerid.equals("0")) {
//            holder.order_cleanerid.setVisibility(View.VISIBLE);
//            holder.order_cleanerid.setText("服务人员编号："+order.getId());
//        }
        switch (order.getState()) {
            case 0:
                holder.getOrder.setVisibility(View.VISIBLE);
                break;
            case 1:
                break;
            case 3:
                holder.contanier.setVisibility(View.GONE);
                holder.contanier.setVisibility(View.GONE);
                break;

        }

        return view;
    }

    private class ViewHolder {
        TextView name;
        TextView time;
        TextView place;
        Button contactUser;
        Button getOrder;
        TextView order_cleanerid;
        RelativeLayout contanier;
    }

    public interface onSelected {
        public void onGetOrder(Order o);

        public void onContactUser(Order o);
    }
}
