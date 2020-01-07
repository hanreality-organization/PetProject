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
import com.punuo.pet.home.care.request.GetOuterInfoRequest;
import com.punuo.pet.home.care.request.SaveBathRequest;
import com.punuo.pet.home.care.request.SaveOuterRequest;
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

@Route(path = HomeRouter.ROUTER_CARE_IN_VITRO_ACTIVITY)
public class DewormingVitroActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.status_bar)
    View statusBar;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;
    @BindView(R2.id.outer_time_text)
    TextView outerTimeText;
    @BindView(R2.id.set_vitro_time)
    RelativeLayout setVitroTime;
    @BindView(R2.id.outer_alarm_text)
    TextView outerAlarmText;
    @BindView(R2.id.set_vitro_alarm)
    RelativeLayout setVitroAlarm;
    @BindView(R2.id.outer_repeat_text)
    TextView outerRepeatText;
    @BindView(R2.id.set_vitro_repeat)
    RelativeLayout setVitroRepeat;
    @BindView(R2.id.outer_pet_name)
    TextView outerPetName;
    @BindView(R2.id.set_vitro_pet)
    RelativeLayout setVitroPet;

    private final String type = "体外驱虫";
    private static final String TAG = "outer";
    Calendar cal = Calendar.getInstance();
    private static Calendar calendar = Calendar.getInstance();
    private static long outerDateAndTime;
    private static String dateText;
    private static String timeText;
    private PopupWindow mPopupWindow;
    private PopupWindow rPopuoWindow;
    private PopupWindow pPopupWindow;
    private RecyclerView recyclerView;
    public static AlarmManager outerAlarmManager;
    public static PendingIntent outerPendingIntent;
    private static List<PetData> mPetNameList;
    private HashMap<String,PendingIntent> hashMap = new HashMap<>();
    public static int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_deworming_vitro);
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
                saveOuterInfo();
                setOuterAlarm(outerDateAndTime);
            }
        });
        setVitroTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        setVitroAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmPopupWindow();
            }
        });
        setVitroRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatPopupWindow();
            }
        });
        setVitroPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetManager.getPetInfo();
                showPetPopupWindow();
            }
        });
    }
    public void initData(){
        getOuterInfo(Constant.petData.petname);
    }

    public void setDate(){
        DatePickerDialog dpd = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            //点击确定时触发
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateText = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                setTime();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    public void setTime(){
        new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String h = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String m = minute < 10 ? "0" + minute : "" + minute;
                timeText = h + ":" + m;
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.MILLISECOND, 0);
                outerDateAndTime = calendar.getTimeInMillis();
                Constant.vitroDateAndTime = outerDateAndTime;
                outerTimeText.setText(dateText + " " + timeText);
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
                outerAlarmText.setText("提醒");
                mPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outerAlarmText.setText("不提醒");
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
                outerRepeatText.setText("每周");
                rPopuoWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outerRepeatText.setText("每两周");
                rPopuoWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outerRepeatText.setText("每月");
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
                outerPetName.setText(petName);
                getOuterInfo(petName);
                pPopupWindow.dismiss();
            }
        });
        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    private SaveOuterRequest mSaveOuterRequest;
    public void saveOuterInfo(){
        if (mSaveOuterRequest != null && !mSaveOuterRequest.isFinish()) {
            return;
        }
        mSaveOuterRequest = new SaveOuterRequest();
        mSaveOuterRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveOuterRequest.addUrlParam("type",type);
        mSaveOuterRequest.addUrlParam("time", outerTimeText.getText().toString());
        mSaveOuterRequest.addUrlParam("remind",outerAlarmText.getText().toString());
        mSaveOuterRequest.addUrlParam("period",outerRepeatText.getText().toString());
        mSaveOuterRequest.addUrlParam("petname",outerPetName.getText().toString());
        mSaveOuterRequest.setRequestListener(new RequestListener<BaseModel>() {
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
        HttpManager.addRequest(mSaveOuterRequest);
    }

    private GetOuterInfoRequest mGetOuterInfoRequest;
    public void getOuterInfo(final String petName){
        if (mGetOuterInfoRequest!=null&&!mGetOuterInfoRequest.isFinish()){
            return;
        }
        mGetOuterInfoRequest = new GetOuterInfoRequest();
        mGetOuterInfoRequest.addUrlParam("username",AccountManager.getUserName());
        mGetOuterInfoRequest.addUrlParam("petname",petName);
        mGetOuterInfoRequest.addUrlParam("type",type);
        mGetOuterInfoRequest.setRequestListener(new RequestListener<AlarmInfoModel>() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onSuccess(AlarmInfoModel result) {
                outerTimeText.setText(result.time);
                outerAlarmText.setText(result.remind);
                outerRepeatText.setText(result.period);
                outerPetName.setText(petName);
            }
            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetOuterInfoRequest);
    }

    public void setOuterAlarm(long targetTime){
        String key = String.valueOf(targetTime);
        PendingIntent targetIntent = hashMap.get(key);
        int requestCode = key.hashCode();
        if (targetIntent!=null){
            targetIntent.cancel();
            hashMap.remove(key);
        }
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_FIVE);
        outerPendingIntent = PendingIntent.getBroadcast(this,requestCode,intent,0);
        outerAlarmManager = (AlarmManager) PnApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        if(outerAlarmText.getText().toString().equals("提醒")&&outerRepeatText.getText().toString().equals("每周")){
            day = 7;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                outerAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,outerPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                outerAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,outerPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                outerAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,outerPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (outerAlarmText.getText().toString().equals("提醒")&&outerRepeatText.getText().toString().equals("每两周")){
            day=14;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                outerAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,outerPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                outerAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,outerPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                outerAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,outerPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (outerAlarmText.getText().toString().equals("提醒")&&outerRepeatText.getText().toString().equals("每月")){
            day = 30;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                outerAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,outerPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                outerAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,outerPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                outerAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,outerPendingIntent);
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
