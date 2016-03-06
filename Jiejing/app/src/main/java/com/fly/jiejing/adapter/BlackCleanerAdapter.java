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
 * Created by Administrator on 2015/10/5.常用人员和黑名单都用到了
 */
public class BlackCleanerAdapter extends BaseAdapter {
    private List<Cleaner> mData;
    private Context mContent;
    private onPeopleSelectedChangeListener listener;

    public void setListener(onPeopleSelectedChangeListener listener) {
        this.listener = listener;
    }

    public BlackCleanerAdapter(Context mContent, List<Cleaner> mData) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder vHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContent);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.my_common_user_item, null);
            vHolder = new viewHolder();
            vHolder.imageView = (ImageView) convertView.findViewById(R.id.common_user_imag);
            vHolder.commanPeopleName = (TextView) convertView.findViewById(R.id.common_user_name);
            vHolder.commanPeopleGrade = (TextView) convertView.findViewById(R.id.common_user_grade);
            vHolder.commanPeopleCount = (TextView) convertView.findViewById(R.id.user_service_count);
            vHolder.comman_user_id=(TextView)convertView.findViewById(R.id.common_user_id);
            vHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(vHolder);
        } else {
            vHolder = (viewHolder) convertView.getTag();
        }
        //捆绑数据
        final Cleaner p = mData.get(position);
        vHolder.commanPeopleName.setText("姓名:"+p.getName());
        vHolder.commanPeopleGrade.setText("类型："+p.getType());
        vHolder.comman_user_id.setText("编号："+p.getCleanerid());
        vHolder.commanPeopleCount.setText("服务：" + p.getCount()+"次");
        //防止checkBox重用
        if (p.isSelect()) {
            vHolder.checkBox.setChecked(true);
        } else {
            vHolder.checkBox.setChecked(false);
        }
        vHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                p.setIsSelect(c.isChecked());
                if (listener != null) {
                    listener.onPeopleSelectedChange(p);
                }
            }
        });
        // 常用阿姨网络获取头像
        String urlImag = UrlUtils.IMG_URL + p.getPic();
        ImageLoaderUtil.display(urlImag, vHolder.imageView);
        return convertView;
    }

    public class viewHolder {
        private ImageView imageView;
        private TextView commanPeopleName;
        private TextView commanPeopleGrade;
        private TextView commanPeopleCount;
        private TextView comman_user_id;
        private CheckBox checkBox;
    }

    public interface onPeopleSelectedChangeListener {
        public void onPeopleSelectedChange(Cleaner p);
    }

}
