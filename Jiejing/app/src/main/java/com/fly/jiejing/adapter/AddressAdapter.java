package com.fly.jiejing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Address;

import java.util.List;

/**
 * Created by Administrator on 2015/10/8.加载用户的常用地址
 */
public class AddressAdapter extends BaseAdapter {

    private List<Address> datas;
    private Context context;
    private onSelectedCity listener;

    public onSelectedCity getListener() {
        return listener;
    }

    public void setListener(onSelectedCity listener) {
        this.listener = listener;
    }

    public AddressAdapter(List<Address> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.main_order_place_layout_item, null);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            holder.addressCommon= (TextView) view.findViewById(R.id.addressCommon);
            holder.addressNum= (TextView) view.findViewById(R.id.addressNum);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.addressNum.setText(this.datas.get(position).getCity());
        holder.addressCommon.setText(this.datas.get(position).getStreet()+this.datas.get(position).getDetail());

        return view;
    }

    private class ViewHolder{
        TextView addressCommon;
TextView addressNum;
    }

    public interface onSelectedCity {
        public void onCheckBoxChanged(Address nowChecked, Address oldChecked);
    }
}
