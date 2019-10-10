package com.punuo.pet.home.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.Method;

/**
 * Created by han.chen.
 * Date on 2019-08-14.
 **/
public class BluetoothUtil {

    public static boolean removeBond(BluetoothDevice bluetoothDevice) {
        boolean success = false;
        try {
            Method method = BluetoothDevice.class.getMethod("removeBond");
            success = (boolean) method.invoke(bluetoothDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public static void createBond(BluetoothDevice bluetoothDevice) {
        try {
            Method method = BluetoothDevice.class.getMethod("createBond");
            method.invoke(bluetoothDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
