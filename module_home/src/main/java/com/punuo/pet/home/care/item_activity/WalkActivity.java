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
import com.punuo.pet.home.care.request.GetWalkInfoRequest;
import com.punuo.pet.home.care.request.SaveBathRequest;
import com.punuo.pet.home.care.request.SaveWalkRequest;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


@Route(path = HomeRouter.ROUTER_CARE_WALK_ACTIVITY)
public class WalkActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.status_bar)
    View statusBar;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;
    @BindView(R2.id.walk_time_text)
    TextView walkTimeText;
    @BindView(R2.id.set_walk_time)
    RelativeLayout setWalkTime;
    @BindView(R2.id.walk_alarm_text)
    TextView walkAlarmText;
    @BindView(R2.id.set_walk_alarm)
    RelativeLayout setWalkAlarm;
    @BindView(R2.id.walk_repeat_text)
    TextView walkRepeatText;
    @BindView(R2.id.set_walk_repeat)
    RelativeLayout setWalkRepeat;
    @BindView(R2.id.walk_pet_name)
    TextView walkPetName;
    @BindView(R2.id.set_walk_pet)
    RelativeLayout setWalkPet;
    private static final String TAG = "walk";
    private final String type = "遛宠";
    Calendar cal = Calendar.getInstance();
    private static Calendar calendar = Calendar.getInstance();
    private static long walkDateAndTime;
    private static String dataText;
    private static String timeText;
    private PopupWindow mPopupWindow;
    private PopupWindow rPopuoWindow;
    private PopupWindow pPopupWindow;
    private RecyclerView recyclerView;
    public static AlarmManager walkAlarmManager;
    public static PendingIntent walkPendingIntent;
    private static List<PetData> mPetNameList;
    private HashMap<String, PendingIntent> hashMap = new HashMap<>();
    public static int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_walk);
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
        initData();
        subTitle.setVisibility(View.VISIBLE);
        subTitle.setText("保存");
        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWalkInfo();
                setWalkAlarm(walkDateAndTime);
            }
        });

        setWalkTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        setWalkAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmPopupWindow();
            }
        });
        setWalkRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatPopupWindow();
            }
        });
        setWalkPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetManager.getPetInfo();
                showPetPopupWindow();
            }
        });
    }

    public void initData() {
        getWalkInfo(Constant.petData.petname);
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
                walkDateAndTime = calendar.getTimeInMillis();
                Constant.walkDateAndTime = walkDateAndTime;
                walkTimeText.setText(dataText + " " + timeText);
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
                walkAlarmText.setText("提醒");
                mPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkAlarmText.setText("不提醒");
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
                walkRepeatText.setText("每周");
                rPopuoWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkRepeatText.setText("每两周");
                rPopuoWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walkRepeatText.setText("每月");
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
                walkPetName.setText(petName);
                getWalkInfo(petName);
                pPopupWindow.dismiss();
            }
        });
        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    private SaveWalkRequest mSaveWalkRequest;
    public void saveWalkInfo(){
        if (mSaveWalkRequest != null && !mSaveWalkRequest.isFinish()) {
            return;
        }
        mSaveWalkRequest = new SaveWalkRequest();
        mSaveWalkRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveWalkRequest.addUrlParam("type",type);
        mSaveWalkRequest.addUrlParam("time", walkTimeText.getText().toString());
        mSaveWalkRequest.addUrlParam("remind",walkAlarmText.getText().toString());
        mSaveWalkRequest.addUrlParam("period",walkRepeatText.getText().toString());
        mSaveWalkRequest.addUrlParam("petname",walkPetName.getText().toString());
        mSaveWalkRequest.setRequestListener(new RequestListener<BaseModel>() {
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
        HttpManager.addRequest(mSaveWalkRequest);
    }

    private GetWalkInfoRequest mGetWalkInfoRequest;
    public void getWalkInfo(final  String petName){
        if (mGetWalkInfoRequest!=null&&!mGetWalkInfoRequest.isFinish()){
            return;
        }
        mGetWalkInfoRequest = new GetWalkInfoRequest();
        mGetWalkInfoRequest.addUrlParam("username",AccountManager.getUserName());
        mGetWalkInfoRequest.addUrlParam("petname",petName);
        mGetWalkInfoRequest.addUrlParam("type",type);
        mGetWalkInfoRequest.setRequestListener(new RequestListener<AlarmInfoModel>() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onSuccess(AlarmInfoModel result) {
                walkTimeText.setText(result.time);
                walkAlarmText.setText(result.remind);
                walkRepeatText.setText(result.period);
                walkPetName.setText(petName);
            }
            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetWalkInfoRequest);
    }

    public void setWalkAlarm(long targetTime){
        String key = String.valueOf(targetTime);
        PendingIntent targetIntent = hashMap.get(key);
        int requestCode = key.hashCode();
        if (targetIntent!=null){
            targetIntent.cancel();
            hashMap.remove(key);
        }
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_EIGHT);
        walkPendingIntent = PendingIntent.getBroadcast(this,requestCode,intent,0);
        walkAlarmManager = (AlarmManager) PnApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        if(walkAlarmText.getText().toString().equals("提醒")&&walkRepeatText.getText().toString().equals("每周")){
            day = 7;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                walkAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,walkPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                walkAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,walkPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                walkAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,walkPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (walkAlarmText.getText().toString().equals("提醒")&&walkRepeatText.getText().toString().equals("每两周")){
            day=14;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                walkAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,walkPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                walkAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,walkPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                walkAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,walkPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (walkAlarmText.getText().toString().equals("提醒")&&walkRepeatText.getText().toString().equals("每月")){
            day = 30;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                walkAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,walkPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                walkAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,walkPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                walkAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,walkPendingIntent);
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
