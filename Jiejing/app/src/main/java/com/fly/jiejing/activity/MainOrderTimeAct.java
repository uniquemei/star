package com.fly.jiejing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fly.jiejing.R;
import com.fly.jiejing.pickerlib.TimePicker;
import com.fly.jiejing.pickerlib.base.BaseActivity;
import com.fly.jiejing.pickerlib.TimePicker.TimePickerListener;
import net.tsz.afinal.annotation.view.ViewInject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import datetime.DateTime;

public class MainOrderTimeAct extends BaseActivity implements TimePickerListener {

    @ViewInject(id = R.id.time_picker)
    private TimePicker mTimePicker;
    private DateTime dateTime;
    TextView confirmTime;
    ImageView backTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_order_time);
        dateTime = new DateTime();
        confirmTime = (TextView) findViewById(R.id.confirmTimeBack);
        backTime = (ImageView) findViewById(R.id.backTime);

        mTimePicker.setTimePickerListener(this);

        confirmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ArrayList<String> s = new ArrayList<String>();
                s.add(dateTime.getYear() + "-" + dateTime.getMonth() + "-" + dateTime.getDay());
                if (dateTime.getMinute() == 0) {
                    if (dateTime.getHour() < 10) {
                        s.add("0" + dateTime.getHour() + ":" + "00");
                    } else {
                        s.add(dateTime.getHour() + ":" + "00");
                    }
                } else {
                    if (dateTime.getHour() < 10) {
                        s.add("0" + dateTime.getHour() + ":" + dateTime.getMinute());
                    } else {
                        s.add(dateTime.getHour() + ":" + dateTime.getMinute());
                    }
                }
                intent.putStringArrayListExtra("ORDERTIME", s);
                setResult(2, intent);
                finish();
            }
        });
        backTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String getDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


    //弹出框
    @Override
    public void onPick(DateTime time) {
        dateTime = time;
        //Toast.makeText(getBaseContext(), "已选时间：" + time.toString(), Toast.LENGTH_SHORT).show();
    }
}