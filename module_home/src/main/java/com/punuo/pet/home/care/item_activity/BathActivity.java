package com.punuo.pet.home.care.item_activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.StatusBarUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = HomeRouter.ROUTER_CARE_BATH_ACTIVITY)
public class BathActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;
    @BindView(R2.id.set_bath_time)
    RelativeLayout setBathTime;
    @BindView(R2.id.set_bath_alarm)
    RelativeLayout setBathAlarm;
    @BindView(R2.id.set_bath_repeat)
    RelativeLayout setBathRepeat;
    @BindView(R2.id.set_bath_pet)
    RelativeLayout setBathPet;
    @BindView(R2.id.date_select)
    TextView dateSelectText;

    Calendar cal = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    long dateAndTime;
    String dataSelect;
    String timeSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_bathe);
        ButterKnife.bind(this);
        View mStatusBar = findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        initView();
    }

    public void initView() {
        title.setText("日常护理");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
        subTitle.setText("保存");
        setBathTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
                dateSelectText.setText(dataSelect+" "+timeSelect);
            }
        });
    }

    public void setDate() {
        DatePickerDialog dpd = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            //点击确定时触发
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                dataSelect = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,monthOfYear+1);
                setTime();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }
    public void setTime(){
        new TimePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String h = hourOfDay<10 ? "0"+hourOfDay:""+hourOfDay;
                String m = minute<10 ? "0"+minute:""+minute;
                timeSelect = h+":"+m;
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.MILLISECOND,0);
                dateAndTime = calendar.getTimeInMillis();
            }
        },cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
