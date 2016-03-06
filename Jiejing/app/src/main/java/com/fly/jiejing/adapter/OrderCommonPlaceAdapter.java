package com.fly.jiejing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.UrlUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/5.下订单的时候展示的常用地址
 */
public class OrderCommonPlaceAdapter extends BaseAdapter {
    private List<Cleaner> mData;
    private Context mContent;
    private onPeopleSelectedChangeListener listener;

    public void setListener(onPeopleSelectedChangeListener listener) {
        this.listener = listener;
    }

    public OrderCommonPlaceAdapter(Context mContent, List<Cleaner> mData) {
        this.mContent = mContent;
        this.mData = mData;
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

    //重用 常用地址列表的布局
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder vHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContent);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.main_order_place_layout_item, null);
            vHolder = new viewHolder();
 //           vHolder.c1 = (TextView) convertView.findViewById(R.id.addressNumTitle);
            vHolder.c2 = (TextView) convertView.findViewById(R.id.addressNum);
            vHolder.c3 = (TextView) convertView.findViewById(R.id.addressCommonTitle);
            vHolder.c4 = (TextView) convertView.findViewById(R.id.addressCommon);
            convertView.setTag(vHolder);
        } else {
            vHolder = (viewHolder) convertView.getTag();
        }
        //捆绑数据
        Cleaner p = mData.get(position);
 //       vHolder.c1.setText("编号：");
        vHolder.c2.setText("编号："+p.getCleanerid());
        vHolder.c3.setText("名字：");
        vHolder.c4.setText(p.getName());
        return convertView;

    }
    public class viewHolder {
        private TextView c1;
        private TextView c2;
        private TextView c3;
        private TextView c4;
    }

    public interface onPeopleSelectedChangeListener {
        public void onPeopleSelectedChange(Cleaner p);
    }

}
