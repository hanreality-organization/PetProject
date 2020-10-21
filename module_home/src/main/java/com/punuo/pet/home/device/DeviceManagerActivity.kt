package com.punuo.pet.home.device

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.punuo.pet.home.R
import com.punuo.pet.home.device.adapter.DeviceTabAdapter
import com.punuo.pet.router.HomeRouter
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity
import com.punuo.sys.sdk.view.PagerSlidingTabStrip

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
@Route(path = HomeRouter.ROUTER_DEVICE_MANAGER_ACTIVITY)
class DeviceManagerActivity : BaseSwipeBackActivity() {

    private lateinit var title: TextView
    private lateinit var backIcon: ImageView
    private lateinit var subTitle: TextView
    private lateinit var tabStrip: PagerSlidingTabStrip
    private lateinit var viewPager: ViewPager

    private var mDeviceTabAdapter: DeviceTabAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_device_manager_actvity)

        initView()
    }

    private fun initView() {
        title = findViewById(R.id.title) as TextView
        backIcon = findViewById(R.id.back) as ImageView
        subTitle = findViewById(R.id.sub_title) as TextView
        title.text = "设备管理"
        tabStrip = findViewById(R.id.pager_slide_tab) as PagerSlidingTabStrip
        tabStrip.visibility = View.GONE
        viewPager = findViewById(R.id.view_pager) as ViewPager
        mDeviceTabAdapter = DeviceTabAdapter(supportFragmentManager)
        viewPager.adapter = mDeviceTabAdapter
        tabStrip.setViewPager(viewPager)

        backIcon.setOnClickListener {
            onBackPressed()
        }
    }

}