package com.punuo.pet.home.device;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
@Route(path = HomeRouter.ROUTER_BIND_DEVICE_ACTIVITY)
public class BindDeviceActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_bind_device_activity);
        ButterKnife.bind(this);
        mTitle.setText("设备列表");
        mBack.setOnClickListener(v -> onBackPressed());
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.attach_container);
        if (!(fragment instanceof BindDeviceFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.attach_container, new BindDeviceFragment(), "BindDeviceFragment")
                    .commitAllowingStateLoss();
        }
    }
}
