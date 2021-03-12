package com.punuo.pet.home.care.model

import android.os.Parcel
import android.os.Parcelable
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
                              var title: String?) : PNBaseModel() , Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(petid)
            parcel.writeString(id)
            parcel.writeString(username)
            parcel.writeString(time)
            parcel.writeString(detail)
            parcel.writeString(title)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<CareDetailData> {
            override fun createFromParcel(parcel: Parcel): CareDetailData {
                return CareDetailData(parcel)
            }

            override fun newArray(size: Int): Array<CareDetailData?> {
                return arrayOfNulls(size)
            }
        }

    }

}