package com.punuo.pet.home.device;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.device.adapter.DeviceInfoAdapter;
import com.punuo.pet.home.device.event.UnBindDeviceEvent;
import com.punuo.pet.home.device.model.DeviceInfo;
import com.punuo.pet.home.device.model.DeviceModel;
import com.punuo.pet.home.device.request.BindDeviceRequest;
import com.punuo.pet.home.device.request.CheckBindDeviceRequest;
import com.punuo.pet.home.device.request.GetBindDeviceRequest;
import com.punuo.pet.home.device.request.JoinGroupRequest;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
@RuntimePermissions
@Route(path = HomeRouter.ROUTER_BIND_DEVICE_ACTIVITY)
public class BindDeviceActivity extends BaseSwipeBackActivity {
    private static final int QR_SCAN_REQUEST_CODE = 1;


    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.device_list)
    RecyclerView mDeviceList;
    @BindView(R2.id.text_empty)
    TextView mTextEmpty;

    private DeviceInfoAdapter mDeviceInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_bind_device_activity);
        ButterKnife.bind(this);
        mTitle.setText("设备列表");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText("添加设备");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindDeviceActivityPermissionsDispatcher.openScanWithCheck(BindDeviceActivity.this);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDeviceList.setLayoutManager(layoutManager);
        mDeviceInfoAdapter = new DeviceInfoAdapter(this, new ArrayList<DeviceInfo>());
        mDeviceList.setAdapter(mDeviceInfoAdapter);
        showLoadingDialog("加载中...");
        refresh();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void openScan() {
        ARouter.getInstance().build(SDKRouter.ROUTER_QR_SCAN_ACTIVITY)
                .navigation(this, QR_SCAN_REQUEST_CODE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void openScanError() {
        ToastUtils.showToast("权限获取失败");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BindDeviceActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCAN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String result = data.getStringExtra("result");
                    Log.v("BindDeviceActivity", "result = " + result);
                    showLoadingDialog("绑定设备中...");
                    checkBindDevice(result);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UnBindDeviceEvent event) {
        refresh();
    }

    private void updateView(List<DeviceInfo> deviceInfoList) {
        if (deviceInfoList == null || deviceInfoList.isEmpty()) {
            mTextEmpty.setVisibility(View.VISIBLE);
            mDeviceList.setVisibility(View.GONE);
            return;
        }
        mDeviceList.setVisibility(View.VISIBLE);
        mTextEmpty.setVisibility(View.GONE);
        mDeviceInfoAdapter.appendData(deviceInfoList);
    }

    private void refresh() {
        getDeviceInfo();
    }

    private GetBindDeviceRequest mGetBindDeviceRequest;

    private void getDeviceInfo() {
        if (mGetBindDeviceRequest != null && !mGetBindDeviceRequest.isFinish()) {
            return;
        }
        mGetBindDeviceRequest = new GetBindDeviceRequest();
        mGetBindDeviceRequest.addUrlParam("username", AccountManager.getUserName());
        mGetBindDeviceRequest.setRequestListener(new RequestListener<DeviceModel>() {
            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(DeviceModel result) {
                if (result == null) {
                    return;
                }
                updateView(result.mDeviceInfoList);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mGetBindDeviceRequest);
    }

    private BindDeviceRequest mBindDeviceRequest;

    private void bindDevice(String devId) {
        if (mBindDeviceRequest != null && !mBindDeviceRequest.isFinish()) {
            return;
        }
        mBindDeviceRequest = new BindDeviceRequest();
        mBindDeviceRequest.addUrlParam("devid", devId);
        mBindDeviceRequest.addUrlParam("username", AccountManager.getUserName());
        mBindDeviceRequest.setRequestListener(new RequestListener<BaseModel>() {
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
                    refresh();
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(mBindDeviceRequest);
    }

    private JoinGroupRequest mJoinGroupRequest;

    private void joinDevice(String devId) {
        if (mJoinGroupRequest != null && !mJoinGroupRequest.isFinish()) {
            return;
        }
        mJoinGroupRequest = new JoinGroupRequest();
        mJoinGroupRequest.addUrlParam("devid", devId);
        mJoinGroupRequest.addUrlParam("username", AccountManager.getUserName());
        mJoinGroupRequest.setRequestListener(new RequestListener<BaseModel>() {
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
                    refresh();
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(mJoinGroupRequest);
    }

    private CheckBindDeviceRequest mCheckBindDeviceRequest;

    private void checkBindDevice(final String devId) {
        if (mCheckBindDeviceRequest != null && !mCheckBindDeviceRequest.isFinish()) {
            return;
        }
        mCheckBindDeviceRequest = new CheckBindDeviceRequest();
        mCheckBindDeviceRequest.addUrlParam("devid", devId);
        mCheckBindDeviceRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    bindDevice(devId);
                } else {
                    joinDevice(devId);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mCheckBindDeviceRequest);
    }
}
