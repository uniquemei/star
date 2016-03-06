package com.fly.jiejing.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Administrator on 2015/10/1.首页的图片加载器
 */
public class ImagePgAdapter extends PagerAdapter {
    private List<ImageView> data;

    public ImagePgAdapter(List<ImageView> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
//       return this.data.size();
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0==arg1;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
  //      super.destroyItem(container, position, object);
        position = position % data.size();
        container.removeView(this.data.get(position));
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    //    ((ViewPager)container).addView(this.data.get(position));
        position = position % data.size();
        ViewGroup vg = ((ViewGroup)this.data.get(position).getParent());

        try{
            if(vg!=null){
                vg.removeView(this.data.get(position));
            }
            container.addView(this.data.get(position),0);
        }catch (Exception e) {
            // handler something
        }
        return this.data.get(position);

    }
}
