package com.fly.jiejing.pickerlib.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import net.tsz.afinal.FinalActivity;

/**
 * 自定义控件的基类
 */
abstract public class BaseLayout extends RelativeLayout {
    protected View mContentView;

    public BaseLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public BaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public BaseLayout(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        setContentView(onInitLayoutResId());
        FinalActivity.initInjectedView(this, this);
        onInit();
    }

    private void setContentView(int layoutResId) {
        LayoutInflater.from(getContext()).inflate(layoutResId, this, true);
        mContentView = getChildAt(0);
    }

    abstract protected int onInitLayoutResId();

    protected void onInit() {
    }

    public void show() {
        show(this);
    }

    public void hide() {
        hide(this);
    }

    public void show(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void hide(View v) {
        v.setVisibility(View.GONE);
    }

    public void invisible(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setBackgroundResource(int resid) {
        mContentView.setBackgroundResource(resid);
    }

    /**
     * 重新计算View及子View的宽高、边距
     * 
     * @param view
     */
    public void resizeView(View view) {
 //       WindowUtil.resizeRecursively(view);----------2
    }

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }

    public void onDestroy() {
        this.removeAllViews();
        mContentView = null;
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void clear() {
    }

    public void reset() {
    }

    public void onShow() {

    }
}
