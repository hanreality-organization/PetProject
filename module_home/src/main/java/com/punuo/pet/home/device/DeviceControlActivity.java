package com.punuo.pet.home.device;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.feed.request.GetWeightInfoRequest;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipControlDeviceRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-17.
 **/
@Route(path = HomeRouter.ROUTER_DEVICE_CONTROL_ACTIVITY)
public class DeviceControlActivity extends BaseActivity {

    @BindView(R2.id.left)
    Button mLeft;
    @BindView(R2.id.right)
    Button mRight;
    @BindView(R2.id.get_weight)
    Button mGetWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_control_activity);
        ButterKnife.bind(this);
        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator animator = ValueAnimator.ofInt(1, 4000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        operateControl("left");
                    }
                });
                animator.setDuration(4000);
                animator.start();
            }
        });

        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator animator = ValueAnimator.ofInt(1, 4000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        operateControl("right");
                    }
                });
                animator.setDuration(4000);
                animator.start();
            }
        });

        mGetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeightInfo("321000000200150001");
            }
        });
    }

    private void operateControl(String operate) {
        SipControlDeviceRequest sipControlDeviceRequest = new SipControlDeviceRequest(operate);
        SipUserManager.getInstance().addRequest(sipControlDeviceRequest);
    }

    private GetWeightInfoRequest mGetWeightInfoRequest;
    private void getWeightInfo(String devId) {
        if (mGetWeightInfoRequest != null && !mGetWeightInfoRequest.isFinish()) {
            return;
        }
        mGetWeightInfoRequest = new GetWeightInfoRequest();
        mGetWeightInfoRequest.addUrlParam("username", AccountManager.getUserName());
        mGetWeightInfoRequest.addUrlParam("devid", devId);
        mGetWeightInfoRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetWeightInfoRequest);
    }
}
