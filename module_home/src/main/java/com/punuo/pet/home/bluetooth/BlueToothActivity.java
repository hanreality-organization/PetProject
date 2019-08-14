package com.punuo.pet.home.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.bluetooth.adapter.BluetoothInfoAdapter;
import com.punuo.pet.home.bluetooth.model.BaseInfo;
import com.punuo.pet.home.bluetooth.model.BluetoothInfo;
import com.punuo.pet.home.bluetooth.model.BluetoothLabel;
import com.punuo.pet.home.bluetooth.module.HeadModule;
import com.punuo.pet.home.bluetooth.service.BluetoothChatService;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by han.chen.
 * Date on 2019-08-12.
 * 蓝牙连接页
 **/
@RuntimePermissions
@Route(path = HomeRouter.ROUTER_BLUETOOTH_ACTIVITY)
public class BlueToothActivity extends BaseActivity {
    private static final String TAG = "BlueToothActivity";

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.content_list)
    RecyclerView mContentList;
    @BindView(R2.id.search_device)
    TextView mSearchDevice;

    private HeadModule mHeadModule;
    private BluetoothInfo mSelectBluetoothInfo;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothInfoAdapter mBluetoothInfoAdapter;
    private List<BaseInfo> mBluetoothDevices = new ArrayList<>();
    private Set<BluetoothDevice> mFindDevice = new HashSet<>(); //查找到的蓝牙设备

    private BluetoothChatService mBluetoothChatService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_bluetooth);
        ButterKnife.bind(this);
        initView();
        mBluetoothChatService = BluetoothChatService.getInstance();
        mBluetoothChatService.setHandler(mBaseHandler);
        registerBroadcast();
        //刷新当前已经有匹配过的蓝牙设备
        refreshBluetoothDevices();
    }

    private void initView() {
        mTitle.setText("连接蓝牙");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        LinearLayout headView = new LinearLayout(this);
        headView.setOrientation(LinearLayout.VERTICAL);
        mHeadModule = new HeadModule(this, headView, mBluetoothAdapter);
        headView.addView(mHeadModule.getRootView());
        mHeadModule.updateView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mContentList.setLayoutManager(layoutManager);
        mBluetoothInfoAdapter = new BluetoothInfoAdapter(this, new ArrayList<BaseInfo>());
        mBluetoothInfoAdapter.setHeaderView(headView);
        mBluetoothInfoAdapter.setItemClickListener(mClickListener);
        mBluetoothInfoAdapter.setLongClickListener(mLongClickListener);
        mContentList.setAdapter(mBluetoothInfoAdapter);

        mSearchDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    private void refresh() {
        mBluetoothInfoAdapter.clear();
        refreshBluetoothDevices();
        BluetoothLabel label = new BluetoothLabel();
        label.label = "可用设备";
        mBluetoothInfoAdapter.add(label);
        BlueToothActivityPermissionsDispatcher.searchBlueToothWithCheck(BlueToothActivity.this);
    }

    private BluetoothInfoAdapter.OnClickListener mClickListener = new BluetoothInfoAdapter.OnClickListener() {
        @Override
        public void click(BluetoothInfo bluetoothInfo) {
            mSelectBluetoothInfo = bluetoothInfo;
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            BluetoothDevice bluetoothDevice = bluetoothInfo.bluetoothDevice;
            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                //连接
                if (mBluetoothChatService.getState() == BluetoothChatService.STATE_CONNECTED) {
                    ToastUtils.showToast("已经与设备建立连接");
                    return;
                }
                ToastUtils.showToast("连接中...");
                mBluetoothChatService.connect(bluetoothDevice, false);
            } else {
                ToastUtils.showToast("蓝牙配对中...");
                BluetoothUtil.createBond(bluetoothDevice);
            }
        }
    };

    private BluetoothInfoAdapter.OnLongClickListener mLongClickListener = new BluetoothInfoAdapter.OnLongClickListener() {
        @Override
        public void click(BluetoothInfo bluetoothInfo) {
            BluetoothDevice bluetoothDevice = bluetoothInfo.bluetoothDevice;
            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
               BluetoothUtil.removeBond(bluetoothDevice);
            }
        }
    };

    /**
     * 获取已经配对的设备列表
     */
    private void refreshBluetoothDevices() {
        mBluetoothDevices.clear();
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        if (bondedDevices != null && !bondedDevices.isEmpty()) {
            for (BluetoothDevice bluetoothDevice : bondedDevices) {
                BluetoothInfo info = new BluetoothInfo();
                info.bluetoothDevice = bluetoothDevice;
                info.cellType = BaseInfo.TYPE_BLUETOOTH_MARCHED;
                info.isConnect = mSelectBluetoothInfo != null && (bluetoothDevice.equals(mSelectBluetoothInfo.bluetoothDevice)
                        && mSelectBluetoothInfo.isConnect);
                mBluetoothDevices.add(info);
            }
        }
        if (!mBluetoothDevices.isEmpty()) {
            BluetoothLabel label = new BluetoothLabel();
            label.label = "已配对的设备";
            mBluetoothDevices.add(0, label);
        }
        mBluetoothInfoAdapter.addAll(mBluetoothDevices);
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    void searchBlueTooth() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
        mFindDevice.clear();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!mFindDevice.contains(device)) {
                    mFindDevice.add(device);
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        BluetoothInfo info = new BluetoothInfo();
                        info.bluetoothDevice = device;
                        info.cellType = BaseInfo.TYPE_BLUETOOTH_FIND;
                        mBluetoothInfoAdapter.add(info);
                    }
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                dismissLoadingDialog();
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int state = bluetoothDevice.getBondState();
                if (state == BluetoothDevice.BOND_BONDED) {
                    ToastUtils.showToast("配对成功");
                    dismissLoadingDialog();
                    refresh();
                } else if (state == BluetoothDevice.BOND_NONE) {
                    ToastUtils.showToast("取消配对成功");
                    refresh();
                }
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //蓝牙与设备断开
                mSelectBluetoothInfo = null;
                refresh();
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                    case BluetoothAdapter.STATE_ON:
                        refresh();
                        mHeadModule.changeHomeHomeDetail(true);
                        startChatService();
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        mSelectBluetoothInfo = null;
                        mBluetoothInfoAdapter.clear();
                        mHeadModule.changeHomeHomeDetail(false);
                        break;
                    default:
                        break;
                }
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        BlueToothActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                    case BluetoothChatService.STATE_CONNECTED:
                        ToastUtils.showToast("连接成功");
                        if (mSelectBluetoothInfo != null) {
                            mSelectBluetoothInfo.isConnect = true;
                            mBluetoothInfoAdapter.notifyDataSetChanged();
                        }
                        mHeadModule.showWifiConfig();
                        break;
                    case BluetoothChatService.STATE_CONNECTING:
                        break;
                    case BluetoothChatService.STATE_LISTEN:
                        break;
                    case BluetoothChatService.STATE_NONE:
                        break;
                    default:
                        break;
                }
                break;
            case Constants.MESSAGE_READ:
                break;
            case Constants.MESSAGE_TOAST:
                ToastUtils.showToast(msg.getData().getString(Constants.TOAST));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHeadModule.onResume();
        if (mBluetoothAdapter.isEnabled()) {
            refresh();
            startChatService();
        }
    }

    /**
     * 开启通讯服务
     */
    private void startChatService() {
        if (mBluetoothChatService != null && (mBluetoothChatService.getState() == BluetoothChatService.STATE_NONE)) {
            mBluetoothChatService.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothChatService != null) {
            mBluetoothChatService.stop();
        }
        unregisterReceiver(mReceiver);
    }
}
