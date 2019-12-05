package com.punuo.pet.feed.plan;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punuo.pet.feed.R;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MyPlanAdapter extends BaseRecyclerViewAdapter<Plan> {

    public MyPlanAdapter(Context context, List<Plan> data) {
        super(context, data);
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planTime;
        TextView planName;
        TextView planCount;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        public PlanViewHolder(View view) {
            super(view);
            planTime = view.findViewById(R.id.plan_time);
            planName = view.findViewById(R.id.plan_name);
            planCount = view.findViewById(R.id.plan_count);
        }

        public void bindData(Plan plan) {
            Date date = new Date(plan.getPlanTime()*1000);
            planTime.setText(mSimpleDateFormat.format(date));
            planName.setText(plan.getPlanName());
            planCount.setText(plan.getPlanCount());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new PlanViewHolder(LayoutInflater.from(mContext).inflate(R.layout.feed_plan_item, parent, false));
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof PlanViewHolder) {
            ((PlanViewHolder) baseViewHolder).bindData(mData.get(position));
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
