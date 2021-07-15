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
class BindHistoryVH(var context:Context, parent: ViewGroup) : BaseViewHolder<BaseDevice>(
        LayoutInflater.from(context).inflate(R.layout.home_recycle_bind_history_item, parent, false)
) {

    private val userName = itemView.findViewById<TextView>(R.id.user_name)
    private val devId = itemView.findViewById<TextView>(R.id.dev_id)
    private val applyTime = itemView.findViewById<TextView>(R.id.apply_time)
    private val applyStatus = itemView.findViewById<TextView>(R.id.device_status)

    override fun bindData(t: BaseDevice?, position: Int) {
        (t as? BindHistoryData)?.let { order->
            userName.text = context.getString(R.string.string_apply_user, order.username)
            devId.text = context.getString(R.string.string_apply_device, order.devid)
            applyTime.text = context.getString(R.string.string_apply_time, order.time)
            when (order.status) {
                0 -> {
                    applyStatus.text = context.getString(R.string.string_checking)
                }
                1 -> {
                    applyStatus.text = context.getString(R.string.string_passed)
                }
                2 -> {
                    applyStatus.text = context.getString(R.string.string_refuse)

                }
            }
        }
    }

}