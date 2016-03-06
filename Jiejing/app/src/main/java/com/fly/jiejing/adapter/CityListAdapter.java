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
 * Created by Administrator on 2015/10/8.在首页加载城市列表
 */
public class CityListAdapter extends BaseAdapter {

    private List<Address> datas;
    private Context context;
    private onSelectedCity listener;

    public onSelectedCity getListener() {
        return listener;
    }

    public void setListener(onSelectedCity listener) {
        this.listener = listener;
    }

    public CityListAdapter(List<Address> datas, Context context) {
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
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.main_city_item, null);
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100);
            view.setLayoutParams(lp);
            holder.cityName = (TextView) view.findViewById(R.id.cityName);
            holder.selected = (CheckBox) view.findViewById(R.id.citySelected);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.cityName.setText(this.datas.get(position).getCity());


        final CheckBox checkBox = holder.selected;


        if (this.datas.get(position).isSelected()) {
            i = position;
            checkBox.setChecked(true);
            checkBox.setButtonDrawable(R.mipmap.siftaunt_page_select_small);
        } else {
            checkBox.setChecked(false);
            checkBox.setButtonDrawable(R.mipmap.siftaunt_page_no_select_small);
        }
        holder.selected.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != i) {
                            datas.get(position).setSelected(1);
                            datas.get(i).setSelected(0);
                            listener.onCheckBoxChanged(datas.get(position), datas.get(i));
                        }
                    }
                }
        );
        return view;
    }

    private class ViewHolder{
        TextView cityName;
        CheckBox selected;
    }

    public interface onSelectedCity {
        public void onCheckBoxChanged(Address nowChecked, Address oldChecked);
    }
}
