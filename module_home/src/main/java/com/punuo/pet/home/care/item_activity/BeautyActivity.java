package com.punuo.pet.home.care.item_activity;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.PetManager;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.care.adapter.PetRelevanceAdapter;
import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.pet.home.care.request.GetBathInfoRequest;
import com.punuo.pet.home.care.request.GetBeautyInfoRequest;
import com.punuo.pet.home.care.request.SaveBathRequest;
import com.punuo.pet.home.care.request.SaveBeautyRequest;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.PnApplication;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = HomeRouter.ROUTER_CARE_BEAUTY_ACTIVITY)
public class BeautyActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.status_bar)
    View statusBar;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;
    @BindView(R2.id.beauty_time_text)
    TextView beautyTimeText;
    @BindView(R2.id.set_beauty_time)
    RelativeLayout setBeautyTime;
    @BindView(R2.id.beauty_alarm_text)
    TextView beautyAlarmText;
    @BindView(R2.id.set_beauty_alarm)
    RelativeLayout setBeautyAlarm;
    @BindView(R2.id.beauty_repeat_text)
    TextView beautyRepeatText;
    @BindView(R2.id.set_beauty_repeat)
    RelativeLayout setBeautyRepeat;
    @BindView(R2.id.beauty_pet_name)
    TextView beautyPetName;
    @BindView(R2.id.set_beauty_pet)
    RelativeLayout setBeautyPet;

    private static final String TAG = "beauty";
    private final String type = "美容护理";
    Calendar cal = Calendar.getInstance();
    private static Calendar calendar = Calendar.getInstance();
    private static long beautyDateAndTime;
    private static String dataText;
    private static String timeText;
    private PopupWindow mPopupWindow;
    private PopupWindow rPopuoWindow;
    private PopupWindow pPopupWindow;
    private RecyclerView recyclerView;
    public static AlarmManager beautyAlarmManager;
    public static PendingIntent beautyPendingIntent;
    private static List<PetData> mPetNameList;
    private HashMap<String,PendingIntent> hashMap = new HashMap<>();
    public static int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_beauty);
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

    public void initView() {
        title.setText("日常护理");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
        initData();
        subTitle.setVisibility(View.VISIBLE);
        subTitle.setText("保存");
        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBeautyInfo();
                setBeautyAlarm(beautyDateAndTime);
            }
        });

        setBeautyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        setBeautyAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmPopupWindow();
            }
        });
        setBeautyRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatPopupWindow();
            }
        });
        setBeautyPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetManager.getPetInfo();
                showPetPopupWindow();
            }
        });
    }

    public void initData() {
        getBeautyInfo(Constant.petData.petname);
    }

    public void setDate() {
        DatePickerDialog dpd = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            //点击确定时触发
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dataText = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                setTime();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    public void setTime() {
        new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String h = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String m = minute < 10 ? "0" + minute : "" + minute;
                timeText = h + ":" + m;
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.MILLISECOND, 0);
                beautyDateAndTime = calendar.getTimeInMillis();
                Constant.beautyDateAndTime = beautyDateAndTime;
                beautyTimeText.setText(dataText + " " + timeText);
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    public void showAlarmPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_alarm_popup, null);
        mPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        mPopupWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.bath_yes);
        TextView tv2 = (TextView) contenView.findViewById(R.id.bath_no);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beautyAlarmText.setText("提醒");
                mPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beautyAlarmText.setText("不提醒");
                mPopupWindow.dismiss();
            }
        });

        //显示PopupWindow
        mPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    public void showRepeatPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_repeat_popup, null);
        rPopuoWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        rPopuoWindow.setAnimationStyle(R.style.DialogWindowStyle);
        rPopuoWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.one_week);
        TextView tv2 = (TextView) contenView.findViewById(R.id.two_weeks);
        TextView tv3 = (TextView) contenView.findViewById(R.id.one_month);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beautyRepeatText.setText("每周");
                rPopuoWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beautyRepeatText.setText("每两周");
                rPopuoWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                beautyRepeatText.setText("每月");
                rPopuoWindow.dismiss();
            }
        });
        //显示PopupWindow
        rPopuoWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    public void showPetPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_list, null);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.bathe_recylcer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final PetRelevanceAdapter adapter = new PetRelevanceAdapter(this,mPetNameList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListenrt(new PetRelevanceAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                String petName = adapter.getPetName(position);
                Log.i("bath", petName);
                beautyPetName.setText(petName);
                getBeautyInfo(petName);
                pPopupWindow.dismiss();
            }
        });

        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    private SaveBeautyRequest mSaveBeautyRequest;
    public void saveBeautyInfo() {
        if (mSaveBeautyRequest != null && !mSaveBeautyRequest.isFinish()) {
            return;
        }
        mSaveBeautyRequest = new SaveBeautyRequest();
        mSaveBeautyRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveBeautyRequest.addUrlParam("type",type);
        mSaveBeautyRequest.addUrlParam("time", beautyDateAndTime);
        mSaveBeautyRequest.addUrlParam("remind",beautyAlarmText.getText().toString());
        mSaveBeautyRequest.addUrlParam("period",beautyRepeatText.getText().toString());
        mSaveBeautyRequest.addUrlParam("petname",beautyPetName.getText().toString());
        mSaveBeautyRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                ToastUtils.showToast(result.message);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mSaveBeautyRequest);
    }

    private GetBeautyInfoRequest mGetBeautyInfoRequest;
    public void getBeautyInfo(final String petName){
            if (mGetBeautyInfoRequest!=null&&!mGetBeautyInfoRequest.isFinish()){
                return;
            }
            mGetBeautyInfoRequest = new GetBeautyInfoRequest();
            mGetBeautyInfoRequest.addUrlParam("username",AccountManager.getUserName());
            mGetBeautyInfoRequest.addUrlParam("petname",petName);
            mGetBeautyInfoRequest.addUrlParam("type",type);
            mGetBeautyInfoRequest.setRequestListener(new RequestListener<AlarmInfoModel>() {
                @Override
                public void onComplete() {

                }
                @Override
                public void onSuccess(AlarmInfoModel result) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date = new Date(result.time);
                    beautyTimeText.setText(simpleDateFormat.format(date));
                    beautyAlarmText.setText(result.remind);
                    beautyRepeatText.setText(result.period);
                    beautyPetName.setText(petName);
                }
                @Override
                public void onError(Exception e) {

                }
            });
            HttpManager.addRequest(mGetBeautyInfoRequest);
    }

    public void setBeautyAlarm(long targetTime){
        String key = String.valueOf(targetTime);
        PendingIntent targetIntent = hashMap.get(key);
        int requestCode = key.hashCode();
        if (targetIntent!=null){
            targetIntent.cancel();
            hashMap.remove(key);
        }
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_SEVEN);
        beautyPendingIntent = PendingIntent.getBroadcast(this,requestCode,intent,0);
        beautyAlarmManager = (AlarmManager) PnApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        if(beautyAlarmText.getText().toString().equals("提醒")&&beautyRepeatText.getText().toString().equals("每周")){
            day = 7;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                beautyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,beautyPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                beautyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,beautyPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                beautyAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,beautyPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (beautyAlarmText.getText().toString().equals("提醒")&&beautyRepeatText.getText().toString().equals("每两周")){
            day=14;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                beautyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,beautyPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                beautyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,beautyPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                beautyAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,beautyPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (beautyAlarmText.getText().toString().equals("提醒")&&beautyRepeatText.getText().toString().equals("每月")){
            day = 30;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                beautyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,beautyPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                beautyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,beautyPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                beautyAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,beautyPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PetModel petModel) {
        mPetNameList = new ArrayList<>();
        if (mPetNameList!=null){
            mPetNameList.clear();
        }
        mPetNameList.addAll(petModel.mPets);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
