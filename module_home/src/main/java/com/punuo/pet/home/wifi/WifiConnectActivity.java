package com.punuo.pet.home.wifi;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.bluetooth.Constants;
import com.punuo.pet.home.bluetooth.service.BluetoothChatService;
import com.punuo.pet.home.wifi.adapter.WifiAdapter;
import com.punuo.pet.home.wifi.model.PTOMessage;
import com.punuo.pet.home.wifi.model.WifiMessage;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.util.ToastUtils;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-14.
 **/
@Route(path = HomeRouter.ROUTER_WIFI_CONNECT_ACTIVITY)
public class WifiConnectActivity extends BaseActivity {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.wifi_list)
    RecyclerView mWifiList;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;

    private BluetoothChatService mBluetoothChatService;
    private WifiManager mWifiManager;
    private WifiAdapter mWifiAdapter;
    private WifiReceiver mWifiReceiver = new WifiReceiver();
    private AlertDialog mWifiConnectDialog;
    private WifiController mWifiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_wifi_connect_activity);
        ButterKnife.bind(this);
        mTitle.setText("无线连接");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSubTitle.setText("扫描");
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWifi();
            }
        });
        mBluetoothChatService = BluetoothChatService.getInstance();
        mBluetoothChatService.setHandler(mBaseHandler);
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!WifiUtil.isWifiEnable(mWifiManager)) {
            mWifiManager.setWifiEnabled(true);
        }
        mWifiController = new WifiController(this, mWifiManager, true);
        initView();
        registerReceiver(mWifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        //扫描wifi
        scanWifi();
    }

    private void scanWifi() {
        mWifiManager.startScan();
        showLoadingDialog("扫描中...");
    }

    private void initView() {
        mWifiAdapter = new WifiAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mWifiList.setLayoutManager(layoutManager);
        mWifiList.setAdapter(mWifiAdapter);
        mWifiAdapter.setItemClickListener(new WifiAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ScanResult result) {
                connectWifi(result);
            }
        });
    }


    private void connectWifi(final ScanResult result) {
        if (mWifiConnectDialog != null && mWifiConnectDialog.isShowing()) {
            mWifiConnectDialog.dismiss();
        }
        View contentView = LayoutInflater.from(this).inflate(R.layout.home_connect_wifi_dialog, null);
        final EditText editPWD = contentView.findViewById(R.id.edit_password);
        mWifiConnectDialog = new AlertDialog.Builder(this)
                .setTitle(result.SSID)
                .setView(contentView)
                .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String pwd = editPWD.getText().toString().trim();
                        if (TextUtils.isEmpty(pwd) || pwd.length() < 8) {
                            ToastUtils.showToast("密码至少8位");
                            return;
                        }
                        int wifiType = 0;
                        if (result.capabilities.contains("WEP")) {
                            wifiType = 1;
                        } else if (result.capabilities.contains("WPA")) {
                            wifiType = 2;
                        }

                        WifiMessage wifiMessage = new WifiMessage();
                        wifiMessage.pwd = pwd;
                        wifiMessage.wifiType = wifiType;
                        wifiMessage.BSSID = result.BSSID;
                        wifiMessage.SSID = result.SSID;
                        mWifiController.connectWifi(wifiMessage);
                        mWifiController.setClientWifiListener(new OnClientWifiListener() {
                            @Override
                            public void OnClientConnected(WifiInfo wifiInfo, WifiMessage wifiMessage) {
                                Log.v("WifiConnectActivity", "OnClientConnected: 连接成功");
                                PTOMessage ptoMessage = new PTOMessage();
                                ptoMessage.data = JSON.toJSONString(wifiMessage);
                                ptoMessage.type = Constants.CLIENT;
                                ptoMessage.msg = "wifi信息";
                                byte[] sendData = JSON.toJSONString(ptoMessage).getBytes();
                                mBluetoothChatService.write(sendData);
                            }

                            @Override
                            public void OnClientConnecting() {
                                Log.v("WifiConnectActivity", "OnClientConnecting: 连接中");
                            }

                            @Override
                            public void OnClientDisConnect() {
                                Log.v("WifiConnectActivity", "OnClientDisConnect: 失去连接");
                            }
                        });

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        mWifiConnectDialog.setCancelable(false);
        mWifiConnectDialog.setCanceledOnTouchOutside(false);
        mWifiConnectDialog.show();
    }

    public class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                List<ScanResult> scanResults = mWifiManager.getScanResults();
                mWifiAdapter.addAll(filterScanResult(scanResults));
                dismissLoadingDialog();
            }
        }
    }

    /**
     * 以 SSID 为关键字，过滤掉信号弱的选项
     *
     * @param list
     * @return
     */
    public List<ScanResult> filterScanResult(final List<ScanResult> list) {
        LinkedHashMap<String, ScanResult> linkedMap = new LinkedHashMap<>(list.size());
        for (ScanResult rst : list) {
            ScanResult scanResult = linkedMap.get(rst.SSID);
            if (scanResult != null) {
                if (rst.level > scanResult.level) {
                    linkedMap.put(rst.SSID, rst);
                }
                continue;
            }
            linkedMap.put(rst.SSID, rst);
        }
        list.clear();
        list.addAll(linkedMap.values());
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseHandler.removeCallbacksAndMessages(null);
        if (mWifiReceiver != null) {
            unregisterReceiver(mWifiReceiver);
        }
        if (mWifiController != null) {
            mWifiController.unregisterStateReceiver();
        }
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case Constants.MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                String readMessage = new String(readBuf, 0, msg.arg1);
                PTOMessage ptoMessage = null;
                try {
                    ptoMessage = JSON.parseObject(readMessage, PTOMessage.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ptoMessage != null) {
                    ToastUtils.showToast(ptoMessage.msg);
                    if (ptoMessage.type == Constants.SUCCESS) {
                        finish();
                    }
                }
                break;
        }
    }
}
