package com.punuo.pet.home.device.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.punuo.pet.home.device.BindDeviceFragment
import com.punuo.pet.home.device.DeviceOrderFragment
import com.punuo.pet.home.device.DeviceSelfApplyFragment

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
class DeviceTabAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val tabs = ArrayList<String>()

    init {
        tabs.add("设备绑定")
        tabs.add("设备权限")
        tabs.add("我的申请")
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            1 -> {
                DeviceOrderFragment()
            }
            2-> {
                DeviceSelfApplyFragment()
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