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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.punuo.pet.home.care.request.GetCheckInfoRequest;
import com.punuo.pet.home.care.request.SaveCheckRequest;
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

    private static String dateText;
    private static String timeText;
    private static long checkAlarmTime;
    private PopupWindow aPopupWindow;
    private PopupWindow rPopupWindow;
    private PopupWindow pPopupWindow;

    private static List<PetData> mPetNameList;
    private RecyclerView recyclerView;
    private final static String type = "体检";
    private final static String TAG = "check";
    private HashMap<String,PendingIntent> hashMap = new HashMap<>();
    public static AlarmManager checkAlarmManager;
    public static PendingIntent checkPendingIntent;
    public static int day;
    //两个工具calendar
    private Calendar calendar = Calendar.getInstance();
    Calendar cd = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_checkup);
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
                saveCheckInfo();
                setCheckAlarm(checkAlarmTime);
            }
        });
        setCheckTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置时间
                setDate();
            }
        });
        setCheckAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlarmPopupWindow();
            }
        });
        setCheckRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置重复周期
                showRepeatPopupWindow();
            }
        });
        setCheckPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置关联宠物
                PetManager.getPetInfo();
                showPetPopupWindow();
            }
        });
    }

    /**
     * 初始化显示
     */
    public void initData(){
//        checkPetnameText.setText(Constant.petData.petname);
        getCheckInfo(Constant.petData.petname);
    }

    public void setDate(){
        DatePickerDialog dpd = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                dateText = year+"-"+month+1+"-"+dayOfMonth;
                setTime();
            }
        },cd.get(Calendar.YEAR),cd.get(Calendar.MONTH),cd.get(Calendar.DAY_OF_MONTH));
        dpd.show();
    }

    public void setTime(){
         new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String h = i<10? "0"+i:i+"";
                String m = i1<10? "0"+i1: i1+"";
                timeText = h+":"+m;
                checkTimeText.setText(dateText+" "+timeText);
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.MILLISECOND,i1);
                checkAlarmTime = calendar.getTimeInMillis();
                Constant.checkDateAndTime = checkAlarmTime;
                checkTimeText.setText(dateText+" "+timeText);
            }
        },cd.get(Calendar.HOUR_OF_DAY),cd.get(Calendar.MINUTE),true).show();
    }

    public void showAlarmPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_alarm_popup, null);
        aPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        aPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        aPopupWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.bath_yes);
        TextView tv2 = (TextView) contenView.findViewById(R.id.bath_no);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAlarmText.setText("提醒");
                aPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAlarmText.setText("不提醒");
                aPopupWindow.dismiss();
            }
        });

        aPopupWindow.showAtLocation(contenView,Gravity.BOTTOM,0,0);
    }

    public void showRepeatPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.repeat_popup, null);
        rPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        rPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        rPopupWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.half_year);
        TextView tv2 = (TextView) contenView.findViewById(R.id.one_year);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRepeatText.setText("每半年");
                rPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRepeatText.setText("每年");
                rPopupWindow.dismiss();
            }
        });
        //显示PopupWindow
        rPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
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
                checkPetnameText.setText(petName);
                //TODO 需要在点击不同的宠物名时获取对应的信息
                getCheckInfo(petName);
                pPopupWindow.dismiss();
            }
        });

        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);

        //显示PopupWindow
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    private SaveCheckRequest mSaveCheckRequest;
    private void saveCheckInfo(){
        if(mSaveCheckRequest!=null&&!mSaveCheckRequest.isFinish()){
            return;
        }
        mSaveCheckRequest = new SaveCheckRequest();
        mSaveCheckRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveCheckRequest.addUrlParam("type",type);
        mSaveCheckRequest.addUrlParam("petname",checkPetnameText.getText().toString());
        mSaveCheckRequest.addUrlParam("time",checkAlarmTime);
        mSaveCheckRequest.addUrlParam("remind",checkAlarmText.getText().toString());
        mSaveCheckRequest.addUrlParam("period",checkRepeatText.getText().toString());
        mSaveCheckRequest.setRequestListener(new RequestListener<BaseModel>() {
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
        HttpManager.addRequest(mSaveCheckRequest);
    }

    private GetCheckInfoRequest mGetCheckInfoRequest;
    public void getCheckInfo(final String petName){
        if (mGetCheckInfoRequest!=null&&!mGetCheckInfoRequest.isFinish()){
            return;
        }
        mGetCheckInfoRequest = new GetCheckInfoRequest();
        mGetCheckInfoRequest.addUrlParam("username", AccountManager.getUserName());
        mGetCheckInfoRequest.addUrlParam("type",type);
        mGetCheckInfoRequest.addUrlParam("petname",petName);
        mGetCheckInfoRequest.setRequestListener(new RequestListener<AlarmInfoModel>() {
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
                    checkTimeText.setText(sdf.format(date));
                    checkAlarmText.setText(result.remind);
                    checkRepeatText.setText(result.period);
                    checkPetnameText.setText(petName);
                } else {
                    checkTimeText.setText(sdf.format(new Date()));
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetCheckInfoRequest);
    }

    public void setCheckAlarm(Long targetTime){
        String key = String.valueOf(targetTime);
         int checkRequestCode = key.hashCode();
        PendingIntent targetIntent = hashMap.get(key);
        if (targetIntent!=null){
            targetIntent.cancel();
            hashMap.remove(key);
        }
        Intent intent = new Intent();
        intent.setAction(Constant.ALARM_TWO);
        checkPendingIntent = PendingIntent.getBroadcast(this,checkRequestCode,intent,0);
        checkAlarmManager = (AlarmManager) PnApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
        if(checkAlarmText.getText().toString().equals("提醒")&&checkRepeatText.getText().toString().equals("每半年")){
            day = 183;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                checkAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,checkAlarmTime,checkPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                checkAlarmManager.setExact(AlarmManager.RTC_WAKEUP,checkAlarmTime,checkPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                checkAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,checkAlarmTime,day*AlarmManager.INTERVAL_DAY,checkPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
        if (checkAlarmText.getText().toString().equals("提醒")&&checkRepeatText.getText().toString().equals("每年")){
            day = 365;
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                checkAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,checkAlarmTime,checkPendingIntent);
                Log.i(TAG, ">M");
            }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
                checkAlarmManager.setExact(AlarmManager.RTC_WAKEUP,checkAlarmTime,checkPendingIntent);
                Log.i(TAG, ">KITKAT");
            }else{
                checkAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,checkAlarmTime,day*AlarmManager.INTERVAL_DAY,checkPendingIntent);
            }
            hashMap.put(key,targetIntent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PetModel petModel){
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
