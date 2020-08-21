package com.punuo.pet.home.wifi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.hacknife.wifimanager.IWifi;
import com.hacknife.wifimanager.IWifiManager;
import com.hacknife.wifimanager.OnWifiChangeListener;
import com.hacknife.wifimanager.OnWifiConnectListener;
import com.hacknife.wifimanager.OnWifiStateChangeListener;
import com.hacknife.wifimanager.State;
import com.hacknife.wifimanager.WifiManager;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.home.adapter.WifiAdapter;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.view.AdvancedTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2020/8/21.
 **/
@Route(path = HomeRouter.ROUTER_WIFI_CHOOSE_ACTIVITY)
public class WifiChooseActivity extends BaseSwipeBackActivity implements OnWifiChangeListener, OnWifiConnectListener, OnWifiStateChangeListener {

    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.recycler)
    RecyclerView mRecycler;
    @BindView(R2.id.scan_wifi)
    AdvancedTextView mScanWifi;
    @BindView(R2.id.title)
    TextView mTitle;

    private IWifiManager manager;
    private WifiAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_wif_choose_activity);
        ButterKnife.bind(this);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
        mTitle.setText("选择WIFI");
        manager = WifiManager.create(this);
        mRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        manager.setOnWifiChangeListener(this);
        manager.setOnWifiConnectListener(this);
        manager.setOnWifiStateChangeListener(this);
        mAdapter = new WifiAdapter(this, new ArrayList<IWifi>(), mClickListener);
        mRecycler.setAdapter(mAdapter);
        manager.openWifi();
        mScanWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.scanWifi();
                showLoadingDialog("正在获取wif");
            }
        });
        showLoadingDialog("正在获取wifi");
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IWifi wifi = (IWifi) v.getTag();
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("wifiName", wifi.name());
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            scrollToFinishActivity();
        }
    };

    @Override
    public void onWifiChanged(List<IWifi> wifis) {
        mAdapter.clear();
        mAdapter.addAll(wifis);
        dismissLoadingDialog();
    }

    @Override
    public void onConnectChanged(boolean status) {

    }

    @Override
    public void onStateChanged(State state) {

    }

    @Override
    protected void onDestroy() {
        manager.destroy();
        super.onDestroy();
    }
}
