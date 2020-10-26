package com.punuo.pet.home.device.model

import com.punuo.sys.sdk.model.PNBaseModel

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
data class DeviceBindModel(var success: Boolean,
                           var message: String?,
                           var data: DeviceBindData?) : PNBaseModel() {
    data class DeviceBindData(var items: MutableList<DeviceBindOrder>?) : PNBaseModel()
}