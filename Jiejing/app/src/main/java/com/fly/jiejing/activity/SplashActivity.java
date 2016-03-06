package com.fly.jiejing.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.fly.jiejing.R;
import com.fly.jiejing.units.Constant;

public class SplashActivity extends Activity {
    private Handler mhandler;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mhandler = new Handler();
        sp = getSharedPreferences(Constant.SP_FILE_NAME, MODE_PRIVATE);
        Boolean flag = sp.getBoolean("isFirst", true);
//        if (flag) {
//            mhandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(SplashActivity.this, DaoHangActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }, 1500);
//        } else {
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 1500);

    }
}
