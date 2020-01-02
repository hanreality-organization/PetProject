package com.punuo.pet.home.device.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.device.event.UnBindDeviceEvent;
import com.punuo.pet.home.device.model.DeviceInfo;
import com.punuo.pet.home.device.request.UnBindDeviceRequest;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class DeviceInfoVH extends BaseViewHolder<DeviceInfo> {

    @BindView(R2.id.device_icon)
    RoundedImageView mDeviceIcon;
    @BindView(R2.id.device_id)
    TextView mDeviceId;
    @BindView(R2.id.device_unbind)
    TextView mDeviceUnbind;
    @BindView(R2.id.device_time)
    TextView mDeviceTime;

    protected Context mContext;

    public DeviceInfoVH(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.home_recycle_dev_item, parent, false));
        mContext = context;
        ButterKnife.bind(this, itemView);
    }

    @Override
    protected void bindData(final DeviceInfo deviceInfo, int position) {
        ViewUtil.setText(mDeviceId, deviceInfo.devid);
        ViewUtil.setText(mDeviceTime, deviceInfo.createTime);
        mDeviceUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindDevice(deviceInfo.devid);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_HOTSPOT_CONNECT_WIFI)
                        .navigation();
            }
        });
    }

    private UnBindDeviceRequest mUnBindDeviceRequest;

    private void unBindDevice(String devId) {
        if (mUnBindDeviceRequest != null && !mUnBindDeviceRequest.isFinish()) {
            return;
        }
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingDialog("解绑设备中...");
        }
        mUnBindDeviceRequest = new UnBindDeviceRequest();
        mUnBindDeviceRequest.addUrlParam("devid", devId);
        mUnBindDeviceRequest.addUrlParam("username", AccountManager.getUserName());
        mUnBindDeviceRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                ToastUtils.showToast(result.message);
                if (result.success) {
                    EventBus.getDefault().post(new UnBindDeviceEvent());
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mUnBindDeviceRequest);
    }
}
