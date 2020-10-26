package com.punuo.pet.cirlce.mycircle.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.punuo.pet.cirlce.mycircle.model.CircleItem;
import com.punuo.pet.cirlce.mycircle.viewholder.CircleItemVH;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
public class MyCircleAdapter extends BaseRecyclerViewAdapter<CircleItem> {
    public MyCircleAdapter(Context context, List<CircleItem> data) {
        super(context, data);
    }

    public void appendData() {
        mData.clear();
        for (int i = 0; i < 10; i++) {
            CircleItem circleItem = new CircleItem();
            mData.add(circleItem);
        }
        notifyDataSetChanged();//适配器内容改变时，刷新每个item中的内容
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new CircleItemVH(mContext, parent);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof BaseViewHolder) {
            ((BaseViewHolder) baseViewHolder).bind(getItem(position), position);
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
