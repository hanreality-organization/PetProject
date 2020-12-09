package com.punuo.pet.home.care.model

import com.punuo.sys.sdk.model.PNBaseModel

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
data class CareDetailModel(var success: Boolean, var message: String?, var info: MutableList<CareDetailData>?) : PNBaseModel() {

    data class CareDetailData(var petid: String?,
                              var id: String?,
                              var username: String?,
                              var time: String?,
                              var detail: String?,
                              var title: String?) : PNBaseModel()

}