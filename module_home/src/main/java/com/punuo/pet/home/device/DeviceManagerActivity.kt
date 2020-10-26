package com.punuo.pet.home.device

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.punuo.pet.home.R
import com.punuo.pet.home.device.adapter.DeviceTabAdapter
import com.punuo.pet.router.HomeRouter
import com.punuo.sip.dev.DevManager
import com.punuo.sip.dev.SelfHost
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity
import com.punuo.sys.sdk.view.PagerSlidingTabStrip
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
        EventBus.getDefault().register(this)

        initView()
    }

    private fun initView() {
        title = findViewById(R.id.title) as TextView
        backIcon = findViewById(R.id.back) as ImageView
        subTitle = findViewById(R.id.sub_title) as TextView
        title.text = "设备管理"
        tabStrip = findViewById(R.id.pager_slide_tab) as PagerSlidingTabStrip
        viewPager = findViewById(R.id.view_pager) as ViewPager
        DevManager.getInstance().refreshDevRelationShip()
        backIcon.setOnClickListener {
            onBackPressed()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event : SelfHost) {
        mDeviceTabAdapter = DeviceTabAdapter(supportFragmentManager, DevManager.getInstance().isHost)
        viewPager.adapter = mDeviceTabAdapter
        tabStrip.setViewPager(viewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}