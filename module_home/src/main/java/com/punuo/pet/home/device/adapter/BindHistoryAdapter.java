package com.punuo.pet.home.device.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.punuo.pet.home.device.holder.BindHistoryVH;
import com.punuo.pet.home.device.holder.EmptyVH;
import com.punuo.pet.home.device.model.BaseDevice;
import com.punuo.pet.home.device.model.EmptyData;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class BindHistoryAdapter extends BaseRecyclerViewAdapter<BaseDevice> {

    public BindHistoryAdapter(Context context, List<BaseDevice> data) {
        super(context, data);
    }

    public void appendData(List<BaseDevice> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new EmptyVH(mContext, parent);
        } else {
            return new BindHistoryVH(mContext, parent);
        }
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof BindHistoryVH) {
            ((BindHistoryVH) baseViewHolder).bind(mData.get(position), position);
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
