package com.punuo.pet.home.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.punuo.pet.home.R;
import com.punuo.pet.model.PetData;
import com.punuo.pet.model.PetModel;
import com.punuo.sys.sdk.view.loading.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by han.chen.
 * Date on 2019-07-30.
 **/
public class PetLoopHolder {

    private ViewPager mViewPager;
    private ViewAdapter mViewAdapter;
    private View mRootView;
    private TextView mEmptyText;
    private LoadingView mLoadingView;

    public static PetLoopHolder newInstance(Context context, ViewGroup viewGroup) {
        return new PetLoopHolder(context, LayoutInflater.from(context).inflate(R.layout.home_recycle_item_loopmodule, viewGroup, false));
    }

    public View getRootView() {
        return mRootView;
    }

    private PetLoopHolder(Context context, View itemView) {
        mRootView = itemView;
        mViewPager = itemView.findViewById(R.id.view_pager);
        mEmptyText = itemView.findViewById(R.id.empty_text);
        mLoadingView = itemView.findViewById(R.id.loading_view);
        mViewAdapter = new ViewAdapter(context);
        mViewPager.setAdapter(mViewAdapter);
        mLoadingView.start();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        if (onPageChangeListener != null) {
            mViewPager.addOnPageChangeListener(onPageChangeListener);
        }
    }

    public void updateView(PetModel petModel) {
        mLoadingView.stop();
        if (petModel == null || petModel.mPets.isEmpty()) {
            mViewPager.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
            return;
        }
        mEmptyText.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE); //有数据显示
        mViewAdapter.reset(petModel.mPets);
        mViewAdapter.notifyDataSetChanged();
    }

    private static class ViewAdapter extends PagerAdapter {

        protected List<PetData> mPetData;
        protected Context mContext;

        public ViewAdapter(Context context) {
            super();
            mContext = context;
            mPetData = new ArrayList<>();
        }

        public void clear() {
            mPetData.clear();
        }

        public void reset(List<PetData> petData) {
            mPetData.clear();
            mPetData.addAll(petData);
        }

        @Override
        public int getCount() {
            return mPetData.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            PetData data = mPetData.get(position);
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.home_loopmodule_loop_item, container, false);
            ImageView iv = view.findViewById(R.id.loop_item_iv);
            Glide.with(mContext).load(data.avatar).into(iv);
            container.addView(view);
            return view;
        }
    }
}
