package com.punuo.pet.home.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.model.SendWiFiResponse;
import com.punuo.sip.model.WiFiConnectedSuccessData;
import com.punuo.sip.request.SipSendWiFiDataRequest;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = HomeRouter.ROUTER_HOTSPOT_CONNECT_WIFI)
public class HotSpotConnectWifiActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.input)
    EditText input;
    @BindView(R2.id.pwd)
    EditText pwd;
    @BindView(R2.id.send)
    Button send;
    @BindView(R2.id.setting)
    Button setting;
    @BindView(R2.id.connected_wifi_name)
    TextView connectedWifiName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_hotspot_wifi);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        title.setText("热点连接WiFi");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = HotSpotConnectWifiActivity.this.input.getText().toString();
                String pwd = HotSpotConnectWifiActivity.this.pwd.getText().toString();
                Send(input, pwd);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
    }

    private void Send(String input, String pwd) {
        SipSendWiFiDataRequest sipSendWiFiDataRequest = new SipSendWiFiDataRequest(input, pwd);
        SipUserManager.getInstance().addRequest(sipSendWiFiDataRequest);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WiFiConnectedSuccessData result) {
        ToastUtils.showToast("设备已成功连上网络" + result.success);
        connectedWifiName.setText("当前设备连接的网络为" + result.success);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendWiFiResponse result) {
        ToastUtils.showToast("开始连接");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
