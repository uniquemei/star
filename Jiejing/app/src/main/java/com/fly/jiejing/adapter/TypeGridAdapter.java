package com.fly.jiejing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.UrlUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/2.重写GridAdapter
 */
public class TypeGridAdapter extends BaseAdapter{

    private Context context;
    private List<Items> datas;


    public TypeGridAdapter(List<Items> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.main_server_item,null);
            holder.name= (TextView) view.findViewById(R.id.itemName);
            holder.pic= (ImageView) view.findViewById(R.id.itemPic);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(this.datas.get(position).getName());


        //加载图片
        holder.name.setText(this.datas.get(position).getName());
        String urlImag = UrlUtils.IMG_URL + this.datas.get(position).getPic();
        ImageLoaderUtil.display(urlImag, holder.pic);

        return view;
    }

    public static class ViewHolder {

        public TextView name;
        public ImageView pic;

    }

}
