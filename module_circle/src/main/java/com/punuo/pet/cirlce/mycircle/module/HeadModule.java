package com.punuo.pet.cirlce.mycircle.module;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.punuo.pet.cirlce.R;
import com.punuo.pet.cirlce.R2;
import com.punuo.pet.cirlce.mycircle.adapter.ThemeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
public class HeadModule {
    @BindView(R2.id.theme_list)
    RecyclerView mThemeList;
    private View mRootView;
    private ThemeAdapter mThemeAdapter;

    public HeadModule(Context context, ViewGroup parent) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.module_my_circle_head_layout, parent, false);
        ButterKnife.bind(this, mRootView);
        mThemeList.setLayoutManager(new GridLayoutManager(context, 2));
        mThemeAdapter = new ThemeAdapter(context);
        mThemeList.setAdapter(mThemeAdapter);
    }

    public View getRootView() {
        return mRootView;
    }

    public void updateView() {
        mThemeAdapter.appendData();
    }
}
