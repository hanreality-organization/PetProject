package com.punuo.pet.home.bluetooth.model;

import android.bluetooth.BluetoothDevice;

/**
 * Created by han.chen.
 * Date on 2019-08-13.
 **/
public class BluetoothInfo extends BaseInfo {
    public BluetoothDevice bluetoothDevice;
    /**
     * 已配对，是否连接
     */
    public boolean isConnect;
}
