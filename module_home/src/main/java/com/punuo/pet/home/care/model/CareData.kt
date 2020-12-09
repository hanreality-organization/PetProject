package com.punuo.pet.home.care.model

import com.google.gson.annotations.SerializedName
import com.punuo.sys.sdk.model.PNBaseModel

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
class CareData : PNBaseModel() {

    @SerializedName("picture")
    var icon: String? = null

    @SerializedName("carename")
    var careName: String? = null

    @SerializedName("petname")
    var petName: String? = null

    @SerializedName("date")
    var date: Long = 0

    val theme: String?
        get() {
            return when (careName) {
                "洗澡清洁" -> "ShowerPlan"
                "体检" -> "HealthExam"
                "买宠物粮" -> "PetFood"
                "体内驱虫" -> "InnerInsect"
                "体外驱虫" -> "OuterInsect"
                "疫苗注射" -> "TakeVaccine"
                "美容护理" -> "MakeBeauty"
                "遛宠" -> "WalkPet"
                else -> ""
            }
        }
}