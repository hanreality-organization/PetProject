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
import com.punuo.pet.home.device.model.BindHistoryData
import com.punuo.pet.home.device.model.DeviceBindOrder
import com.punuo.pet.home.device.request.SetCheckBindingRequest
import com.punuo.sys.sdk.activity.BaseActivity
import com.punuo.sys.sdk.httplib.HttpManager
import com.punuo.sys.sdk.httplib.RequestListener
import com.punuo.sys.sdk.model.BaseModel
import com.punuo.sys.sdk.recyclerview.BaseViewHolder
import com.punuo.sys.sdk.util.HandlerExceptionUtils
import com.punuo.sys.sdk.util.ToastUtils
import com.tencent.bugly.proguard.t
import org.greenrobot.eventbus.EventBus
import java.lang.Exception

/**
 * Created by han.chen.
 * Date on 2020/10/22.
 **/
class BindHistoryVH(context:Context, parent: ViewGroup) : BaseViewHolder<BaseDevice>(
        LayoutInflater.from(context).inflate(R.layout.home_recycle_bind_history_item, parent, false)
) {

    private val userName = itemView.findViewById<TextView>(R.id.user_name)
    private val devId = itemView.findViewById<TextView>(R.id.dev_id)
    private val applyTime = itemView.findViewById<TextView>(R.id.apply_time)
    private val applyStatus = itemView.findViewById<TextView>(R.id.device_status)

    override fun bindData(t: BaseDevice?, position: Int) {
        (t as? BindHistoryData)?.let { order->
            userName.text = "申请用户: ${order.username}"
            devId.text = "申请设备: ${order.devid}"
            applyTime.text = "申请时间: ${order.time}"
            when (order.status) {
                0 -> {
                    applyStatus.text = "审核中"
                }
                1 -> {
                    applyStatus.text = "已通过"
                }
                2 -> {
                    applyStatus.text = "已拒绝"

                }
            }
        }
    }

}