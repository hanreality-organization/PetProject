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
 * 我的申请
 **/
class DeviceSelfApplyFragment : BaseFragment() {

    private lateinit var pulltoRecyclerView: PullToRefreshRecyclerView
    private lateinit var recyclerView: RecyclerView
    private var mHistoryAdapter: BindHistoryAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(R.layout.device_self_apply_fragment, container, false)
        initView()
        EventBus.getDefault().register(this)
        return mFragmentView
    }

    private fun initView() {
        pulltoRecyclerView = mFragmentView.findViewById(R.id.apply_list)
        recyclerView = pulltoRecyclerView.refreshableView

        pulltoRecyclerView.setOnRefreshListener {
            refresh()
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        mHistoryAdapter = BindHistoryAdapter(context, ArrayList())
        refresh()
    }

    private fun refresh() {
        getMyApplyCheck()
    }
    private fun getMyApplyCheck() {
        val request = GetBindingHistoryRequest()
        request.addUrlParam("username", AccountManager.getUserName())
        request.requestListener = object : RequestListener<BindHistoryModel> {
            override fun onComplete() {
                pulltoRecyclerView.onRefreshComplete()
            }

            override fun onSuccess(result: BindHistoryModel?) {
                result?.data?.let {
                    recyclerView.adapter = mHistoryAdapter
                    if (it.items.isNullOrEmpty()) {
                        val list = ArrayList<BaseDevice>()
                        list.add(EmptyData(getString(R.string.string_empty_apply)))
                        mHistoryAdapter?.appendData(list)
                    } else {
                        mHistoryAdapter?.appendData(it.items?.reversed())
                    }
                }
            }

            override fun onError(e: Exception?) {
                HandlerExceptionUtils.handleException(e)
            }
        }
        HttpManager.addRequest(request)
    }

    //提交申请
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AddBindCheckEvent) {
        getMyApplyCheck()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }
}