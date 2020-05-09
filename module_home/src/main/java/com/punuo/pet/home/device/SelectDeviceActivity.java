package com.punuo.pet.home.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.event.SelectDeviceEvent;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.device.model.DeviceData;
import com.punuo.pet.router.DeviceType;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2020/5/4.
 **/
@Route(path = HomeRouter.ROUTER_SELECT_DEVICE_ACTIVITY)
public class SelectDeviceActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.left_text)
    TextView mLeftText;
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.device_container)
    ViewGroup mDeviceContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_select_device_activity);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        mTitle.setText("选择设备");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
    }

    private void initData() {
        List<DeviceData> list = getDeviceDataList();
        for (int i = 0; i < list.size(); i++) {
            final DeviceData deviceData = list.get(i);
            View item = LayoutInflater.from(this).inflate(R.layout.home_select_device_item, mDeviceContainer, false);
            TextView name = item.findViewById(R.id.device_name);
            ImageView icon = item.findViewById(R.id.device_icon);
            name.setText(deviceData.deviceName);
            icon.setImageResource(deviceData.drawableId);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deviceData.deviceType == DeviceType.MAOCE) {
                        ToastUtils.showToast("设备即将上线，敬请期待");
                        return;
                    }
                    EventBus.getDefault().post(new SelectDeviceEvent(deviceData.deviceType));
                    scrollToFinishActivity();
                }
            });
            mDeviceContainer.addView(item);
        }
    }

    private List<DeviceData> getDeviceDataList() {
        List<DeviceData> list = new ArrayList<>();
        list.add(new DeviceData("梦视智能宠物喂食器", R.drawable.home_device_feed, DeviceType.FEED));
        list.add(new DeviceData("梦视智能宠物喂食器", R.drawable.home_device_maoce, DeviceType.MAOCE));
        return list;
    }
}
