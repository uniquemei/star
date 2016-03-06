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
import com.fly.jiejing.entity.Items;
import com.fly.jiejing.request.ImageLoaderUtil;
import com.fly.jiejing.units.UrlUtils;

import java.util.List;

/**
 * Created by Administrator on 2015/10/5.
 */
public class RankItemsAdapter extends BaseAdapter {
    private List<Items> mData;
    private Context mContent;
    private onPeopleSelectedChangeListener listener;

    public void setListener(onPeopleSelectedChangeListener listener) {
        this.listener = listener;
    }
    public RankItemsAdapter(Context mContent, List<Items> mData) {
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

            convertView.setTag(vHolder);
        } else {
            vHolder = (viewHolder) convertView.getTag();
        }
        //捆绑数据
        final Items p = mData.get(position);
        vHolder.two_cleanerid.setText(p.getName() + "");
        vHolder.two_cleanerName.setText(p.getPrice() + "");
        vHolder.two_cleanNum.setText(p.getNum()+"次");

        vHolder.two_id.setText("名称：");
        vHolder.two_content.setText("");
        vHolder.two_num.setText("");
        //防止checkBox重用

        vHolder.saveCleaner.setVisibility(View.GONE);

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
    }

    public interface onPeopleSelectedChangeListener {
        public void onPeopleSelectedChange(Items p);
    }

}
