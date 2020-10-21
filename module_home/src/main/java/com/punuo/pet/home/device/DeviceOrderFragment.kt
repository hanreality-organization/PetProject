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
import com.punuo.pet.home.device.model.DeviceBindModel
import com.punuo.pet.home.device.request.GetBindingCheckRequest
import com.punuo.sip.dev.DevManager
import com.punuo.sys.sdk.account.AccountManager
import com.punuo.sys.sdk.fragment.BaseFragment
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import java.lang.Exception

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
class DeviceOrderFragment : BaseFragment() {

    private lateinit var pulltoRecyclerView: PullToRefreshRecyclerView
    private lateinit var recyclerView: RecyclerView
    private lateinit var myApplyBtn: TextView
    private lateinit var emptyView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mFragmentView = inflater.inflate(R.layout.device_order_fragment, container, false)
        initView()
        return mFragmentView
    }

    private fun initView() {
        pulltoRecyclerView = mFragmentView.findViewById(R.id.apply_list)
        recyclerView = pulltoRecyclerView.refreshableView
        myApplyBtn = mFragmentView.findViewById(R.id.my_device_apply)
        emptyView = mFragmentView.findViewById(R.id.text_empty)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        if (DevManager.getInstance().isBindDevice) {
            refresh()
        } else {
            emptyView.visibility = View.VISIBLE
        }
    }

    private fun refresh() {
        val request = GetBindingCheckRequest()
        request.addUrlParam("devid", DevManager.getInstance().devId)
        request.addUrlParam("username", AccountManager.getUserName())
        request.requestListener = object :RequestListener<DeviceBindModel> {
            override fun onComplete() {
            }

            override fun onSuccess(result: DeviceBindModel?) {
            }

            override fun onError(e: Exception?) {

            }
        }
        HttpManager.addRequest(request)

    }
}