package com.punuo.pet.cirlce.mycircle.viewholder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.punuo.pet.cirlce.R;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
public class ThemeVH extends RecyclerView.ViewHolder {
    public ThemeVH(Context context, ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.theme_item_layout, parent, false));
    }
}
