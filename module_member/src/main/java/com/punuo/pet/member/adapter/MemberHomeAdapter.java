package com.punuo.pet.member.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2020-01-07.
 **/
public class MemberHomeAdapter extends BaseRecyclerViewAdapter<String> {

    public MemberHomeAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(mContext));
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
