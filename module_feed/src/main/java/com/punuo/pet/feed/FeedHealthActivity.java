package com.punuo.pet.feed;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.punuo.pet.feed.model.FeedingAdviceModel;
import com.punuo.pet.feed.plan.GetPlanRequest;
import com.punuo.pet.feed.plan.PlanModel;
import com.punuo.pet.feed.request.GetFeedingAdviceRequest;
import com.punuo.pet.model.PetData;
import com.punuo.pet.router.FeedRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 * 健康喂养
 **/
@Route(path = FeedRouter.ROUTER_FEED_HEALTH_ACTIVITY)
public class FeedHealthActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.pet_avatar)
    RoundedImageView mPetAvatar;
    @BindView(R2.id.pet_nick)
    TextView mPetNick;
    @BindView(R2.id.feed_edit)
    TextView mFeedEdit;
    @BindView(R2.id.feed_more)
    TextView mFeedMore;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.feed_value)
    TextView mFeedValue;

    @Autowired(name = "petData")
    PetData mPetData;
    @BindView(R2.id.feed_daily)
    TextView feedDaily;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_health_activity);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitle.setText("健康喂养");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (mPetData != null) {
            Glide.with(this).load(mPetData.avatar).into(mPetAvatar);
            ViewUtil.setText(mPetNick, mPetData.petname);
        }
        mFeedMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(FeedRouter.ROUTER_FEED_ABOUT).navigation();
            }
        });
        mFeedEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(FeedRouter.ROUTER_ADD_FEED_PLAN_ACTIVITY).navigation();
            }
        });
        getFeedAdvice();
        getMyFrequency();
    }

    private GetFeedingAdviceRequest mGetFeedingAdviceRequest;

    private void getFeedAdvice() {
        if (mGetFeedingAdviceRequest != null && !mGetFeedingAdviceRequest.isFinish()) {
            return;
        }
        mGetFeedingAdviceRequest = new GetFeedingAdviceRequest();
        mGetFeedingAdviceRequest.addUrlParam("petname", mPetData.petname);
        mGetFeedingAdviceRequest.addUrlParam("username", AccountManager.getUserName());
        mGetFeedingAdviceRequest.setRequestListener(new RequestListener<FeedingAdviceModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(FeedingAdviceModel result) {
                if (result == null) {
                    return;
                }
                ViewUtil.setText(mFeedValue, result.mFeedingAdvice);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetFeedingAdviceRequest);
    }


    private GetPlanRequest mGetPlanRequest;

    public void getMyFrequency() {
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
                feedDaily.setText(result.planSum);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetPlanRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
