package com.punuo.pet.home.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.model.SendWiFiResponse;
import com.punuo.sip.model.WiFiConnectedSuccessData;
import com.punuo.sip.request.SipSendWiFiDataRequest;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = HomeRouter.ROUTER_HOTSPOT_CONNECT_WIFI)
public class HotSpotConnectWifi extends BaseSwipeBackActivity {

@BindView(R2.id.title)
TextView mtitle;
@BindView(R2.id.back)
ImageView mback;
@BindView(R2.id.input)
EditText minput;
@BindView(R2.id.pwd)
EditText mpwd;
@BindView(R2.id.send)
Button msend;
@BindView(R2.id.setting)
Button Setting;
@BindView(R2.id.connected_wifi_name)
TextView connectedwifiname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_hotspot_wifi);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mtitle.setText("热点连接WiFi");
        mback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        msend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input=minput.getText().toString();
                String pwd=mpwd.getText().toString();
                Send(input,pwd);
            }
        });
        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
    }
private void Send(String input,String pwd){
    SipSendWiFiDataRequest sipSendWiFiDataRequest=new SipSendWiFiDataRequest(input,pwd);
    SipUserManager.getInstance().addRequest(sipSendWiFiDataRequest);
}

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WiFiConnectedSuccessData result){
        Toast.makeText(HotSpotConnectWifi.this,"设备已成功连上网络"+result.success,Toast.LENGTH_LONG).show();
        connectedwifiname.setText("当前设备连接的网络为"+result.success);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SendWiFiResponse result){
        Toast.makeText(HotSpotConnectWifi.this,"开始连接",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
