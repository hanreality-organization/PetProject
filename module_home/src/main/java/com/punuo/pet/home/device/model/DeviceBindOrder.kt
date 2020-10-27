package com.punuo.pet.home.device.model

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
data class DeviceBindOrder(var id: String,
                           var time: String,
                           var username: String,
                           var devid: String,
                           var status: Int) : BaseDevice()