package yu.cleaner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import yu.cleaner.R;


public class SplashActivity extends Activity {
    private Handler mhandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mhandler = new Handler();
            mhandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
      //              intent.putExtra("",)
                    startActivity(intent);
                    finish();
                }
            }, 1500);



    }
}
