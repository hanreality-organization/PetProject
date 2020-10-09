package com.punuo.pet.home.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.punuo.pet.home.R
import com.punuo.sys.sdk.fragment.BaseFragment

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
class DeviceOrderFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(R.layout.device_order_fragment, container, false)
        return mFragmentView
    }
}