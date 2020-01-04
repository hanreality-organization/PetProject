package com.punuo.pet.home.care.item_activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = HomeRouter.ROUTER_CARE_CHECKUP_ACTIVITY)
public class CheckupActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.status_bar)
    View statusBar;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;
    @BindView(R2.id.check_time_text)
    TextView checkTimeText;
    @BindView(R2.id.set_check_time)
    RelativeLayout setCheckTime;
    @BindView(R2.id.check_alarm_text)
    TextView checkAlarmText;
    @BindView(R2.id.set_check_alarm)
    RelativeLayout setCheckAlarm;
    @BindView(R2.id.check_repeat_text)
    TextView checkRepeatText;
    @BindView(R2.id.set_check_repeat)
    RelativeLayout setCheckRepeat;
    @BindView(R2.id.check_petname_text)
    TextView checkPetnameText;
    @BindView(R2.id.set_check_pet)
    RelativeLayout setCheckPet;

    //两个工具calendar
    private Calendar calendar = Calendar.getInstance();
    Calendar cd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_checkup);
        ButterKnife.bind(this);
        View mStatusBar = findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        initView();
        EventBus.getDefault().register(this);
    }

    public void initView(){
        title.setText("日常护理");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
        subTitle.setText("保存");
        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 保存所有数据并设置相应的闹钟
            }
        });
        setCheckTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置时间
            }
        });
        setCheckAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置是否提醒
            }
        });
        setCheckRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置重复周期
            }
        });
        setCheckPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置关联宠物
            }
        });
    }

    public void setDate(){
        DatePickerDialog dpd = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                checkTimeText.setText(year+"-"+month+1+"-"+dayOfMonth);
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                setTime();
            }
        },cd.get(Calendar.YEAR),cd.get(Calendar.MONTH),cd.get(Calendar.DAY_OF_MONTH));
    }

    public void setTime(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
