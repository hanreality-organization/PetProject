package com.punuo.pet.home.care.item_activity;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.care.adapter.PetRelevanceAdapter;
import com.punuo.pet.home.care.model.PetData;
import com.punuo.pet.home.care.model.PetNameListModel;
import com.punuo.pet.home.care.request.GetRelevancePetRequest;
import com.punuo.pet.home.care.request.SaveBathTimeRequest;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.StatusBarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    private static final String TAG = "bathe";
    Calendar cal = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();
    private static long dateAndTime;
    private static String dataSelect;
    private static String timeSelect;
    private PopupWindow mPopupWindow;
    private PopupWindow rPopuoWindow;
    private PopupWindow pPopupWindow;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    private static List<PetData> mPetNameList;

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
        subTitle.setVisibility(View.VISIBLE);
        subTitle.setText("保存");
        initData();
        subTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 保存所有相关数据
//                saveBathInfo();
                /**
                 *
                 */
            }
        });

        /**
         * 设置时间
         */
        setBathTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
                Log.i(TAG, "" + Constant.bathDateAndTIme);
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
                getPetName();
            }
        });
    }

    public void initData() {
        /**
         * 初始化显示
         */
        //TODO 不应该写死，应该利用宠物名去获取相关的信息；
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(Constant.bathDateAndTIme);
        dateSelectText.setText(simpleDateFormat.format(date));
        bathAlarm.setText(Constant.bathAlarm);
        bathRepeat.setText(Constant.bathRepeat);
        bathPet.setText(Constant.bathRelevancePetName);
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
                dateAndTime = calendar.getTimeInMillis();
                Log.i("setTime", "" + dateAndTime);
                dateSelectText.setText(dataSelect + " " + timeSelect);
                Constant.bathDateAndTIme = dateAndTime;
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
    }

    //TODO 将设置的关于洗澡的相关信息上传到数据库的路径
    private SaveBathTimeRequest mSaveBathTimeRequest;

    public void saveBathInfo(long time) {
        if (mSaveBathTimeRequest != null && mSaveBathTimeRequest.isFinish()) {
            return;
        }
        mSaveBathTimeRequest = new SaveBathTimeRequest();
        mSaveBathTimeRequest.addUrlParam("username", AccountManager.getUserName());
        mSaveBathTimeRequest.addUrlParam("bathTime", time);
        mSaveBathTimeRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                Log.i("bath", result.message);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mSaveBathTimeRequest);
    }

    public void showAlarmPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_alarm_popup, null);
        mPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        mPopupWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.one_hour);
        TextView tv2 = (TextView) contenView.findViewById(R.id.two_hour);
        TextView tv3 = (TextView) contenView.findViewById(R.id.one_day);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置提前一小时提醒的逻辑
                bathAlarm.setText("提前一小时");
                Constant.bathAlarm = "提前一小时";
                mPopupWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置提前两小时提醒的逻辑
                bathAlarm.setText("提前两小时");
                Constant.bathAlarm = "提前两小时";
                mPopupWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置提前一天提醒的逻辑
                bathAlarm.setText("提前一天");
                Constant.bathAlarm = "提前一天";
                mPopupWindow.dismiss();
            }
        });

        //显示PopupWindow
//        View rootView = LayoutInflater.from(this).inflate(R.layout.care_bathe, null);
        mPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    public void showRepeatPopupWindow() {
        View contenView = LayoutInflater.from(this).inflate(R.layout.bathe_repeat_popup, null);
        rPopuoWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        rPopuoWindow.setAnimationStyle(R.style.DialogWindowStyle);
        rPopuoWindow.setContentView(contenView);

        TextView tv1 = (TextView) contenView.findViewById(R.id.one_week);
        TextView tv2 = (TextView) contenView.findViewById(R.id.half_month);
        TextView tv3 = (TextView) contenView.findViewById(R.id.one_month);
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置每周重复逻辑
                bathRepeat.setText("每周重复");
                Constant.bathRepeat = "每周重复";
                rPopuoWindow.dismiss();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置每半个月重复逻辑
                bathRepeat.setText("每半月重复");
                Constant.bathRepeat = "每半月重复";
                rPopuoWindow.dismiss();
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 设置每月重复逻辑
                bathRepeat.setText("每月重复");
                Constant.bathRepeat = "每月重复";
                rPopuoWindow.dismiss();
            }
        });
        //显示PopupWindow
//        View rootView = LayoutInflater.from(this).inflate(R.layout.care_bathe, null);
        rPopuoWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    private GetRelevancePetRequest mGetRelevancePetRequest;
    public void getPetName(){
        if (mGetRelevancePetRequest!=null&&mGetRelevancePetRequest.isFinish()){
            return;
        }
        mGetRelevancePetRequest = new GetRelevancePetRequest();
        mGetRelevancePetRequest.addUrlParam("username", AccountManager.getUserName());
        mGetRelevancePetRequest.setRequestListener(new RequestListener<PetNameListModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(PetNameListModel result) {
                mPetNameList = result.mPetNameList;
                Log.i(TAG, ""+mPetNameList);
                showPetPopupWindow();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetRelevancePetRequest);
    }

    public void showPetPopupWindow() {
//        List<PetData> list = new ArrayList<>();
//        PetData data = new PetData("小白");
//        PetData data1 = new PetData("小红");
//        PetData data2 = new PetData("小黄");
//        list.add(data);
//        list.add(data1);
//        list.add(data2);
//        Log.i("bathe", "" + list);

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
                Log.i("bath", petName);
                Constant.bathRelevancePetName = petName;
                bathPet.setText(petName);
                pPopupWindow.dismiss();
                //TODO 在选择不同的宠物名时应该获取对应的宠物的信息
            }
        });

        pPopupWindow = new PopupWindow(contenView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        pPopupWindow.setAnimationStyle(R.style.DialogWindowStyle);
        pPopupWindow.setContentView(contenView);

        //显示PopupWindow
//        View rootView = LayoutInflater.from(this).inflate(R.layout.care_bathe,null);
        pPopupWindow.showAtLocation(contenView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
