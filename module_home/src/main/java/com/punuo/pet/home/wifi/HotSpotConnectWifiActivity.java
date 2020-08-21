package com.punuo.pet.home.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

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
    @BindView(R2.id.sub_title)
    TextView mSubTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_hotspot_wifi);
        ButterKnife.bind(this);
        mSubTitle.setText("去选择");
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_WIFI_CHOOSE_ACTIVITY)
                        .navigation(HotSpotConnectWifiActivity.this, 5);
            }
        });
        title.setText("AP模式连接WiFi");
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
                Toast.makeText(HotSpotConnectWifiActivity.this, "发送WiFi信息成功", Toast.LENGTH_SHORT).show();
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

    private void Send(String msg1, String msg2) {
        final String message1 = msg1;
        final String message2 = msg2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = null;
                    socket = new Socket("192.168.1.108", 1234);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(message1 + "+" + message2);
                    writer.flush();
                    writer.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                String wifiName = data.getExtras().getString("wifiName", "");
                input.setText(wifiName);
            }
        }
    }
}
