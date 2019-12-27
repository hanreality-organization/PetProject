package com.punuo.pet.home.wifi;

import android.os.Bundle;
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
import com.punuo.sip.request.SipSendWiFiDataRequest;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_hotspot_wifi);
        ButterKnife.bind(this);
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
    }
private void Send(String input,String pwd){
    SipSendWiFiDataRequest sipSendWiFiDataRequest=new SipSendWiFiDataRequest(input,pwd);
    SipUserManager.getInstance().addRequest(sipSendWiFiDataRequest);
}
}
