package com.punuo.pet.home.device

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.punuo.pet.home.R
import com.punuo.pet.home.device.adapter.DeviceTabAdapter
import com.punuo.pet.home.device.model.SelfHost
import com.punuo.pet.home.device.request.CheckSelfIsHostRequest
import com.punuo.pet.router.HomeRouter
import com.punuo.sip.dev.DevManager
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.view.PagerSlidingTabStrip
import java.lang.Exception

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
        viewPager = findViewById(R.id.view_pager) as ViewPager

        checkHostOfSelf()

        backIcon.setOnClickListener {
            onBackPressed()
        }
    }

    private var checkHostOfSelfRequest: CheckSelfIsHostRequest? = null
    private fun checkHostOfSelf() {
        checkHostOfSelfRequest?.takeIf {
            !it.isFinish
        }?.apply {
            this.finish()
        }
        checkHostOfSelfRequest = CheckSelfIsHostRequest()
        checkHostOfSelfRequest?.addUrlParam("userName", AccountManager.getUserName())
        checkHostOfSelfRequest?.addUrlParam("devId", DevManager.getInstance().devId)
        checkHostOfSelfRequest?.requestListener = object : RequestListener<SelfHost?> {
            override fun onComplete() {

            }

            override fun onSuccess(result: SelfHost?) {
                result?.data?.let {
                    mDeviceTabAdapter = DeviceTabAdapter(supportFragmentManager, it.host)
                    viewPager.adapter = mDeviceTabAdapter
                    tabStrip.setViewPager(viewPager)
                }
            }

            override fun onError(e: Exception?) {
            }

        }
        HttpManager.addRequest(checkHostOfSelfRequest)
    }

}