package com.punuo.pet.home.device.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.punuo.pet.home.device.holder.DeviceBindCheckVH;
import com.punuo.pet.home.device.holder.EmptyVH;
import com.punuo.pet.home.device.model.BaseDevice;
import com.punuo.pet.home.device.model.EmptyData;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class DeviceBindCheckAdapter extends BaseRecyclerViewAdapter<BaseDevice> {

    public DeviceBindCheckAdapter(Context context, List<BaseDevice> data) {
        super(context, data);
    }

    public void appendData(List<BaseDevice> devices) {
        mData.clear();
        if (devices != null) {
            mData.addAll(devices);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new EmptyVH(mContext, parent);
        } else {
            return new DeviceBindCheckVH(mContext, parent);
        }
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof DeviceBindCheckVH) {
            ((DeviceBindCheckVH) baseViewHolder).bind(mData.get(position), position);
        } else if (baseViewHolder instanceof EmptyVH) {
            ((EmptyVH) baseViewHolder).bind(mData.get(position), position);
        }
    }

    @Override
    public int getBasicItemType(int position) {
        BaseDevice item = getItem(position);
        if (item instanceof EmptyData) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getBasicItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
