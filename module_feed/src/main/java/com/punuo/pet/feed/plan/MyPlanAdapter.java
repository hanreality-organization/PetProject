package com.punuo.pet.feed.plan;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.punuo.pet.feed.R;

import java.util.List;


public class MyPlanAdapter extends RecyclerView.Adapter<MyPlanAdapter.ViewHolder> {

    private List<Plan> mPlanList;

    static  class ViewHolder extends RecyclerView.ViewHolder{
        TextView planTime;
        TextView planName;
        TextView planCount;

        public ViewHolder(View view){
            super(view);
            planTime = view.findViewById(R.id.plan_time);
            planName = view.findViewById(R.id.plan_name);
            planCount = view.findViewById(R.id.plan_count);
        }
    }

    public MyPlanAdapter(List<Plan> planList){
        mPlanList = planList;
    }

    @Override
    public int getItemCount() {
        return mPlanList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feed_plan_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
         Plan mPlan = mPlanList.get(position);
         viewHolder.planTime.setText(mPlan.getPlanTime());
         viewHolder.planName.setText(mPlan.getPlanName());
         viewHolder.planCount.setText(mPlan.getPlanCount());
    }
}
