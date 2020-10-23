package com.punuo.pet.home.device.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.punuo.pet.home.device.holder.DeviceBindCheckVH;
import com.punuo.pet.home.device.model.DeviceBindOrder;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class DeviceBindCheckAdapter extends BaseRecyclerViewAdapter<DeviceBindOrder> {

    public DeviceBindCheckAdapter(Context context, List<DeviceBindOrder> data) {
        super(context, data);
    }

    public void appendData(List<DeviceBindOrder> devices) {
        mData.clear();
        if (devices != null) {
            mData.addAll(devices);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new DeviceBindCheckVH(mContext, parent);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof BaseViewHolder) {
            ((BaseViewHolder) baseViewHolder).bind(mData.get(position), position);
        }
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    @Override
    public int getBasicItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
