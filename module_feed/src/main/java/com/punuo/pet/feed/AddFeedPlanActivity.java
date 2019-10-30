package com.punuo.pet.feed;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.FeedRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

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

    //子布局中的id
    private TextView time;
    private TextView name;
    private TextView count;

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
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        timeSelectText =(TextView) findViewById(R.id.time_select_text);
        mealName =(TextView) findViewById(R.id.meal_name);
        countSelect =(TextView)findViewById(R.id.count_select);



        mButton =(Button)findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completePlanEdit();
            }
        });
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

    private LinearLayout mLinearLayout;
    private View mFeedShowView;

    //首先加载子布局，然后将子布局以view的形式传入事先写好的容器中
    public void completePlanEdit(){
            mFeedShowView =View.inflate(this,R.layout.feed_plan_show,null);
            mLinearLayout=(LinearLayout)findViewById(R.id.box);
            mLinearLayout.addView(mFeedShowView);
            time=(TextView) findViewById(R.id.time);
            name=(TextView) findViewById(R.id.name);
            count=(TextView) findViewById(R.id.count);
            time.setText(timeSelectText.getText().toString());
            name.setText(mealName.getText().toString());
            count.setText(countSelect.getText().toString());
    }
}
