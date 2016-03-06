package com.fly.jiejing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Address;

import java.util.List;

/**
 * Created by Administrator on 2015/10/8.常用地址适配器
 */
public class CommonPlaceAdapter extends BaseAdapter{
    private List<Address> mData;
    private Context mcontext;
    private ondeleteChangeListenner listenner;

    public void setOndeleteListenner(ondeleteChangeListenner listenner){
       this.listenner=listenner;
    }

    public CommonPlaceAdapter(List<Address> mData, Context mcontext) {
        this.mData = mData;
        this.mcontext = mcontext;
    }

    @Override
    public int getCount() {
        return this.mData.size();
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
        viewHolder vHolder=null;
        LayoutInflater inflater=LayoutInflater.from(mcontext);
        if(convertView==null){
           convertView=inflater.inflate(R.layout.my_common_place_item,null);
            vHolder=new viewHolder();
            vHolder.address=(TextView)convertView.findViewById(R.id.address);
            vHolder.deleteButton=(Button)convertView.findViewById(R.id.delete);
            convertView.setTag(vHolder);
        }else{
            vHolder=(viewHolder)convertView.getTag();
        }
        //捆绑数据
        final Address a = mData.get(position);
        vHolder.address.setText(a.getStreet()+a.getDetail());
        vHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(listenner != null){
                  listenner.deleteChangeListenner(a);
              }
            }
        });
        return convertView;
    }
    private class viewHolder{
        private TextView address;
        private Button deleteButton;
    }
    public interface ondeleteChangeListenner{
        public void deleteChangeListenner(Address a);
    }
}
