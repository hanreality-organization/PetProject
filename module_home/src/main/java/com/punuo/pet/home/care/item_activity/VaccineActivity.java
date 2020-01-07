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
import com.punuo.pet.home.care.request.GetBuyInfoRequest;
import com.punuo.pet.home.care.request.GetVaccinreInfoRequest;
import com.punuo.pet.home.care.request.SaveBuyRequest;
import com.punuo.pet.home.care.request.SaveVaccineRequest;
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

@Route(path = HomeRouter.ROUTER_CARE_VACCINE_ACTIVITY)
public class VaccineActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.status_bar)
    View statusBar;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;
    @BindView(R2.id.vaccine_time_text)
    TextView vaccineTimeText;
    @BindView(R2.id.set_vaccine_time)
    RelativeLayout setVaccineTime;
    @BindView(R2.id.vaccine_alarm_text)
    TextView vaccineAlarmText;
    @BindView(R2.id.set_vaccine_alarm)
    RelativeLayout setVaccineAlarm;
    @BindView(R2.id.vaccine_repeat_text)
    TextView vaccineRepeatText;
    @BindView(R2.id.set_vaccine_repeat)
    RelativeLayout setVaccineRepeat;
    @BindView(R2.id.vaccine_pet_name)
    TextView vaccinePetName;
    @BindView(R2.id.set_vaccine_pet)
    RelativeLayout setVaccinePet;

    private static String dateText;
    private static String timeText;
    private static long vaccineAlarmTime;
    private PopupWindow aPopupWindow;
    private PopupWindow rPopupWindow;
    private PopupWindow pPopupWindow;

    private static List<PetData> mPetNameList;
    private RecyclerView recyclerView;
    private final static String type = "疫苗注射";
    private final static String TAG = "vaccine";
    private HashMap<String, PendingIntent> hashMap = new HashMap<>();
    public static AlarmManager vaccineAlarmManager;
    public static PendingIntent vaccinePendingIntent;
    public static int day;

    //两个工具calendar
    private Calendar calendar = Calendar.getInstance();
    Calendar cd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_vaccine);
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
        subTitle.setVisibility(View.VISIBLE);
        subTitle.setText("保存");
        initData();
        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVaccineInfo();
                setVacAlarm(vaccineAlarmTime);
            }
        });
        setVaccineTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        setVaccineAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmPopupWindow();
            }
        });
        setVaccineRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatPopupWindow();
            }
        });
        setVaccinePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetManager.getPetInfo();
                showPetPopupWindow();
            }
        });
    }

    public void initData(){
        getVaccineInfo(Constant.petData.petname);
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
        }, cd.get(Calendar.YEAR), cd.get(Calendar.MONTH), cd.get(Calendar.DAY_OF_MONTH));
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
                vaccineAlarmTime = calendar.getTimeInMillis();
                Constant.vaccineDateAndTime = vaccineAlarmTime;
                vaccineTimeText.setText(dateText + " " + timeText);
            }
        }, cd.get(Calendar.HOUR_OF_DAY), cd.get(Calendar.MINUTE), true).show();
    }

    public void showAlarmPopupWindow(){
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_alarm_popup,null);
        aPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT,true);
        aPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        aPopupWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.bath_yes);
        TextView tv2 = (TextView) contenView.findViewById(R.id.bath_no);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineAlarmText.setText("提醒");
                aPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineAlarmText.setText("不提醒");
                aPopupWindow.dismiss();
            }
        });
        //显示PopupWindow
        aPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }
    public void showRepeatPopupWindow(){
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_repeat_popup, null);
        rPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        rPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        rPopupWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.one_week);
        TextView tv2 = (TextView) contenView.findViewById(R.id.two_weeks);
        TextView tv3 = (TextView) contenView.findViewById(R.id.one_month);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineRepeatText.setText("每周");
                rPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineRepeatText.setText("每两周");
                rPopupWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineRepeatText.setText("每月");
                rPopupWindow.dismiss();
            }
        });
        //显示PopupWindow
        rPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }
    public void showPetPopupWindow(){
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
                vaccinePetName.setText(petName);
                getVaccineInfo(petName);
                pPopupWindow.dismiss();
            }
        });
        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);
        //显示PopupWindow
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    private SaveVaccineRequest mSaveVaccineRequest;
    public void saveVaccineInfo(){
        if (mSaveVaccineRequest != null && mSaveVaccineRequest.isFinish()) {
            return;
        }
        mSaveVaccineRequest = new SaveVaccineRequest();
        mSaveVaccineRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveVaccineRequest.addUrlParam("type",type);
        mSaveVaccineRequest.addUrlParam("time", vaccineTimeText.getText().toString());
        mSaveVaccineRequest.addUrlParam("remind",vaccineAlarmText.getText().toString());
        mSaveVaccineRequest.addUrlParam("period",vaccineRepeatText.getText().toString());
        mSaveVaccineRequest.addUrlParam("petname",vaccinePetName.getText().toString());
        mSaveVaccineRequest.setRequestListener(new RequestListener<BaseModel>() {
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
        HttpManager.addRequest(mSaveVaccineRequest);
    }

    private GetVaccinreInfoRequest mGetVaccinreInfoRequest;
    private void getVaccineInfo(final String petName){
        if (mGetVaccinreInfoRequest!=null&&!mGetVaccinreInfoRequest.isFinish()){
            return;
        }
        mGetVaccinreInfoRequest = new GetVaccinreInfoRequest();
        mGetVaccinreInfoRequest.addUrlParam("username",AccountManager.getUserName());
        mGetVaccinreInfoRequest.addUrlParam("petname",petName);
        mGetVaccinreInfoRequest.addUrlParam("type",type);
        mGetVaccinreInfoRequest.setRequestListener(new RequestListener<AlarmInfoModel>() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onSuccess(AlarmInfoModel result) {
                vaccineTimeText.setText(result.time);
                vaccineAlarmText.setText(result.remind);
                vaccineRepeatText.setText(result.period);
                vaccinePetName.setText(petName);
            }
            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetVaccinreInfoRequest);
    }

    public void setVacAlarm(long targetTime){
        String key = String.valueOf(targetTime);
        int checkRequestCode = key.hashCode();
        PendingIntent targetIntent = hashMap.get(key);
        if (targetIntent!=null){
            targetIntent.cancel();
            hashMap.remove(key);
        }
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_SIX);
        vaccinePendingIntent = PendingIntent.getBroadcast(this,checkRequestCode,intent,0);
        vaccineAlarmManager = (AlarmManager) PnApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        if(vaccineAlarmText.getText().toString().equals("提醒")&&vaccineRepeatText.getText().toString().equals("每周")){
            day = 7;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                vaccineAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,vaccinePendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                vaccineAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,vaccinePendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                vaccineAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,vaccinePendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (vaccineAlarmText.getText().toString().equals("提醒")&&vaccineRepeatText.getText().toString().equals("每两周")){
            day= 14;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                vaccineAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,vaccinePendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                vaccineAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,vaccinePendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                vaccineAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,vaccinePendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (vaccineAlarmText.getText().toString().equals("提醒")&&vaccineRepeatText.getText().toString().equals("每月")){
            day = 30;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                vaccineAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,vaccinePendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                vaccineAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,vaccinePendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                vaccineAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*30,vaccinePendingIntent);
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
