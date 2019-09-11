package com.punuo.pet.home.bluetooth.module;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-13.
 **/
public class HeadModule {
    public static final int WIFI_CONNECT_REQUEST_CODE = 2;

    @BindView(R2.id.home_bluetooth_switch)
    CheckBox mHomeBluetoothSwitch;
    @BindView(R2.id.bluetooth_container)
    RelativeLayout mBluetoothContainer;
    @BindView(R2.id.home_bluetooth_name)
    TextView mHomeBluetoothName;
    @BindView(R2.id.home_device_info)
    RelativeLayout mHomeDeviceInfo;
    @BindView(R2.id.home_device_wifi)
    RelativeLayout mHomeDeviceWifi;
    @BindView(R2.id.home_detail_container)
    LinearLayout mHomeDetailContainer;

    private View mRootView;
    private Activity mActivity;
    private BluetoothAdapter mBluetoothAdapter;

    public HeadModule(Activity activity, ViewGroup parent, BluetoothAdapter bluetoothAdapter) {
        mActivity = activity;
        mBluetoothAdapter = bluetoothAdapter;
        mRootView = LayoutInflater.from(activity)
                .inflate(R.layout.home_recycler_bluetooth_head, parent, false);
        ButterKnife.bind(this, mRootView);
    }

    public View getRootView() {
        return mRootView;
    }

    public void updateView() {
        mHomeBluetoothName.setText(mBluetoothAdapter.getName());
        mHomeBluetoothSwitch.setChecked(mBluetoothAdapter.isEnabled());
        if (mBluetoothAdapter.isEnabled()) {
            mHomeDetailContainer.setVisibility(View.VISIBLE);
        } else {
            mHomeDetailContainer.setVisibility(View.GONE);
        }
        mHomeBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                } else {
                    mBluetoothAdapter.enable();
                }

            }
        });
    }

    public void showWifiConfig() {
        mHomeDeviceWifi.setVisibility(View.VISIBLE);
        mHomeDeviceWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_WIFI_CONNECT_ACTIVITY)
                        .navigation(mActivity, WIFI_CONNECT_REQUEST_CODE);
            }
        });
    }

    public void changeHomeHomeDetail(boolean open) {
        if (open) {
            mHomeDetailContainer.setVisibility(View.VISIBLE);
        } else {
            mHomeDetailContainer.setVisibility(View.GONE);
        }
    }

    public void onResume() {
        updateView();
    }
}
