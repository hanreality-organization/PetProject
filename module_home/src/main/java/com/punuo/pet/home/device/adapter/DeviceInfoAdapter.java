package com.punuo.pet.home.device.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.punuo.pet.home.device.holder.DeviceInfoVH;
import com.punuo.pet.home.device.model.DeviceInfo;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class DeviceInfoAdapter extends BaseRecyclerViewAdapter<DeviceInfo> {

    public DeviceInfoAdapter(Context context, List<DeviceInfo> data) {
        super(context, data);
    }

    public void appendData(List<DeviceInfo> devices) {
        mData.clear();
        if (devices != null) {
            mData.addAll(devices);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new DeviceInfoVH(mContext, parent);
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
