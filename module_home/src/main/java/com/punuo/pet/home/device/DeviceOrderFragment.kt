package com.punuo.pet.home.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView
import com.punuo.pet.home.R
import com.punuo.pet.home.device.adapter.BindHistoryAdapter
import com.punuo.pet.home.device.adapter.DeviceBindCheckAdapter
import com.punuo.pet.home.device.event.AddBindCheckEvent
import com.punuo.pet.home.device.event.BindCheckEvent
import com.punuo.pet.home.device.model.BaseDevice
import com.punuo.pet.home.device.model.BindHistoryModel
import com.punuo.pet.home.device.model.DeviceBindModel
import com.punuo.pet.home.device.model.EmptyData
import com.punuo.pet.home.device.request.GetBindingCheckRequest
import com.punuo.pet.home.device.request.GetBindingHistoryRequest
import com.punuo.sip.dev.DevManager
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.fragment.BaseFragment
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.util.HandlerExceptionUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 * 设备权限
 **/
class DeviceOrderFragment : BaseFragment() {

    private lateinit var pulltoRecyclerView: PullToRefreshRecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var myApplyBtn: TextView
    private var adapter: DeviceBindCheckAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(R.layout.device_order_fragment, container, false)
        initView()
        EventBus.getDefault().register(this)
        return mFragmentView
    }

    private fun initView() {
        pulltoRecyclerView = mFragmentView.findViewById(R.id.apply_list)
        recyclerView = pulltoRecyclerView.refreshableView
        myApplyBtn = mFragmentView.findViewById(R.id.my_device_apply)

        pulltoRecyclerView.setOnRefreshListener {
            refresh()
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = DeviceBindCheckAdapter(context, ArrayList())
        refresh()
    }

    private fun refresh() {
        getBindingCheck()
    }

    private fun getBindingCheck() {
        val request = GetBindingCheckRequest()
        request.addUrlParam("username", AccountManager.getUserName())
        request.requestListener = object : RequestListener<DeviceBindModel> {
            override fun onComplete() {
                pulltoRecyclerView.onRefreshComplete()
            }

            override fun onSuccess(result: DeviceBindModel?) {
                result?.data?.let {
                    recyclerView.adapter = adapter
                    if (it.items.isNullOrEmpty()) {
                        val list = ArrayList<BaseDevice>()
                        list.add(EmptyData("暂无可管理的设备"))
                        adapter?.appendData(list)
                    } else {
                        adapter?.appendData(it.items?.reversed())
                    }
                }
            }

            override fun onError(e: Exception?) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        HttpManager.addRequest(request)

    }


    //通过或拒绝
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: BindCheckEvent) {
        getBindingCheck()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }
}