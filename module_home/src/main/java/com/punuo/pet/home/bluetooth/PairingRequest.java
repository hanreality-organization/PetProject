package com.punuo.pet.home.bluetooth;

/**
 * Created by 75716 on 2019/10/15.
 */
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Method;

public class PairingRequest extends BroadcastReceiver {
    String strPsw ;


    static BluetoothDevice remoteDevice = null;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        int pairingkey=intent.getIntExtra(BluetoothDevice.EXTRA_PAIRING_KEY,BluetoothDevice.ERROR);
        strPsw=String.valueOf(pairingkey);
        if (intent.getAction().equals(
                "android.bluetooth.device.action.PAIRING_REQUEST"))
        {
            BluetoothDevice btDevice = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            // byte[] pinBytes = BluetoothDevice.convertPinToBytes("1234");
            // device.setPin(pinBytes);
            if (btDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
//                try {
//                    Method creMethod =BluetoothDevice.class.getMethod("createBond");
//                    creMethod.invoke(btDevice);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }

                try {
                    Method creMethod =BluetoothDevice.class.getMethod("createBond");
                    creMethod.invoke(btDevice);
                    ClsUtils.setPin(btDevice.getClass(), btDevice, strPsw); // 手机和蓝牙采集器配对
                    ClsUtils.createBond(btDevice.getClass(), btDevice);
                    ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                pair(btDevice.getAddress(),strPsw);
            }
        }


    }
    public  boolean pair(String strAddr, String strPsw)
    {
        boolean result = false;
		BluetoothAdapter btAdapter = BluetoothAdapter
			.getDefaultAdapter();

        btAdapter.cancelDiscovery();

        if (!btAdapter.isEnabled())
        {
            btAdapter.enable();
        }

        if (!BluetoothAdapter.checkBluetoothAddress(strAddr))
        { // 检查蓝牙地址是否有效

            Log.d("mylog", "地址无效===========");
        }

        BluetoothDevice device = btAdapter.getRemoteDevice(strAddr);

        if (device.getBondState() != BluetoothDevice.BOND_BONDED)
        {
            try
            {
                Log.d("mylog", "设备没有配对==========");
                ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对

                ClsUtils.createBond(device.getClass(), device);
//				ClsUtils.cancelPairingUserInput(device.getClass(), device);
                remoteDevice = device; // 配对完毕就把这个设备对象传给全局的remoteDevice
                result = true;
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block

                Log.d("mylog", "配对失败===="+e);
                e.printStackTrace();
            } //

        }
        else
        {
            Log.d("mylog", "设备已经配对");
            try
            {
                ClsUtils.createBond(device.getClass(), device);
                ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                ClsUtils.createBond(device.getClass(), device);
                remoteDevice = device; // 如果绑定成功，就直接把这个设备对象传给全局的remoteDevice
                result = true;
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                Log.d("mylog", "setPiN failed!");
                e.printStackTrace();
            }
        }
        return result;
    }
}
