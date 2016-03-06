package com.fly.jiejing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.units.NoScrollGridView;
import com.fly.jiejing.entity.Items;

import java.util.List;

/**
 * Created by Administrator on 2015/10/3.全部服务的加载
 */
public class TypeAdapter extends BaseAdapter {

    private List<Items> datas;
    private Context context;
    private onStartActivity listener;

    public onStartActivity getListener() {
        return listener;
    }

    public void setListener(onStartActivity listener) {
        this.listener = listener;
    }

    public TypeAdapter(List<Items> datas, Context context) {
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
        return this.datas == null ? 0 : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.main_server_type,
                    null);

            holder.title = (TextView) view.findViewById(R.id.serverType);
            holder.grid = (NoScrollGridView) view.findViewById(R.id.grid);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // 绑定数据
        final Items items = this.datas.get(position);

        holder.title.setText(items.getName());
        //根据类型得到服务项目列表
        TypeGridAdapter adapter = new TypeGridAdapter(items.getChild(), context);
        holder.grid.setAdapter(adapter);
        holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Items item = items.getChild().get(position);
                listener.goIntent(item);
            }

        });

        return view;
    }

    public static class ViewHolder {
        public TextView title;
        public NoScrollGridView grid;
    }

    public static interface onStartActivity {
        public void goIntent(Items item);
    }
}
