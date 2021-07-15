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
                "洗澡清洁" ,"Cleaning" -> "ShowerPlan"
                "体检" ,"Health Examination"-> "HealthExam"
                "买宠物粮", "Pet Food" -> "PetFood"
                "体内驱虫", "Insect Repellent" -> "InnerInsect"
                "体外驱虫", "Expelling parasite" -> "OuterInsect"
                "疫苗注射", "Vaccination" -> "TakeVaccine"
                "美容护理", "Aesthetic Nursing" -> "MakeBeauty"
                "遛宠", "Walking" -> "WalkPet"
                else -> ""
            }
        }
}