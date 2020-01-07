package com.punuo.pet.home.care.item_activity;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
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

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.PetManager;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.care.adapter.PetRelevanceAdapter;
import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.pet.home.care.request.GetBathInfoRequest;
import com.punuo.pet.home.care.request.SaveBathRequest;
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
    @BindView(R2.id.bath_alarm)
    TextView bathAlarm;
    @BindView(R2.id.bath_repeat)
    TextView bathRepeat;
    @BindView(R2.id.bath_pet)
    TextView bathPet;

    private static final String TAG = "bath";
    private final String type = "洗澡清洁";
    Calendar cal = Calendar.getInstance();
    private static Calendar calendar = Calendar.getInstance();
    private static long bathDateAndTime;
    private static String dataSelect;
    private static String timeSelect;
    private PopupWindow mPopupWindow;
    private PopupWindow rPopuoWindow;
    private PopupWindow pPopupWindow;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    public static AlarmManager mAlarmManager;
    public static PendingIntent pendingIntent;
    private static List<PetData> mPetNameList;
    private HashMap<String,PendingIntent> hashMap = new HashMap<>();
    public static int day;

    @Autowired(name = "petData")
    PetData mPetData;

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
                /**
                 *保存设置的数据信息到服务器并设置相应的alarm
                 */
                saveBathInfo();
                setBathAlarm(bathDateAndTime);
            }
        });

        /**
         * 设置时间
         */
        setBathTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
        /**
         * 设置提前提醒
         */
        setBathAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmPopupWindow();
            }
        });
        /**
         * 设置重复周期
         */
        setBathRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatPopupWindow();
            }
        });
        /**
         * 设置关联宠物
         */
        setBathPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PetManager.getPetInfo();
                showPetPopupWindow();
            }
        });
    }

    public void initData() {
        /**
         * 初始化显示
         */
        getBathInfo(Constant.petData.petname);
    }

    public void setDate() {
        DatePickerDialog dpd = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            //点击确定时触发
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dataSelect = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
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
                timeSelect = h + ":" + m;
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.MILLISECOND, 0);
                bathDateAndTime = calendar.getTimeInMillis();
                Constant.bathDateAndTime = bathDateAndTime;
                Log.i("setTime", "" + bathDateAndTime);
                dateSelectText.setText(dataSelect + " " + timeSelect);
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    private SaveBathRequest mSaveBathRequest;

    public void saveBathInfo() {
        if (mSaveBathRequest != null && mSaveBathRequest.isFinish()) {
            return;
        }
        mSaveBathRequest = new SaveBathRequest();
        mSaveBathRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveBathRequest.addUrlParam("type",type);
        mSaveBathRequest.addUrlParam("time", dateSelectText.getText().toString());
        mSaveBathRequest.addUrlParam("remind",bathAlarm.getText().toString());
        mSaveBathRequest.addUrlParam("period",bathRepeat.getText().toString());
        mSaveBathRequest.addUrlParam("petname",bathPet.getText().toString());
        mSaveBathRequest.setRequestListener(new RequestListener<BaseModel>() {
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
        HttpManager.addRequest(mSaveBathRequest);
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
                bathAlarm.setText("提醒");
                mPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bathAlarm.setText("不提醒");
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
                bathRepeat.setText("每周");
                rPopuoWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bathRepeat.setText("每两周");
                rPopuoWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bathRepeat.setText("每月");
                rPopuoWindow.dismiss();
            }
        });
        //显示PopupWindow
        rPopuoWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    public void showPetPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_list, null);
        recyclerView = (RecyclerView) contenView.findViewById(R.id.bathe_recylcer);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final PetRelevanceAdapter adapter = new PetRelevanceAdapter(this,mPetNameList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListenrt(new PetRelevanceAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                String petName = adapter.getPetName(position);
                bathPet.setText(petName);
                getBathInfo(petName);
                pPopupWindow.dismiss();
            }
        });

        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);

        //显示PopupWindow
//        View rootView = LayoutInflater.from(this).inflate(R.layout.care_bathe,null);
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    public void setBathAlarm(Long targetTime){
        String key = String.valueOf(targetTime);
        PendingIntent targetIntent = hashMap.get(key);
        int requestCode = key.hashCode();
        if (targetIntent!=null){
            targetIntent.cancel();
            hashMap.remove(key);
        }
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_ONE);
        pendingIntent = PendingIntent.getBroadcast(this,requestCode,intent,0);
        mAlarmManager = (AlarmManager) PnApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        if (bathAlarm.getText().toString().equals("不提醒")){
            return;
        }
        if(bathAlarm.getText().toString().equals("提醒")&&bathRepeat.getText().toString().equals("每周")){
            day = 7;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,bathDateAndTime,pendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,bathDateAndTime,pendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,bathDateAndTime,AlarmManager.INTERVAL_DAY*day,pendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (bathAlarm.getText().toString().equals("提醒")&&bathRepeat.getText().toString().equals("每两周")){
            day=14;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,bathDateAndTime,pendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,bathDateAndTime,pendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,bathDateAndTime,AlarmManager.INTERVAL_DAY*day,pendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (bathAlarm.getText().toString().equals("提醒")&&bathRepeat.getText().toString().equals("每月")){
            day = 30;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,bathDateAndTime,pendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                mAlarmManager.setExact(AlarmManager.RTC_WAKEUP,bathDateAndTime,pendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,bathDateAndTime,AlarmManager.INTERVAL_DAY*day,pendingIntent);
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

    private GetBathInfoRequest mGetBathInfoRequest;
    public void getBathInfo(final String petName){
        if (mGetBathInfoRequest!=null&&!mGetBathInfoRequest.isFinish()){
            return;
        }
        mGetBathInfoRequest = new GetBathInfoRequest();
        mGetBathInfoRequest.addUrlParam("username",AccountManager.getUserName());
        mGetBathInfoRequest.addUrlParam("petname",petName);
        mGetBathInfoRequest.addUrlParam("type",type);
        mGetBathInfoRequest.setRequestListener(new RequestListener<AlarmInfoModel>() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onSuccess(AlarmInfoModel result) {
                Log.i(TAG, result.message);
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm");
//                Date date = new Date(result.time);
                dateSelectText.setText(result.time);
                bathAlarm.setText(result.remind);
                bathRepeat.setText(result.period);
                bathPet.setText(petName);
            }
            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetBathInfoRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
