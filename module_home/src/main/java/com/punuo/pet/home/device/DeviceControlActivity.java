package com.punuo.pet.home.device;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipControlDeviceRequest;
import com.punuo.sys.sdk.activity.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_control_activity);
        ButterKnife.bind(this);

        mLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    operateControl("left");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    operateControl("stop");
                }
                return true;
            }
        });

        mRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    operateControl("right");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    operateControl("stop");
                }
                return true;
            }
        });
    }

    private void operateControl(String operate) {
        SipControlDeviceRequest sipControlDeviceRequest = new SipControlDeviceRequest(operate);
        SipUserManager.getInstance().addRequest(sipControlDeviceRequest);
    }

}
