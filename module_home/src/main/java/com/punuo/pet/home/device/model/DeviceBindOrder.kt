package com.punuo.pet.home.device.model

import com.punuo.sys.sdk.model.PNBaseModel

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
data class DeviceBindOrder(var id: String,
                           var time: String,
                           var userName: String,
                           var devid: String,
                           var status: Int) : PNBaseModel()