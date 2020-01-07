package com.punuo.pet.feed.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.punuo.pet.feed.R;
import com.punuo.pet.feed.plan.Plan;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class FeedViewAdapter extends BaseRecyclerViewAdapter<Plan> {

    private  OnItemLongClickListener onItemLongClickListener;

    public FeedViewAdapter(Context context, List<Plan> data) {
        super(context, data);
    }

    static class PlanViewHolder extends RecyclerView.ViewHolder {
        TextView planTime;
        TextView planName;
        TextView planCount;
        RadioButton check_box;
        LinearLayout root_view;
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        public PlanViewHolder(View view) {
            super(view);
            planTime = view.findViewById(R.id.plan_time);
            planName = view.findViewById(R.id.plan_name);
            planCount = view.findViewById(R.id.plan_count);
            check_box = view.findViewById(R.id.checkbox);
            root_view = view.findViewById(R.id.root_view);

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.feed_plan_item,parent,false);
        final PlanViewHolder viewHolder = new PlanViewHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int layoutPosition = viewHolder.getLayoutPosition();
                if (onItemLongClickListener !=null){
                    onItemLongClickListener.OnItemLongClick(layoutPosition);
                }
                return true;
            }
        });
        return viewHolder;
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

    //删除Plan
    public void removePlan(int position){
        mData.remove(position);
        notifyDataSetChanged();
    }

    //获取到对应位置的计划名称
    public String getPlanName(int position){
        String delPlanName = mData.get(position).getPlanName();
        return delPlanName;
    }

    //定义长按接口
    public interface OnItemLongClickListener{
        void OnItemLongClick(int position);
    }

    //定义长按接口的实现
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
