package com.punuo.pet.home.care.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.punuo.pet.home.R
import com.punuo.pet.home.care.CareActivity
import com.punuo.pet.home.care.CareDetailActivity
import com.punuo.pet.home.care.model.CareDetailModel

/**
 * Created by han.chen.
 * Date on 2020/12/8.
 **/
class CareDetailViewHolder(var context: Context?, parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.care_item_layout, parent, false)) {

    private val label = itemView.findViewById<TextView>(R.id.item_label)
    private val desc = itemView.findViewById<TextView>(R.id.item_desc)
    private val time = itemView.findViewById<TextView>(R.id.item_time)
    private val delete = itemView.findViewById<TextView>(R.id.item_delete)

    fun bindData(item: CareDetailModel.CareDetailData?, position: Int) {
        item?.let {
            label.text = it.title
            desc.text = it.detail
            time.text = it.time
            delete.setOnClickListener {
                (context as? CareDetailActivity)?.deleteHistory(item, position)
            }
        }
    }
}