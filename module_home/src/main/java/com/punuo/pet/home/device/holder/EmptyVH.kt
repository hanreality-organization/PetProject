package com.punuo.pet.home.device.holder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.punuo.pet.home.R
import com.punuo.pet.home.device.model.BaseDevice
import com.punuo.pet.home.device.model.EmptyData
import com.punuo.sys.sdk.recyclerview.BaseViewHolder

/**
 * Created by han.chen.
 * Date on 2020/10/27.
 **/
class EmptyVH(context: Context, parent: ViewGroup) : BaseViewHolder<BaseDevice>(
        LayoutInflater.from(context).inflate(R.layout.home_recycle_empty_item, parent, false)
) {
    private val emptyText = itemView.findViewById<TextView>(R.id.empty_text)
    override fun bindData(t: BaseDevice?, position: Int) {
        (t as? EmptyData)?.let {
            emptyText.text = it.emptyText
        }
    }
}