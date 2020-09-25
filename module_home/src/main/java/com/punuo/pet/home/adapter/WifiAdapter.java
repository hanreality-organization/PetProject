package com.punuo.pet.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hacknife.wifimanager.IWifi;
import com.punuo.pet.home.R;
import com.punuo.pet.home.holder.WifiHolder;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2020/8/21.
 **/
public class WifiAdapter extends BaseRecyclerViewAdapter<IWifi> {
    private View.OnClickListener mOnClickListener;
    public WifiAdapter(Context context, List<IWifi> data, View .OnClickListener listener) {
        super(context, data);
        mOnClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new WifiHolder(LayoutInflater.from(mContext).inflate(R.layout.home_item_wifi, parent, false));
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof WifiHolder) {
            ((WifiHolder) baseViewHolder).bind(mData.get(position), position);
            baseViewHolder.itemView.setTag(mData.get(position));
            baseViewHolder.itemView.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    @Override
    public int getBasicItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }
}
