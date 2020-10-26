package com.punuo.pet.home.device.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.punuo.pet.home.device.holder.BindHistoryVH;
import com.punuo.pet.home.device.model.BindHistoryData;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-16.
 **/
public class BindHistoryAdapter extends BaseRecyclerViewAdapter<BindHistoryData> {

    public BindHistoryAdapter(Context context, List<BindHistoryData> data) {
        super(context, data);
    }

    public void appendData(List<BindHistoryData> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new BindHistoryVH(mContext, parent);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof BindHistoryVH) {
            ((BindHistoryVH) baseViewHolder).bind(mData.get(position), position);
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
