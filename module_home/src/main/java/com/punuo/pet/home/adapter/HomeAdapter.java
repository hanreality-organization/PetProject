package com.punuo.pet.home.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-15.
 **/
public class HomeAdapter extends BaseRecyclerViewAdapter<Object> {

    public HomeAdapter(Context context, List<Object> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {

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
