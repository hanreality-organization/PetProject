package com.punuo.pet.feed;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.feed.plan.GetPlanRequest;
import com.punuo.pet.feed.plan.MyPlanAdapter;
import com.punuo.pet.feed.plan.Plan;
import com.punuo.pet.feed.plan.PlanModel;
import com.punuo.pet.feed.plan.PlanToSipRequest;
import com.punuo.pet.router.FeedRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;

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
    @BindView(R2.id.recycler_editPlan)
    RecyclerView mEditPlan;
    @BindView(R2.id.sub_title)
    TextView subTitle;


    private Calendar calendar;
    private TimePicker timePicker;
    private TextView timeSelectText;
    private TextView timeSure;
    private EditText mealName;
    private TextView countSelect;
    private RelativeLayout timeSelect;
    private TextView mPlanSum;
    private TextView mFeedCountSum;
    private Button mButton;
    private TextView addPlanCount;
    private TextView lessPlanCount;
    private int defaultPlanCount = 3;
    private long selectDateMills = 0;
    private MyPlanAdapter mMyPlanAdapter;


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
        mFeedCountSum =(TextView) findViewById(R.id.feed_count_sum);
        mPlanSum = (TextView) findViewById(R.id.plan_sum);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        timeSelectText = (TextView) findViewById(R.id.time_select_text);
        mealName = (EditText) findViewById(R.id.meal_name);
        countSelect = (TextView) findViewById(R.id.count_select);
        countSelect.setHint("选择份数");
        countSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countSelect.setText(defaultPlanCount+"");
            }
        });
        addPlanCount = (TextView)findViewById(R.id.add_plan_count);
        addPlanCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countSelect.setText((defaultPlanCount+=1)+"");
            }
        });
        lessPlanCount = (TextView)findViewById(R.id.less_plan_count);
        lessPlanCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(defaultPlanCount>1)
                countSelect.setText((defaultPlanCount-=1)+"");
            }
        });

        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String planTime = timeSelectText.getText().toString().trim();
                String planName = mealName.getText().toString().trim();
                String planCount = countSelect.getText().toString().trim();
                if(planTime.length()==0){
                    ToastUtils.showToast("请输入时间");
                }else if(planName.length()==0){
                    ToastUtils.showToast("请输入餐名");
                }else if(planCount.length()==0){
                    ToastUtils.showToast("请输入份数");
                }
                savePlanToSip(String.valueOf(selectDateMills / 1000), planName, planCount, AccountManager.getUserName());
                Log.i("plan", "喂食计划发送中... ");
                scrollToFinishActivity();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(AddFeedPlanActivity.this);
        mEditPlan.setLayoutManager(layoutManager);
        mMyPlanAdapter = new MyPlanAdapter(this, new ArrayList<Plan>());
        mEditPlan.setAdapter(mMyPlanAdapter);

        initPlanData();

    }


    public void savePlanToSip(String planTime, String planName, String planCount, String userName) {
        PlanToSipRequest planToSipRequest = new PlanToSipRequest(planTime, planName, planCount, userName);
        SipUserManager.getInstance().addRequest(planToSipRequest);
    }


    private GetPlanRequest mGetPlanRequest;
    public void initPlanData(){
        if(mGetPlanRequest!=null&&mGetPlanRequest.isFinish()){
            return;
        }
        mGetPlanRequest = new GetPlanRequest();
        mGetPlanRequest.addUrlParam("userName",AccountManager.getUserName());
        mGetPlanRequest.setRequestListener(new RequestListener<PlanModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(PlanModel result) {
                if (result == null || result.mPlanList == null) {
                    return;
                }
                mMyPlanAdapter.clear();
                mMyPlanAdapter.addAll(result.mPlanList);
                mMyPlanAdapter.notifyDataSetChanged();
                mFeedCountSum.setText(result.feedCountSum);
                mPlanSum.setText(result.planSum);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetPlanRequest);
    }
    public void timePick() {
        //TODO 以下代码用于测试时间选择

        calendar = Calendar.getInstance();
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);//设置点击事件不弹出键盘
        timePicker.setIs24HourView(true);//设置时间显示是24小时格式

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(00);
            timePicker.setMinute(00);
        }

        timeSelect = (RelativeLayout) findViewById(R.id.time_select);

        timeSelectText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                timeSelect.setVisibility(View.VISIBLE);
            }
        });

        timeSure = (TextView) findViewById(R.id.time_sure);
        timeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelect.setVisibility(View.GONE);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {//获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, 2019);
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH,1);
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                selectDateMills = calendar.getTimeInMillis();
                String sHour = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                String sMinute = minute < 10 ? "0"+minute:"" + minute;
                timeSelectText.setText(sHour+":"+sMinute);
            }
        });
    }

}
