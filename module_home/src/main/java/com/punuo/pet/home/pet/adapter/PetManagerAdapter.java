package com.punuo.pet.home.pet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.punuo.pet.home.R;
import com.punuo.pet.home.pet.holder.PetManagerHolder;
import com.punuo.pet.model.PetData;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2020/8/21.
 **/
public class PetManagerAdapter extends BaseRecyclerViewAdapter<PetData> {
    public PetManagerAdapter(Context context, List<PetData> data) {
        super(context, data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new PetManagerHolder(LayoutInflater.from(mContext).inflate(R.layout.pet_manager_item, parent, false));
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, int position) {
        if (baseViewHolder instanceof PetManagerHolder) {
            ((PetManagerHolder) baseViewHolder).bind(mData.get(position), position);
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
