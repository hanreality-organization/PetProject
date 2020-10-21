package com.punuo.pet.feed;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.punuo.pet.PetManager;
import com.punuo.pet.feed.adapter.FeedShowAdapter;
import com.punuo.pet.feed.feednow.FeedDialog;
import com.punuo.pet.feed.get_real_weight.UpdateWeightSipRequest;
import com.punuo.pet.feed.model.GetRemainderModel;
import com.punuo.pet.feed.model.OutedModel;
import com.punuo.pet.feed.module.FeedHeadModule;
import com.punuo.pet.feed.plan.GetPlanRequest;
import com.punuo.pet.feed.plan.Plan;
import com.punuo.pet.feed.plan.PlanModel;
import com.punuo.pet.feed.request.GetOutedRequest;
import com.punuo.pet.feed.request.GetRemainderRequest;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.FeedRouter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.dev.BindDevSuccessEvent;
import com.punuo.sip.dev.DevManager;
import com.punuo.sip.dev.UnBindDevSuccessEvent;
import com.punuo.sip.event.AddPlanSuccessEvent;
import com.punuo.sip.event.DeletePlanSuccessEvent;
import com.punuo.sip.model.DevNotifyData;
import com.punuo.sip.model.FeedCountData;
import com.punuo.sip.model.LatestWeightData;
import com.punuo.sip.model.LoginResponse;
import com.punuo.sip.model.OnLineData;
import com.punuo.sip.weight.WeightData;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.MMKVUtil;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.view.BreatheView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-09-12.
 **/
@Route(path = FeedRouter.ROUTER_FEED_HOME_FRAGMENT)
public class FeedFragment extends BaseFragment {
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.edit_feed_plan)
    View mEditFeedPlan;
    @BindView(R2.id.feed_right_now)
    View mFeedRightNow;
    @BindView(R2.id.pull_to_refresh)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R2.id.real_weight)
    LinearLayout mRealWeight;
    @BindView(R2.id.breathView)
    BreatheView mBreatheView;
    @BindView(R2.id.device_status)
    View deviceStatus;
    @BindView(R2.id.wifistate)
    TextView mWifiState;

    @BindView(R2.id.status_bar)
    View mStatusBar;

    private RecyclerView mRecyclerView;
    private FeedHeadModule mFeedHeadModule;
    private FeedDialog feedDialog;
    private FeedShowAdapter mFeedViewAdapter;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.feed_fragment_home, container, false);
        ButterKnife.bind(this, mFragmentView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        EventBus.getDefault().register(this);
        initView();
        return mFragmentView;
    }

    private void initView() {
        mTitle.setText("梦视宠物喂食器");
        mEditFeedPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(FeedRouter.ROUTER_ADD_FEED_PLAN_ACTIVITY).navigation();
            }
        });
        mFeedRightNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFeedDialog();
            }
        });
        deviceStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_DEVICE_MANAGER_ACTIVITY).navigation();
            }
        });
        mRealWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateWeight(AccountManager.getUserName());
            }
        });

        mPullToRefreshRecyclerView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);//设置刷新模式
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        LinearLayout headerView = new LinearLayout(getActivity());
        headerView.setOrientation(LinearLayout.VERTICAL);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        headerView.setGravity(Gravity.CENTER_HORIZONTAL);

//        mFeedViewAdapter = new FeedViewAdapter(getActivity(), new ArrayList<Plan>());
        mFeedViewAdapter = new FeedShowAdapter(getActivity(), new ArrayList<Plan>());
        mFeedViewAdapter.setHeaderView(headerView);
        mFeedHeadModule = new FeedHeadModule(getActivity(), headerView);
        headerView.addView(mFeedHeadModule.getView());
        mFeedHeadModule.setOnDateClickListener((time, selectCalendarData, isFeature) -> {
            if (isFeature) {
                mFeedHeadModule.resetDisplay();
            } else {
                getRemainderQuality(false);
                getOutedCount();
            }
        });
        mRecyclerView.setAdapter(mFeedViewAdapter);

        mPullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                refresh();
            }
        });
        layer();
        mBreatheView.setInterval(2000)
                .setCoreRadius(7f)
                .setDiffusMaxWidth(10f)
                .setDiffusColor(Color.parseColor("#ff1940"))
                .setCoreColor(Color.parseColor("#ff1940"))
                .onStart();
    }

    private void refresh() {
        PetManager.getPetInfo();
        DevManager.getInstance().refreshDevRelationShip();
        getPlan();
        getRemainderQuality(true);
        getOutedCount();
        DevManager.getInstance().isOnline();
    }

    private void layer() {
        if (!MMKVUtil.getBoolean("deviceManageGuide", false)) {
            View layer = LayoutInflater.from(getActivity()).inflate(R.layout.device_gudie_layout, null);
            layer.setPadding(0, mStatusBar.getLayoutParams().height, 0 ,0);
            if (getActivity() != null) {
                getActivity().addContentView(layer, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                MMKVUtil.setBoolean("deviceManageGuide", true);
                layer.setOnClickListener(v -> {
                    if (v.getParent() instanceof ViewGroup) {
                        ((ViewGroup) v.getParent()).removeView(v);
                    }
                    //引导关闭请求
                    refresh();
                });
            }
        } else {
            refresh();
        }
    }

    public void showFeedDialog() {
        feedDialog = new FeedDialog(getContext(), R.layout.feed_right_now,
                new int[]{R.id.count, R.id.sub_count, R.id.add_count, R.id.complete});
        feedDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PetModel petModel) {
        mFeedHeadModule.initPetInfo(petModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DevNotifyData result) {
        changeDeviceStatus(result.mDevInfo.live);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnLineData result) {
        int live = Integer.parseInt(result.live);
        changeDeviceStatus(live);
    }

    /**
     * 添加喂食计划成功
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AddPlanSuccessEvent event) {
        getPlan();
    }

    /**
     * 删除喂食计划成功
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DeletePlanSuccessEvent event) {
        getPlan();
    }

    private void changeDeviceStatus(int live) {
        if (live == 1) {
            mWifiState.setText("在线");
            mBreatheView.setCoreColor(Color.parseColor("#8BC34A"));
            mBreatheView.setDiffusColor(Color.parseColor("#8BC34A"));
        } else {
            String text = TextUtils.isEmpty(DevManager.getInstance().getDevId()) ? "未绑定" : "离线";
            mWifiState.setText(text);
            mBreatheView.setCoreColor(Color.parseColor("#ff0000"));
            mBreatheView.setDiffusColor(Color.parseColor("#ff0000"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginResponse event) {
        int live = Integer.parseInt(event.live);
        changeDeviceStatus(live);
    }

    /**
     * 绑定成功 需要重新获取设备在线信息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BindDevSuccessEvent event) {
        DevManager.getInstance().isOnline();
    }

    /**
     * 解绑之后 要重置设备在线信息为 离线
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnBindDevSuccessEvent event) {
        //重置设备状态
        changeDeviceStatus(0);

        getPlan();
        getRemainderQuality(true);
        getOutedCount();
    }

    private GetPlanRequest mGetPlanRequest;

    public void getPlan() {
        if (mGetPlanRequest != null && !mGetPlanRequest.isFinish()) {
            return;
        }
        mGetPlanRequest = new GetPlanRequest();
        mGetPlanRequest.addUrlParam("userName", AccountManager.getUserName());
        mGetPlanRequest.setRequestListener(new RequestListener<PlanModel>() {
            @Override
            public void onComplete() {
                mPullToRefreshRecyclerView.onRefreshComplete();
            }

            @Override
            public void onSuccess(PlanModel result) {
                if (result == null || result.mPlanList == null) {
                    mFeedViewAdapter.clear();
                    mFeedViewAdapter.notifyDataSetChanged();
                    mFeedHeadModule.updatePlan("0");
                    return;
                }
                mFeedViewAdapter.clear();
                mFeedViewAdapter.addAll(result.mPlanList);
                mFeedViewAdapter.notifyDataSetChanged();
                mFeedHeadModule.updatePlan(result.feedCountSum);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetPlanRequest);
    }

    /**
     * 初始化剩余重量
     */
    private GetRemainderRequest mGetRemainderRequest;

    public void getRemainderQuality(boolean needCheckRemainder) {
        if (mGetRemainderRequest != null && !mGetRemainderRequest.isFinish()) {
            return;
        }
        mGetRemainderRequest = new GetRemainderRequest();
        mGetRemainderRequest.addUrlParam("userName", AccountManager.getUserName());
        mGetRemainderRequest.setRequestListener(new RequestListener<GetRemainderModel>() {
            @Override
            public void onComplete() {
            }

            @Override
            public void onSuccess(GetRemainderModel result) {
                if (result == null) {
                    return;
                }
                if (result.mRemainder != null) {
                    mFeedHeadModule.updateRemainder(result.mRemainder.remainder, needCheckRemainder);
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
        HttpManager.addRequest(mGetRemainderRequest);
    }

    /**
     * 显示计划中已经出了的粮食份数
     */
    private GetOutedRequest mGetOutedRequest;

    public void getOutedCount() {
        if (mGetOutedRequest != null && !mGetOutedRequest.isFinish()) {
            return;
        }
        mGetOutedRequest = new GetOutedRequest();
        mGetOutedRequest.addUrlParam("userName", AccountManager.getUserName());
        mGetOutedRequest.setRequestListener(new RequestListener<OutedModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(OutedModel result) {
                mFeedHeadModule.updateOutCount(result.outedCount);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetOutedRequest);
    }


    /**
     * 将收到的称重信息更新到UI（Android）
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(WeightData data) {
        mFeedHeadModule.updateRemainder(data.quality, true);
        Log.i("weight", "剩余粮食重量更新成功");
    }

    /**
     * 将收到的称重信息更新到UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(FeedCountData data) {
        int out = Integer.parseInt(data.feedCount) * 10;
        int temp = Constant.LATESTWEIGHT - out;
        if (temp <= 0) {
            temp = 0;
            Constant.LATESTWEIGHT = 0;
        } else {
            Constant.LATESTWEIGHT -= out;
        }
        Log.i("weight........", String.valueOf(temp));
        mFeedHeadModule.updateRemainder(String.valueOf(temp), true);
        getOutedCount();
    }

    /**
     * 用户添加粮食后重新获取重量的SIP命令
     */
    private void updateWeight(String username) {
        UpdateWeightSipRequest mUpdateWeightRequest = new UpdateWeightSipRequest(username);
        SipUserManager.getInstance().addRequest(mUpdateWeightRequest);
        Log.i("update_weight", "正在发送更新重量的sip请求");
    }

    /**
     * 收到最新的重量后更新UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LatestWeightData result) {
        Constant.LATESTWEIGHT = Integer.parseInt(result.latestWeight);
        mFeedHeadModule.updateRemainder(result.latestWeight, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (mFeedHeadModule != null) {
                mFeedHeadModule.onVisible();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFeedHeadModule != null) {
            mFeedHeadModule.onVisible();
        }
    }
}
