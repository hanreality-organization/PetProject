package com.punuo.pet.home.device.model

import com.punuo.sys.sdk.model.PNBaseModel

/**
 * Created by han.chen.
 * Date on 2020/10/9.
 **/
data class BindHistoryData(var id: String,
                           var time: String,
                           var username: String,
                           var devid: String,
                           var status: Int) : BaseDevice()