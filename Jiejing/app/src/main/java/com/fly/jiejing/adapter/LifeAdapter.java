package com.fly.jiejing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Life;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.UrlUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/21.生活的加载器
 */
public class LifeAdapter extends BaseAdapter {
    private List<Life> mData;
    private Context mContext;

    public LifeAdapter(List<Life> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
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
        viewHolder vHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.three_life_item_layout, null);
            vHolder = new viewHolder();
            vHolder.life_item_con = (TextView) convertView.findViewById(R.id.life_item_con);
            vHolder.life_item_time = (TextView) convertView.findViewById(R.id.life_item_time);
            vHolder.life_item_img = (ImageView) convertView.findViewById(R.id.life_item_img);
            convertView.setTag(vHolder);
        }else{
            vHolder=(viewHolder)convertView.getTag();
        }
        //捆绑数据
        Life life=mData.get(position);
        vHolder.life_item_time.setText(life.getTime());
        vHolder.life_item_con.setText(life.getTitle());
        //网上加载图片
        String url= UrlUtils.IMG_URL+life.getImg();
        ImageLoaderUtil.display(url,vHolder.life_item_img);
        return convertView;
    }

    public class viewHolder {
        private TextView life_item_time;
        private ImageView life_item_img;
        private TextView life_item_con;
    }
}
