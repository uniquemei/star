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
import com.fly.jiejing.entity.Order;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.UrlUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/5.清洁工的评价列表
 */
public class CleanerCommentAdapter extends BaseAdapter {
    private List<Order> mData;
    private Context mContent;
    private onPeopleSelectedChangeListener listener;

    public void setListener(onPeopleSelectedChangeListener listener) {
        this.listener = listener;
    }

    public CleanerCommentAdapter(Context mContent, List<Order> mData) {
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
        viewHolder vHolder;
        LayoutInflater inflater = LayoutInflater.from(mContent);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cleaner_detail_item, null);
            vHolder = new viewHolder();
            vHolder.imageView = (ImageView) convertView.findViewById(R.id.cleaner_detail_userImg);
            vHolder.name = (TextView) convertView.findViewById(R.id.cleaner_detail_commentuser_name);
            vHolder.score = (TextView) convertView.findViewById(R.id.cleaner_detail_score);
            vHolder.comment = (TextView) convertView.findViewById(R.id.cleaner_detail_comment);
            vHolder.time = (TextView) convertView.findViewById(R.id.cleaner_detail_commenttime);
            convertView.setTag(vHolder);
        } else {
            vHolder = (viewHolder) convertView.getTag();
        }
        //捆绑数据
        final Order p = mData.get(position);
        String name=p.getUser_name().substring(0,6)+"****";
        vHolder.name.setText("用户："+name);

        vHolder.score.setText("评分："+p.getScore());
        vHolder.comment.setText("             "+p.getComment()+"");
        vHolder.time.setText(p.getTime()+"");
        // 常用阿姨网络获取头像
        String urlImag = UrlUtils.IMG_URL + p.getUserImg();
        ImageLoaderUtil.display(urlImag, vHolder.imageView);

        return convertView;
    }

    public class viewHolder {
        private ImageView imageView;
        private TextView name;
        private TextView score;
        private TextView comment;
        private TextView time;
    }

    public interface onPeopleSelectedChangeListener {
        public void onPeopleSelectedChange(Cleaner p);
    }

}
