package com.punuo.pet.feed;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.feed.plan.MyPlanAdapter;
import com.punuo.pet.feed.plan.Plan;
import com.punuo.pet.feed.plan.SavePlanRequest;
import com.punuo.pet.feed.plan.SelectCountDialog;
import com.punuo.pet.router.FeedRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.ToastUtils;

import org.zoolu.sip.dialog.Dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
@Route(path = FeedRouter.ROUTER_ADD_FEED_PLAN_ACTIVITY)
public class AddFeedPlanActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.pet_list)
    RecyclerView mPetList;
    private Calendar calendar;
    private TimePicker timePicker;
    private TextView timeSelectText;
    private TextView timeSure;
    private TextView mealName;
    private TextView countSelect;
    private RelativeLayout timeSelect;
    Button mButton;
    private SelectCountDialog selectCountDialog;

    private List<Plan> planList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_add_plan_activity);
        ButterKnife.bind(this);
        initView();
        timePick();
    }

    private void initView() {
        mTitle.setText("喂食计划");

        initPlan();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_editPlan);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        MyPlanAdapter adapter = new MyPlanAdapter(planList);
        recyclerView.setAdapter(adapter);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        timeSelectText =(TextView) findViewById(R.id.time_select_text);
        mealName =(TextView) findViewById(R.id.meal_name);
        countSelect =(TextView)findViewById(R.id.count_select);

        countSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCountDialogShow();
                //TODO 如何将dialog中选中的数展示出来?
                countSelect.setText("3");
            }
        });

        mButton =(Button)findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String planTime = timeSelectText.getText().toString().trim();
                String planName = mealName.getText().toString().trim();
                String planCount = countSelect.getText().toString().trim();
                savePlanOnServer(planTime,planName,planCount);
                scrollToFinishActivity();
            }
        });


    }

    public void selectCountDialogShow(){
        selectCountDialog = new SelectCountDialog(this,R.layout.select_count_dialog,new int[]{R.id.select_count,R.id.add_count,R.id.less_count});
        selectCountDialog.show();
    }

    private SavePlanRequest mSavePlanRequest;
    public void savePlanOnServer(String planTime,String planName,String planCount){
        if(mSavePlanRequest!=null&&mSavePlanRequest.isFinish()){
            return;
        }
        mSavePlanRequest = new SavePlanRequest();
        mSavePlanRequest.addUrlParam("planTime",planTime);
        mSavePlanRequest.addUrlParam("planName",planName);
        mSavePlanRequest.addUrlParam("planCount",planCount);
        mSavePlanRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                ToastUtils.showToast(result.message);
                scrollToFinishActivity();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mSavePlanRequest);
    }

    //TODO 测试显示计划
    private void  initPlan(){
        Plan breakfast = new Plan("7:30","breakfast","3");
        planList.add(breakfast);
        Plan lunch = new Plan("12:00","lunch","4");
        planList.add(lunch);
        Plan dinner = new Plan("18:00","dinner","3");
        planList.add(dinner);
    }

    public void timePick(){
        //TODO 以下代码用于测试时间选择

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;//月份从0开始计算
        int day = calendar.get(Calendar.DATE);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);//设置点击事件不弹出键盘
        timePicker.setIs24HourView(true);//设置时间显示是24小时格式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(8);
            timePicker.setMinute(10);
        }

        timeSelect = (RelativeLayout)findViewById(R.id.time_select);

        timeSelectText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                timeSelect.setVisibility(View.VISIBLE);
            }
        });

        timeSure = (TextView)findViewById(R.id.time_sure);
        timeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelect.setVisibility(View.GONE);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {//获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                timeSelectText.setText(i+":"+i1);
            }
        });
    }

}
