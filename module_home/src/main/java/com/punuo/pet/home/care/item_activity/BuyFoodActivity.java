package com.punuo.pet.home.care.item_activity;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.PetManager;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.care.adapter.PetRelevanceAdapter;
import com.punuo.pet.home.care.model.AlarmInfoModel;
import com.punuo.pet.home.care.request.GetBuyInfoRequest;
import com.punuo.pet.home.care.request.SaveBuyRequest;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = HomeRouter.ROUTER_CARE_BUY_FOOD_ACTIVITY)
public class BuyFoodActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.status_bar)
    View statusBar;
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;
    @BindView(R2.id.buy_time_text)
    TextView buyTimeText;
    @BindView(R2.id.set_food_time)
    RelativeLayout setFoodTime;
    @BindView(R2.id.buy_alarm_text)
    TextView buyAlarmText;
    @BindView(R2.id.set_food_alarm)
    RelativeLayout setFoodAlarm;
    @BindView(R2.id.buy_repeat_text)
    TextView buyRepeatText;
    @BindView(R2.id.set_food_repeat)
    RelativeLayout setFoodRepeat;
    @BindView(R2.id.buy_pet_name)
    TextView buyPetName;
    @BindView(R2.id.set_food_pet)
    RelativeLayout setFoodPet;

    private static String dateText;
    private static String timeText;
    private static long buyFoodAlarmTime;
    private PopupWindow aPopupWindow;
    private PopupWindow rPopupWindow;
    private PopupWindow pPopupWindow;

    private static List<PetData> mPetNameList;
    private RecyclerView recyclerView;
    private final static String type = "买宠物粮";
    private final static String TAG = "buyFood";
    private HashMap<String, PendingIntent> hashMap = new HashMap<>();
    public static AlarmManager buyAlarmManager;
    public static PendingIntent buyPendingIntent;
    public static int day;

    //两个工具calendar
    private Calendar calendar = Calendar.getInstance();
    Calendar cd = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_food);
        ButterKnife.bind(this);
        StatusBarUtil.translucentStatusBar(this, Color.TRANSPARENT, true);
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
                //TODO 保存所有数据并设置相应的闹钟
                saveBuyInfo();
                setBuyAlarm(buyFoodAlarmTime);
            }
        });
        setFoodTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置时间
                setDate();
            }
        });
        setFoodAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmPopupWindow();
            }
        });
        setFoodRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置重复周期
                showRepeatPopupWindow();
            }
        });
        setFoodPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置关联宠物
                PetManager.getPetInfo(false);
                showPetPopupWindow();
            }
        });
    }

    /**
     * 初始化显示
     */
    public void initData(){
        getBuyInfo(Constant.petData.petname);
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
                buyFoodAlarmTime = calendar.getTimeInMillis();
                Constant.buyDateAndTime = buyFoodAlarmTime;
                Log.i("setTime", "" + buyFoodAlarmTime);
                buyTimeText.setText(dateText + " " + timeText);
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
                buyAlarmText.setText("提醒");
                aPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyAlarmText.setText("不提醒");
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
                buyRepeatText.setText("每周");
                rPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyRepeatText.setText("每两周");
                rPopupWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyRepeatText.setText("每月");
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
                buyPetName.setText(petName);
                getBuyInfo(petName);
                pPopupWindow.dismiss();
            }
        });
        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);
        //显示PopupWindow
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    private SaveBuyRequest mSaveBuyRequest;
    public void saveBuyInfo(){
        if (mSaveBuyRequest != null && !mSaveBuyRequest.isFinish()) {
            return;
        }
        mSaveBuyRequest = new SaveBuyRequest();
        mSaveBuyRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveBuyRequest.addUrlParam("type",type);
        mSaveBuyRequest.addUrlParam("time", buyFoodAlarmTime);
        mSaveBuyRequest.addUrlParam("remind",buyAlarmText.getText().toString());
        mSaveBuyRequest.addUrlParam("period",buyRepeatText.getText().toString());
        mSaveBuyRequest.addUrlParam("petname",buyPetName.getText().toString());
        mSaveBuyRequest.setRequestListener(new RequestListener<BaseModel>() {
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
        HttpManager.addRequest(mSaveBuyRequest);
    }

    private GetBuyInfoRequest mGetBuyInfoRequest;
    private void getBuyInfo(final String petName){
        if (mGetBuyInfoRequest!=null&&!mGetBuyInfoRequest.isFinish()){
            return;
        }
        mGetBuyInfoRequest = new GetBuyInfoRequest();
        mGetBuyInfoRequest.addUrlParam("username",AccountManager.getUserName());
        mGetBuyInfoRequest.addUrlParam("petname",petName);
        mGetBuyInfoRequest.addUrlParam("type",type);
        mGetBuyInfoRequest.setRequestListener(new RequestListener<AlarmInfoModel>() {
            @Override
            public void onComplete() {

            }
            @Override
            public void onSuccess(AlarmInfoModel result) {
                if (result == null) {
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                if (result.success) {
                    Date date = new Date(result.time);
                    buyTimeText.setText(sdf.format(date));
                    buyAlarmText.setText(result.remind);
                    buyRepeatText.setText(result.period);
                    buyPetName.setText(petName);
                } else {
                    buyTimeText.setText(sdf.format(new Date()));
                }
            }
            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetBuyInfoRequest);
    }

    public void setBuyAlarm(long targetTime){
        String key = String.valueOf(targetTime);
        int checkRequestCode = key.hashCode();
        PendingIntent targetIntent = hashMap.get(key);
        if (targetIntent!=null){
            targetIntent.cancel();
            hashMap.remove(key);
        }
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_THREE);
        buyPendingIntent = PendingIntent.getBroadcast(this,checkRequestCode,intent,0);
        buyAlarmManager = (AlarmManager) PnApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        if(buyAlarmText.getText().toString().equals("提醒")&&buyRepeatText.getText().toString().equals("每周")){
            day = 7;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                buyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,buyPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                buyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,buyPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                buyAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,buyPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (buyAlarmText.getText().toString().equals("提醒")&&buyRepeatText.getText().toString().equals("每两周")){
            day= 14;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                buyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,buyPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                buyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,buyPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                buyAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*day,buyPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (buyAlarmText.getText().toString().equals("提醒")&&buyRepeatText.getText().toString().equals("每月")){
            day = 30;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                buyAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetTime,buyPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                buyAlarmManager.setExact(AlarmManager.RTC_WAKEUP,targetTime,buyPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                buyAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetTime,AlarmManager.INTERVAL_DAY*30,buyPendingIntent);
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
