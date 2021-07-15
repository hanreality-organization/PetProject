package com.punuo.pet.home.device.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.punuo.pet.home.R
import com.punuo.pet.home.device.BindDeviceFragment
import com.punuo.pet.home.device.DeviceOrderFragment
import com.punuo.pet.home.device.DeviceSelfApplyFragment

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
class DeviceTabAdapter(context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val tabs = ArrayList<String>()

    init {
        tabs.add(context.getString(R.string.string_device_bind))
        tabs.add(context.getString(R.string.string_device_permissions))
        tabs.add(context.getString(R.string.string_my_apply))
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> {
                DeviceOrderFragment()
            }
            2 -> {
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