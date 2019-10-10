package com.punuo.pet.cirlce.mycircle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.punuo.pet.cirlce.mycircle.model.ThemeItem;
import com.punuo.pet.cirlce.mycircle.viewholder.ThemeVH;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
public class ThemeAdapter extends RecyclerView.Adapter<ThemeVH> {//话题的适配器
    private Context mContext;
    private List<ThemeItem> mThemeItems;

    public ThemeAdapter(Context context) {
        mContext = context;
        mThemeItems = new ArrayList<>();
    }

    public void appendData() {
        mThemeItems.clear();
        for (int i = 0; i < 4; i++) {
            ThemeItem themeItem = new ThemeItem();
            mThemeItems.add(themeItem);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ThemeVH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ThemeVH(mContext, viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeVH themeVH, int i) {

    }

    @Override
    public int getItemCount() {
        return mThemeItems == null ? 0 : mThemeItems.size();
    }
}
