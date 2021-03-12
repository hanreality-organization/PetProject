package com.punuo.pet.home.care.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.punuo.pet.home.care.holder.CareDetailViewHolder
import com.punuo.pet.home.care.model.CareData
import com.punuo.pet.home.care.model.CareDetailModel
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
class CareDetailAdapter(var context: Context?, var title: String?) : RecyclerView.Adapter<CareDetailViewHolder>() {

    var dataList: MutableList<CareDetailModel.CareDetailData>? = null
        set(value) {
            field = value
            field?.iterator()?.forEach {
                it.title = title
            }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareDetailViewHolder {
        return CareDetailViewHolder(context, parent)
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: CareDetailViewHolder, position: Int) {
        holder.bindData(dataList?.get(position), position)
    }


}