package com.punuo.pet.home.device.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.punuo.pet.home.device.BindDeviceFragment
import com.punuo.pet.home.device.DeviceOrderFragment

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
class DeviceTabAdapter(fragmentManager: FragmentManager, val isHost: Boolean): FragmentPagerAdapter(fragmentManager) {

    private val tabs = ArrayList<String>()

    init {
        tabs.add("设备绑定")
        if (isHost) {
            tabs.add("设备权限")
        } else {
            tabs.add("我的申请")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }

    override fun getItem(position: Int): Fragment {
        return when {
            "我的申请" == tabs[position] -> {
                DeviceOrderFragment()
            }
            "设备权限" == tabs[position] -> {
                DeviceOrderFragment()
            }
            else -> {
                BindDeviceFragment()
            }
        }
    }

    override fun getCount(): Int {
        return tabs.size
    }
}