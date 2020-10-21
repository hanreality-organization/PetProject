package com.punuo.pet.home.device.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.punuo.pet.home.device.BindDeviceFragment
import com.punuo.pet.home.device.DeviceOrderFragment

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
class DeviceTabAdapter(fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager) {

    private val tabs = ArrayList<String>()

    init {
        tabs.add("设备绑定")
//        tabs.add("设备权限")
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            BindDeviceFragment()
        } else {
            DeviceOrderFragment()
        }
    }

    override fun getCount(): Int {
        return tabs.size
    }
}