package com.punuo.pet.feed;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.loonggg.weekcalendar.view.WeekCalendar;
import com.punuo.pet.PetManager;
import com.punuo.pet.feed.feednow.FeedDialog;
import com.punuo.pet.feed.model.GetRemainderModel;
import com.punuo.pet.feed.plan.GetPlanRequest;
import com.punuo.pet.feed.plan.MyPlanAdapter;
import com.punuo.pet.feed.plan.Plan;
import com.punuo.pet.feed.plan.PlanModel;
import com.punuo.pet.feed.request.GetRemainderRequest;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.FeedRouter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.model.DevNotifyData;
import com.punuo.sip.model.LoginResponse;
import com.punuo.sip.model.OnLineData;
import com.punuo.sip.request.SipControlDeviceRequest;
import com.punuo.sip.request.SipOnLineRequest;
import com.punuo.sip.weight.WeightData;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.util.ViewUtil;

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
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.pet_container)
    LinearLayout mPetContainer;
    @BindView(R2.id.calendar_week)
    WeekCalendar mWeekCalendar;
    @BindView(R2.id.edit_feed_plan)
    View mEditFeedPlan;
    @BindView(R2.id.feed_right_now)
    View mFeedRightNow;
    @BindView(R2.id.wifistate)
    TextView mWifiState;
    @Autowired(name = "devId")
    String devId;
    @BindView(R2.id.remainder)
    TextView remainder;
    @BindView(R2.id.out)
    TextView out;
    @BindView(R2.id.plan)
    TextView plan;
    @BindView(R2.id.recycler_plan)
    RecyclerView mRecyclerPlan;
//    @BindView(R2.id.pull_to_refresh_feed)
//    PullToRefreshRecyclerView mPullToRefreshRecyclerView;


    private FeedDialog feedDialog;
    private MyPlanAdapter mMyPlanAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
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

        swipeRefreshLayout = (SwipeRefreshLayout) mFragmentView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        initPlan();
//                        Log.i("plan", "重新刷新 ");
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                }).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        initPlan();
                        mFragmentView.invalidate();
                        Log.i("plan", "重新刷新 ");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

        return mFragmentView;
    }

    private void initView() {
//        mPullToRefreshRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);//设置刷新模式
//        mRecyclerPlan = mPullToRefreshRecyclerView.getRefreshableView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerPlan.setLayoutManager(layoutManager);
        mMyPlanAdapter = new MyPlanAdapter(getActivity(), new ArrayList<Plan>());
        mRecyclerPlan.setAdapter(mMyPlanAdapter);
        mTitle.setText("梦视宠物喂食器");
        mBack.setVisibility(View.GONE);
        out.setText("0");
        remainder.setText("0.0");

//        mPullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
//                initPlan();
//                mPullToRefreshRecyclerView.onRefreshComplete();
//            }
//        });

        mWeekCalendar.setOnDateClickListener(new WeekCalendar.OnDateClickListener() {
            @Override
            public void onDateClick(String s) {
                ToastUtils.showToast(s);
            }
        });

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

        initPlan();
        getRemainderQuality(AccountManager.getUserName());
        IsonLine();
    }

    public void showFeedDialog() {
        feedDialog = new FeedDialog(getContext(), R.layout.feed_right_now, new int[]{R.id.count, R.id.sub_count, R.id.add_count, R.id.complete});
        feedDialog.show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PetModel petModel) {
        initPetInfo(petModel);
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
        if(live==0){
            mWifiState.setBackgroundColor(Color.parseColor("#ff0000"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginResponse event) {
        int live=Integer.parseInt(event.live);
        if (live == 1) {
            mWifiState.setBackgroundColor(Color.parseColor("#8BC34A"));
        }
        if(live==0){
            mWifiState.setBackgroundColor(Color.parseColor("#ff0000"));
        }
    }

    private void initPetInfo(PetModel petModel) {
        if (petModel == null || petModel.mPets == null) {
            return;
        }
        mPetContainer.removeAllViews();
        for (int i = 0; i < petModel.mPets.size(); i++) {
            PetData petData = petModel.mPets.get(i);
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.feed_pet_info_item, mPetContainer, false);
            ImageView avatar = view.findViewById(R.id.pet_avatar);
            TextView petName = view.findViewById(R.id.pet_name);
            Glide.with(this).load(petData.avatar).into(avatar);
            ViewUtil.setText(petName, petData.petname);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mPetContainer.addView(view);
        }
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.feed_add_item, mPetContainer, false);
        mPetContainer.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY).navigation();
            }
        });
    }

    private void operateControl(String operate) {
        SipControlDeviceRequest sipControlDeviceRequest = new SipControlDeviceRequest(operate, devId);
        SipUserManager.getInstance().addRequest(sipControlDeviceRequest);
    }

    private void IsonLine(){
        SipOnLineRequest sipOnLineRequest=new SipOnLineRequest();
        SipUserManager.getInstance().addRequest(sipOnLineRequest);
    }

    private GetPlanRequest mGetPlanRequest;

    public void initPlan() {
        if (mGetPlanRequest != null && mGetPlanRequest.isFinish()) {
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
                mMyPlanAdapter.clear();
                mMyPlanAdapter.addAll(result.mPlanList);
                mMyPlanAdapter.notifyDataSetChanged();
                plan.setText(result.feedCountSum);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetPlanRequest);
    }

    //TODO 初始化剩余重量
    private GetRemainderRequest mGetRemainderRequest;

    public void getRemainderQuality(String username) {
        if (mGetRemainderRequest != null && mGetRemainderRequest.isFinish()) {
            return;
        }
        mGetRemainderRequest = new GetRemainderRequest();
        mGetRemainderRequest.addUrlParam("userName", username);
        mGetRemainderRequest.setRequestListener(new RequestListener<GetRemainderModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(GetRemainderModel result) {
                if (result == null) {
                    return;
                }
                remainder.setText(result.mRemainder.remainder);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetRemainderRequest);
    }

    /**
     * 将收到的称重信息更新到UI
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(WeightData data) {
        float fQuality = Float.parseFloat(data.quality);
        double lastQuality = Math.round((-(fQuality - 1170) / 5.88));//对结果四舍五入
        remainder.setText(String.valueOf(lastQuality));
        Log.i("weight", "剩余粮食重量更新成功");
        //TODO 将获得称重信息更新到主界面的UI
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getInitQuality(String initQuality) {
        float fQuality = Float.parseFloat(initQuality);
        double lastQuality = Math.round((fQuality / 5.5));//对结果四舍五入
        remainder.setText(String.valueOf(lastQuality));
        Log.i("weight", "剩余粮食获取成功");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
