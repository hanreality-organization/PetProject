package com.punuo.pet.home.wifi;

import android.net.wifi.WifiInfo;

import com.punuo.pet.home.wifi.model.WifiMessage;

/**
 * Created by han.chen.
 * Date on 2019-08-14.
 **/
public interface OnServerWifiListener {
    void OnServerConnected(WifiInfo wifiInfo, WifiMessage wifiMessage);

    void OnServerConnecting();

    void OnServerDisConnect();
}
