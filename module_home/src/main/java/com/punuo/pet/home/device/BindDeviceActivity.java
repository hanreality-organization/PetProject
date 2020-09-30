package com.punuo.pet.home.device;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.device.adapter.DeviceInfoAdapter;
import com.punuo.pet.home.device.model.DeviceInfo;
import com.punuo.pet.home.device.model.DeviceModel;
import com.punuo.pet.home.device.request.BindDeviceRequest;
import com.punuo.pet.home.device.request.CheckBindDeviceRequest;
import com.punuo.pet.home.device.request.GetBindDeviceRequest;
import com.punuo.pet.home.device.request.JoinGroupRequest;
import com.punuo.pet.router.HomeRouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.sip.dev.BindDevSuccessEvent;
import com.punuo.sip.dev.UnBindDevSuccessEvent;
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
public class BindDeviceActivity extends BaseSwipeBackActivity implements View.OnClickListener{
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

    private TextView saoma;
    private TextView shoudong;
    private TextView cancel;
    private View inflate;
    private Dialog dialog;
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
                try {
                    show(v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDeviceList.setLayoutManager(layoutManager);
        mDeviceInfoAdapter = new DeviceInfoAdapter(this, new ArrayList<DeviceInfo>());
        mDeviceList.setAdapter(mDeviceInfoAdapter);
        showLoadingDialog("加载中...");
        refresh();
        Toast.makeText(this,"绑定设备后点击设备号开始连接WiFi",Toast.LENGTH_LONG).show();
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
    public void onMessageEvent(UnBindDevSuccessEvent event) {
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

    public void show(View view){
        dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.home_dialog_layout, null);
        //初始化控件
        saoma=inflate.findViewById(R.id.tv_saoma);
        shoudong=inflate.findViewById(R.id.tv_shoudong);
        cancel=inflate.findViewById(R.id.cancel);
        saoma.setOnClickListener(this);
        shoudong.setOnClickListener(this);
        cancel.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        Display display=getWindowManager().getDefaultDisplay();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width=(int)display.getWidth();
        lp.y = 20;//设置Dialog距离底部的距离
         // 将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
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

    private void bindDevice(final String devId) {
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
                    EventBus.getDefault().post(new BindDevSuccessEvent());
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

    private void joinDevice(final String devId) {
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
                    EventBus.getDefault().post(new BindDevSuccessEvent());
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
    public String devId;
    @Override
    public void onClick(View view) {
        int id=view.getId();

        if(id==R.id.tv_saoma){
            BindDeviceActivityPermissionsDispatcher.openScanWithPermissionCheck(BindDeviceActivity.this);
            dialog.dismiss();
        }else if(id==R.id.tv_shoudong){
            final EditText editText=new EditText(this);
            new AlertDialog.Builder(this).setTitle("请输入设备号").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    devId=editText.getText().toString();
                    checkBindDevice(devId);
                    dialog.dismiss();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            }).show();
        }else if(id==R.id.cancel){
            dialog.dismiss();
        }
    }
}
