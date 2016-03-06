package com.fly.jiejing.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fly.jiejing.R;
public class UserLoginActivity extends FragmentActivity implements View.OnClickListener {
    private RadioGroup radioGroup;
    private RadioButton login,sign;
    private ImageView back;
    private Fragment[] fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        setFragment();
    }

    private void setFragment() {
        fragment=new Fragment[2];
        fragmentManager=getSupportFragmentManager();
        fragment[0]=fragmentManager.findFragmentById(R.id.fragment_select_0);
        fragment[1]=fragmentManager.findFragmentById(R.id.fragment_select_2);
        fragmentTransaction = fragmentManager.beginTransaction()
                .hide(fragment[0]).hide(fragment[1]);//
        fragmentTransaction.show(fragment[0]).commit();
        setFragmentIndicator();
    }

    private void setFragmentIndicator(){
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        login=(RadioButton)findViewById(R.id.rb_login);
        sign=(RadioButton)findViewById(R.id.rb_sign);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction=fragmentManager.beginTransaction()
                        .hide(fragment[0]).hide(fragment[1]);
                switch (checkedId){
                    case R.id.rb_login:
                        fragmentTransaction.show(fragment[0]).commit();
                        break;
                    case R.id.rb_sign:
                        fragmentTransaction.show(fragment[1]).commit();
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}