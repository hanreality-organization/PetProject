package com.punuo.pet.home.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.ToastUtils;

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
    View send;
    @BindView(R2.id.setting)
    View setting;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.authentication_type)
    Spinner mAuthenticationType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_hotspot_wifi);
        ButterKnife.bind(this);
        mSubTitle.setText(R.string.string_choose);
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(HomeRouter.ROUTER_WIFI_CHOOSE_ACTIVITY)
                        .navigation(HotSpotConnectWifiActivity.this, 5);
            }
        });
        title.setText(R.string.string_wifi_connected);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this, R.array.authentication_type, R.layout.support_simple_spinner_dropdown_item);
        mAuthenticationType.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = input.getText().toString().trim();
                String pwdText = pwd.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    ToastUtils.showToast(getString(R.string.string_wifi_toast_1));
                    return;
                }
                if (TextUtils.isEmpty(pwdText) || pwdText.length() < 8) {
                    ToastUtils.showToast(getString(R.string.string_wifi_toast_2));
                    return;
                }
                Send(inputText, pwdText, mAuthenticationType.getSelectedItem().toString());
                ToastUtils.showToast( getString(R.string.string_wifi_toast_3));
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

    private void Send(final String wifiName, final String pwd, final String authenticationType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = null;
                    socket = new Socket("192.168.1.108", 1234);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(wifiName + "+" + pwd + "#" + authenticationType);
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
                String authenticationType = data.getExtras().getString("authenticationType", "WPA/WPA2");
                switch (authenticationType) {
                    case "WPA":
                        mAuthenticationType.setSelection(0);
                        break;
                    case "WPA2":
                        mAuthenticationType.setSelection(1);
                        break;
                    case "WPA/WPA2":
                        mAuthenticationType.setSelection(2);
                        break;
                    case "WEP":
                        mAuthenticationType.setSelection(3);
                        break;
                    default:
                        mAuthenticationType.setSelection(0);
                        break;
                }
                input.setText(wifiName);
            }
        }
    }
}
