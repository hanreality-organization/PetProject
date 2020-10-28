package com.punuo.pet.home.device.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.punuo.pet.home.R
import com.punuo.pet.home.device.event.BindCheckEvent
import com.punuo.pet.home.device.model.BaseDevice
import com.punuo.pet.home.device.model.DeviceBindOrder
import com.punuo.pet.home.device.request.SetCheckBindingRequest
import com.punuo.sys.sdk.activity.BaseActivity
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.recyclerview.BaseViewHolder
import com.punuo.sys.sdk.util.HandlerExceptionUtils
import com.punuo.sys.sdk.util.ToastUtils
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

/**
 * Created by han.chen.
 * Date on 2020/10/22.
 **/
class DeviceBindCheckVH(context: Context, parent: ViewGroup) : BaseViewHolder<BaseDevice>(
        LayoutInflater.from(context).inflate(R.layout.home_recycle_dev_bind_check_item, parent, false)
) {

    private val userName = itemView.findViewById<TextView>(R.id.user_name)
    private val devId = itemView.findViewById<TextView>(R.id.dev_id)
    private val applyTime = itemView.findViewById<TextView>(R.id.apply_time)
    private val applyStatus = itemView.findViewById<TextView>(R.id.device_status)
    private val applyOk = itemView.findViewById<TextView>(R.id.device_ok)
    private val applyCancel = itemView.findViewById<TextView>(R.id.device_cancel)

    override fun bindData(t: BaseDevice?, position: Int) {
        (t as? DeviceBindOrder)?.let { order ->
            userName.text = "申请用户: ${order.username}"
            devId.text = "申请设备: ${order.devid}"
            applyTime.text = "申请时间: ${order.time}"
            when (order.status) {
                0 -> {
                    applyStatus.visibility = View.GONE
                    applyOk.visibility = View.VISIBLE
                    applyCancel.visibility = View.VISIBLE
                }
                1 -> {
                    applyStatus.visibility = View.VISIBLE
                    applyStatus.text = "已通过"
                    applyOk.visibility = View.GONE
                    applyCancel.visibility = View.GONE
                }
                2 -> {
                    applyStatus.visibility = View.VISIBLE
                    applyStatus.text = "已拒绝"
                    applyOk.visibility = View.GONE
                    applyCancel.visibility = View.GONE
                }
            }
            applyOk.setOnClickListener {
                //通过
                handleApply(order, 1)
            }
            applyCancel.setOnClickListener {
                //拒绝
                handleApply(order, 2)
            }
        }
    }

    private fun handleApply(t: DeviceBindOrder, status: Int) {
        (itemView.context as? BaseActivity)?.showLoadingDialog()
        val request = SetCheckBindingRequest()
        request.addUrlParam("id", t.id)
        request.addUrlParam("username", t.username)
        request.addUrlParam("devid", t.devid)
        request.addUrlParam("status", status)
        request.requestListener = object : RequestListener<BaseModel> {
            override fun onComplete() {
                (itemView.context as? BaseActivity)?.dismissLoadingDialog()
            }

            override fun onSuccess(result: BaseModel?) {
                result?.let {
                    if (it.success) {
                        EventBus.getDefault().post(BindCheckEvent())
                    }
                    ToastUtils.showToast(it.message)
                }
            }

            override fun onError(e: Exception?) {
                HandlerExceptionUtils.handleException(e)
            }

        }
        HttpManager.addRequest(request)
    }
}