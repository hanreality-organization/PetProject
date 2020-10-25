package com.punuo.pet.home.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView
import com.punuo.pet.home.R
import com.punuo.pet.home.device.adapter.BindHistoryAdapter
import com.punuo.pet.home.device.adapter.DeviceBindCheckAdapter
import com.punuo.pet.home.device.event.BindCheckEvent
import com.punuo.pet.home.device.model.BindHistoryModel
import com.punuo.pet.home.device.model.DeviceBindModel
import com.punuo.pet.home.device.model.SelfHost
import com.punuo.pet.home.device.request.CheckSelfIsHostRequest
import com.punuo.pet.home.device.request.GetBindingCheckRequest
import com.punuo.pet.home.device.request.GetBindingHistoryRequest
import com.punuo.sip.dev.DevManager
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.fragment.BaseFragment
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
class DeviceOrderFragment : BaseFragment() {

    private lateinit var pulltoRecyclerView: PullToRefreshRecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var myApplyBtn: TextView
    private lateinit var emptyView: TextView
    private var adapter: DeviceBindCheckAdapter? = null
    private var mHistoryAdapter: BindHistoryAdapter? = null

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
        emptyView = mFragmentView.findViewById(R.id.text_empty)

        pulltoRecyclerView.setOnRefreshListener {
            checkHostOfSelf()
        }

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        adapter = DeviceBindCheckAdapter(context, ArrayList())
        mHistoryAdapter = BindHistoryAdapter(context, ArrayList())
        if (DevManager.getInstance().isBindDevice) {
            checkHostOfSelf()
        } else {
            emptyView.visibility = View.VISIBLE
            emptyView.text = getString(R.string.home_empty_device_str)
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
                pulltoRecyclerView.onRefreshComplete()
            }

            override fun onSuccess(result: SelfHost?) {
                result?.data?.let {
                    if (it.host) {
                        getBindingCheck()
                    } else {
                        getMyApplyCheck()
                    }
                }
            }

            override fun onError(e: Exception?) {
            }

        }
        HttpManager.addRequest(checkHostOfSelfRequest)
    }

    private fun getBindingCheck() {
        val request = GetBindingCheckRequest()
        request.addUrlParam("devid", DevManager.getInstance().devId)
        request.addUrlParam("username", AccountManager.getUserName())
        request.requestListener = object : RequestListener<DeviceBindModel> {
            override fun onComplete() {
            }

            override fun onSuccess(result: DeviceBindModel?) {
                result?.data?.let {
                    emptyView.visibility = View.GONE
                    recyclerView.adapter = adapter
                    adapter?.appendData(it.items)
                }
                result?.let {
                    if (!it.success) {
                        emptyView.visibility = View.VISIBLE
                        emptyView.text = it.message
                    }
                }
            }

            override fun onError(e: Exception?) {

            }
        }
        HttpManager.addRequest(request)

    }

    private fun getMyApplyCheck() {
        val request = GetBindingHistoryRequest()
        request.addUrlParam("username", AccountManager.getUserName())
        request.requestListener = object : RequestListener<BindHistoryModel> {
            override fun onComplete() {
            }

            override fun onSuccess(result: BindHistoryModel?) {
                result?.data?.let {
                    emptyView.visibility = View.GONE
                    recyclerView.adapter = mHistoryAdapter
                    mHistoryAdapter?.appendData(it.items)
                }
                result?.let {
                    if (!it.success) {
                        emptyView.visibility = View.VISIBLE
                        emptyView.text = it.message
                    }
                }
            }

            override fun onError(e: Exception?) {

            }
        }
        HttpManager.addRequest(request)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: BindCheckEvent) {
        getBindingCheck()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }
}