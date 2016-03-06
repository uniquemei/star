package com.fly.jiejing.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.entity.Cleaner;
import com.fly.jiejing.entity.User;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.Constant;
import com.fly.jiejing.units.MainApplication;
import com.fly.jiejing.units.UrlUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/5.清洁人员的等级
 */
public class RankCleanerAdapter extends BaseAdapter {
    private List<Cleaner> mData;
    private Context mContent;
    private onSaveListener listener;

    public void setListener(onSaveListener listener) {
        this.listener = listener;
    }

    public RankCleanerAdapter(Context mContent, List<Cleaner> mData) {
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
        final viewHolder vHolder;
        LayoutInflater inflater = LayoutInflater.from(mContent);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.two_cleaner_item, null);
            vHolder = new viewHolder();
            vHolder.two_img = (ImageView) convertView.findViewById(R.id.two_img);
            vHolder.two_cleanerid = (TextView) convertView.findViewById(R.id.two_cleanerid);
            vHolder.two_cleanerName = (TextView) convertView.findViewById(R.id.two_cleanerName);
            vHolder.two_cleanNum = (TextView) convertView.findViewById(R.id.two_cleanNum);
            vHolder.saveCleaner = (CheckBox) convertView.findViewById(R.id.two_saveCleaner);
            vHolder.two_id = (TextView) convertView.findViewById(R.id.two_id);
            vHolder.two_content = (TextView) convertView.findViewById(R.id.two_content);
            vHolder.two_num = (TextView) convertView.findViewById(R.id.two_num);
            vHolder.two_type = (TextView) convertView.findViewById(R.id.two_type);

            convertView.setTag(vHolder);
        } else {
            vHolder = (viewHolder) convertView.getTag();
        }
        //捆绑数据
        final Cleaner p = mData.get(position);
        vHolder.two_id.setText("编号：");
        vHolder.two_content.setText("姓名：");
        vHolder.two_num.setText("服务：");
        vHolder.two_type.setVisibility(View.VISIBLE);
        vHolder.two_type.setText("类型："+p.getType());
        vHolder.two_cleanerid.setText(p.getCleanerid() + "");
        vHolder.two_cleanerName.setText(p.getName() + "");
        vHolder.two_cleanNum.setText(p.getCount() + "次");
        //防止checkBox重用
        vHolder.saveCleaner.setVisibility(View.GONE);
        final User user = MainApplication.getApplication().getUser();
        //用户登陆后显示按钮
        if (null != user && user.getUser_id().length() > 0) {
            vHolder.saveCleaner.setVisibility(View.VISIBLE);
            if (p.getSave() == 0) {
                vHolder.saveCleaner.setText("收藏");
                vHolder.saveCleaner.setTextColor(Color.BLACK);
            } else if (p.getSave() == 1) {
                vHolder.saveCleaner.setText("已收藏");
                vHolder.saveCleaner.setTextColor(Color.GRAY);
            }else if (p.getSave() == 2) {
                vHolder.saveCleaner.setText("黑名单");
                vHolder.saveCleaner.setTextColor(Color.GRAY);
            }
        }
        vHolder.saveCleaner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox c = (CheckBox) v;
                p.setIsSelect(c.isChecked());
                p.setUserid(user.getUser_id());
                if (listener != null && p.getSave() == 0) {//没有收藏时让它被收藏
                    listener.onSave(p);
                    p.setSave(1);

                }
            }
        });
        // 常用阿姨网络获取头像
        String urlImag = UrlUtils.IMG_URL + p.getPic();
        ImageLoaderUtil.display(urlImag, vHolder.two_img);
        return convertView;
    }

    public class viewHolder {
        private ImageView two_img;
        private TextView two_cleanerid;
        private TextView two_cleanerName;
        private TextView two_cleanNum;
        private CheckBox saveCleaner;
        TextView two_id;
        TextView two_content;
        TextView two_num;
        TextView two_type;
    }

    public interface onSaveListener {
        public void onSave(Cleaner p);
    }

}
