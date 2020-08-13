package com.punuo.pet.feed;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.punuo.pet.PetManager;
import com.punuo.pet.feed.adapter.FeedShowAdapter;
import com.punuo.pet.feed.adapter.FeedViewAdapter;
import com.punuo.pet.feed.feednow.FeedDialog;
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
import com.punuo.sip.model.DevNotifyData;
import com.punuo.sip.model.FeedCountData;
import com.punuo.sip.model.LatestWeightData;
import com.punuo.sip.model.LoginResponse;
import com.punuo.sip.model.OnLineData;
import com.punuo.sip.request.SipOnLineRequest;
import com.punuo.sip.weight.WeightData;
import com.punuo.sys.sdk.Constant;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.punuo.pet.feed.get_real_weight.UpdateWeightSipRequest;

/**
 * Created by han.chen.
 * Date on 2019-09-12.
 **/
@Route(path = FeedRouter.ROUTER_FEED_HOME_FRAGMENT)
public class FeedFragment extends BaseFragment {
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.edit_feed_plan)
    View mEditFeedPlan;
    @BindView(R2.id.feed_right_now)
    View mFeedRightNow;
    @BindView(R2.id.wifistate)
    TextView mWifiState;
    @Autowired(name = "devId")
    String devId;
    @BindView(R2.id.pull_to_refresh)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    @BindView(R2.id.real_weight)
    LinearLayout mRealWeight;

    private RecyclerView mRecyclerView;
    private FeedHeadModule mFeedHeadModule;
    private static String devid;
    private FeedDialog feedDialog;
//    private FeedViewAdapter mFeedViewAdapter;
    private FeedShowAdapter mFeedViewAdapter;
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.feed_fragment_home, container, false);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this, mFragmentView);
        initView();
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        EventBus.getDefault().register(this);
        PetManager.getPetInfo();
        devId = "310023005801930001";
        Toast.makeText(getActivity(), "点击左上角WiFi按钮绑定设备并为设备连接WiFi", Toast.LENGTH_LONG).show();
        return mFragmentView;
    }

    private void initView() {
        mTitle.setText("梦视宠物喂食器");
        mBack.setVisibility(View.GONE);

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
        mWifiState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_BIND_DEVICE_ACTIVITY).navigation();
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

        mRecyclerView.setAdapter(mFeedViewAdapter);

        mPullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                getPlan();
                getRemainderQuality();
                getOutedCount();
                IsonLine();
            }
        });


        getPlan();
        getRemainderQuality();
        getOutedCount();
        IsonLine();
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
        if (result.mDevInfo.live == 0) {
            mWifiState.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        if (result.mDevInfo.live == 1) {
            mWifiState.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(OnLineData result) {
        int live = Integer.parseInt(result.live);
        if (live == 1) {
            mWifiState.setBackgroundColor(Color.parseColor("#8BC34A"));
        } else {
            mWifiState.setBackgroundColor(Color.parseColor("#ff0000"));
        }
        if (live == 0) {
            mWifiState.setBackgroundColor(Color.parseColor("#ff0000"));
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginResponse event) {
        int live = Integer.parseInt(event.live);
        if (live == 1) {
            mWifiState.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
        if (live == 0) {
            mWifiState.setBackgroundColor(Color.parseColor("#ff0000"));
        }
    }

    private void IsonLine() {
        SipOnLineRequest sipOnLineRequest = new SipOnLineRequest();
        SipUserManager.getInstance().addRequest(sipOnLineRequest);
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

    public void getRemainderQuality() {
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
                mFeedHeadModule.updateRemainder(result.mRemainder.remainder);
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
        mFeedHeadModule.updateRemainder(data.quality);
        Log.i("weight", "剩余粮食重量更新成功");
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void getInitQuality(String initQuality) {
//        float fQuality = Float.parseFloat(initQuality);
//        double lastQuality = Math.round((fQuality / 5.5));//对结果四舍五入
//        remainder.setText(String.valueOf(lastQuality));
//        Log.i("weight", "剩余粮食获取成功");
//    }

    /**
     * 将收到的称重信息更新到UI
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(FeedCountData data) {
        int out = Integer.parseInt(data.feedCount)*10;
        int temp = Constant.LATESTWEIGHT-out;
        if(temp<=0){
            temp=0;
            Constant.LATESTWEIGHT = 0;
        }else{
            Constant.LATESTWEIGHT-=out;
        }
        Log.i("weight........", String.valueOf(temp));
        mFeedHeadModule.updateRemainder(String.valueOf(temp));
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
    public void onMessageEvent(LatestWeightData reuslt) {
        Constant.LATESTWEIGHT = Integer.parseInt(reuslt.latestWeight);
        mFeedHeadModule.updateRemainder(reuslt.latestWeight);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }
}
