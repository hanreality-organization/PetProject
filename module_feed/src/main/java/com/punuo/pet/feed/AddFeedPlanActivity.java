package com.punuo.pet.feed;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.punuo.pet.feed.adapter.FeedViewAdapter;
import com.punuo.pet.feed.plan.DeletePlanRequest;
import com.punuo.pet.feed.plan.DeletePlanSipRequest;
import com.punuo.pet.feed.plan.GetPlanRequest;
import com.punuo.pet.feed.plan.Plan;
import com.punuo.pet.feed.plan.PlanModel;
import com.punuo.pet.feed.plan.PlanToSipRequest;
import com.punuo.pet.router.FeedRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.event.AddPlanSuccessEvent;
import com.punuo.sip.event.DeletePlanSuccessEvent;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    private TextView timeSelect;
    private TextView mealName;
    private TextView countSelect;
    private TextView mPlanSum;
    private TextView mFeedCountSum;
    private View mButton;
    private FeedViewAdapter mFeedViewAdapter;

    private final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private long selectTime = 0L;
    private String mealText = "";
    private String countText = "";
    private boolean enableAddPlan = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_add_plan_activity);
        ButterKnife.bind(this);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mTitle.setText(R.string.string_feed_plan);
        mFeedCountSum = (TextView) findViewById(R.id.feed_count_sum);
        mPlanSum = (TextView) findViewById(R.id.plan_sum);
        mButton = findViewById(R.id.button);
        //新增计划部分
        timeSelect = (TextView) findViewById(R.id.time_select);
        mealName = (TextView) findViewById(R.id.name_edit);
        countSelect = (TextView) findViewById(R.id.size_edit);

        mBack.setOnClickListener(v -> onBackPressed());


        countSelect.setOnClickListener(view -> {
            EditText editText = new EditText(AddFeedPlanActivity.this);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(AddFeedPlanActivity.this)
                    .setTitle(R.string.string_feed_size)
                    .setView(editText)
                    .setPositiveButton(R.string.string_confirm, (dialog, which) -> {
                        String text = editText.getText().toString();
                        if (TextUtils.isEmpty(text)) {
                            ToastUtils.showToast(getString(R.string.string_feed_size));
                            return;
                        }
                        countText = text;
                        countSelect.setText(getString(R.string.string_copies, text));
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.string_cancel, (dialog, which) -> dialog.dismiss()).show();
        });

        mealName.setOnClickListener(v -> {
            EditText editText = new EditText(AddFeedPlanActivity.this);
            new AlertDialog.Builder(AddFeedPlanActivity.this)
                    .setTitle(R.string.string_feed_name_input)
                    .setView(editText)
                    .setPositiveButton(R.string.string_confirm, (dialog, which) -> {
                        String text = editText.getText().toString();
                        if (TextUtils.isEmpty(text)) {
                            ToastUtils.showToast(getString(R.string.string_feed_name_input));
                            return;
                        }
                        mealText = text;
                        mealName.setText(text);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        });

        timeSelect.setOnClickListener(v -> {
            new TimePickerBuilder(AddFeedPlanActivity.this, (date, v1) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.set(Calendar.YEAR, 2019);
                calendar.set(Calendar.MONTH, 1);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.SECOND, 0);
                selectTime = calendar.getTimeInMillis() / 1000L;
                timeSelect.setText(mSimpleDateFormat.format(date));
            })
                    .isDialog(true)
                    .setType(new boolean[]{false, false, false, true, true, false})
                    .build().show();
        });

        mButton.setOnClickListener(view -> {
            if (!enableAddPlan) {
                ToastUtils.showToast(getString(R.string.string_feed_tip_1));
                return;
            }
            if (selectTime == 0L) {
                ToastUtils.showToast(getString(R.string.string_feed_tip_2));
                return;
            }
            if (TextUtils.isEmpty(mealText)) {
                ToastUtils.showToast(getString(R.string.string_feed_tip_3));
                return;
            }
            if (TextUtils.isEmpty(countText)) {
                ToastUtils.showToast(getString(R.string.string_feed_tip_4));
                return;
            }
            savePlanToSip(String.valueOf(selectTime), mealText, countText, AccountManager.getUserName());
            scrollToFinishActivity();
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(AddFeedPlanActivity.this);
        mEditPlan.setLayoutManager(layoutManager);
        mFeedViewAdapter = new FeedViewAdapter(this, new ArrayList<Plan>());
        mEditPlan.setAdapter(mFeedViewAdapter);

        //删除功能
        mFeedViewAdapter.setOnItemLongClickListener(position -> {
            new AlertDialog.Builder(AddFeedPlanActivity.this)
                    .setTitle(R.string.string_attention)
                    .setMessage(R.string.string_delete_tip)
                    .setPositiveButton(R.string.string_confirm, (dialog, which) -> {
                        String delName = mFeedViewAdapter.getPlanName(position);
                        deletePlan(delName);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.string_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        });
        getFeedPlan();

    }


    public void savePlanToSip(String planTime, String planName, String planCount, String userName) {
        PlanToSipRequest planToSipRequest = new PlanToSipRequest(planTime, planName, planCount, userName);
        SipUserManager.getInstance().addRequest(planToSipRequest);
    }


    private GetPlanRequest mGetPlanRequest;

    public void getFeedPlan() {
        if (mGetPlanRequest != null && !mGetPlanRequest.isFinish()) {
            return;
        }
        mGetPlanRequest = new GetPlanRequest();
        mGetPlanRequest.addUrlParam("userName", AccountManager.getUserName());
        mGetPlanRequest.setRequestListener(new RequestListener<PlanModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(PlanModel result) {
                if (result == null || result.mPlanList == null) {
                    return;
                }
                mFeedViewAdapter.clear();
                mFeedViewAdapter.addAll(result.mPlanList);
                mFeedViewAdapter.notifyDataSetChanged();
                mFeedCountSum.setText(result.feedCountSum);
                mPlanSum.setText(result.planSum);
                enableAddPlan = result.mPlanList.size() < 6;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetPlanRequest);
    }

    private DeletePlanRequest mDeletePlanRequest;

    public void deletePlan(String deleteName) {
        if (mDeletePlanRequest != null && !mDeletePlanRequest.isFinish()) {
            return;
        }
        mDeletePlanRequest = new DeletePlanRequest();
        mDeletePlanRequest.addUrlParam("name", deleteName);
        mDeletePlanRequest.addUrlParam("userName", AccountManager.getUserName());
        mDeletePlanRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                ToastUtils.showToast(result.message);
                getFeedPlan();
                deletePlanNotifyDevice(); //通知设备去刷新喂食列表
                EventBus.getDefault().post(new DeletePlanSuccessEvent());
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mDeletePlanRequest);
    }

    public void deletePlanNotifyDevice(){
        DeletePlanSipRequest deletePlanSipRequest = new DeletePlanSipRequest();
        SipUserManager.getInstance().addRequest(deletePlanSipRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddPlanSuccessEvent event) {
        scrollToFinishActivity();
    }
}
