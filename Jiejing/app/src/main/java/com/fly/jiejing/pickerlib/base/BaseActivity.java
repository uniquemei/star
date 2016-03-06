package com.fly.jiejing.pickerlib.base;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.fly.jiejing.pickerlib.util.WindowUtil;

import net.tsz.afinal.FinalActivity;

/**
 * Activity的基类，实现基本的自动缩放
 */
public class BaseActivity extends FragmentActivity {
    private View mContentView;

    @Override
    public void setContentView(int layoutResID) {
        mContentView = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(mContentView);
    }

    @Override
    public void setContentView(View view) {
        resizeView(view);
        super.setContentView(view);
        FinalActivity.initInjectedView(this);
    }

    public View getContentView() {
        return mContentView;
    }

    /**
     * 重新计算View及子View的宽高、边距
     * 
     * @param view
     */
    public void resizeView(View view) {

        //WindowUtil.resizeRecursively(view);
    }

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }
}
