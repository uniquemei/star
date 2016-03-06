package com.fly.jiejing.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fly.jiejing.R;
import com.fly.jiejing.adapter.FragmentPgAdapter;
import com.fly.jiejing.fragment.MainFragment;
import com.fly.jiejing.fragment.FourFragment;

import com.fly.jiejing.fragment.ThreeFragment;
import com.fly.jiejing.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//主页面RadioGrop+ViewPager+Fragment
public class MainActivity extends FragmentActivity {


    private RadioGroup bottom;
    private ViewPager myPager;
    public static List<Fragment> fragments;
    private FragmentPgAdapter fragmentPgAdapter;
    private static Boolean isExit = false;//回退两次退出应用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        initView();
        addFragment();
        setView();
    }

    //初始化数据
    public void initView() {
        bottom = (RadioGroup) findViewById(R.id.bottom);
        myPager = (ViewPager) findViewById(R.id.myPager);
        fragments = new ArrayList<>();

    }

    //加载Fragment数据
    public void addFragment() {

        Fragment fragment = new MainFragment();
        fragments.add(fragment);

        fragment = new TwoFragment();
        fragments.add(fragment);

        fragment = new ThreeFragment();
        fragments.add(fragment);

        fragment = new FourFragment();
        fragments.add(fragment);

        fragmentPgAdapter = new FragmentPgAdapter(getSupportFragmentManager(), fragments);
        myPager.setAdapter(fragmentPgAdapter);
    }

    //监听事件
    public void setView() {
        myPager.setCurrentItem(0);
        ((RadioButton) bottom.getChildAt(0)).setChecked(true);
        bottom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        myPager.setCurrentItem(i);
                        if (i == 1) {
                            ((TwoFragment) fragments.get(1)).initCleaner();
                        }
                    }
                }
            }
        });
        myPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override//状态已经改变
            public void onPageSelected(int i) {

                for (int j = 0; j < bottom.getChildCount(); j++) {
                    if (j == i) {
                        ((RadioButton) bottom.getChildAt(j)).setChecked(true);
                    } else {
                        ((RadioButton) bottom.getChildAt(j)).setChecked(false);
                    }
                }
            }
            //正在滚动中
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    /**
     *点击两次退出应用
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK);
        {
            exitBy2Click();
        }
        return false;
    }
    private void exitBy2Click() {
        // TODO Auto-generated method stub
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        }
        else {
            finish();
            System.exit(0);
        }
    }
}
