package com.punuo.pet.cirlce.adapter;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.punuo.pet.cirlce.nearby.NearbyFragment;
import com.punuo.pet.cirlce.world.WorldFragment;
import com.punuo.pet.cirlce.mycircle.MyCircleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kuiya on 2019/8/1.
 */

public class CircleTabFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> mTab;

    public CircleTabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mTab = new ArrayList<>();
        mTab.add("我的圈");
        mTab.add("附近");
        mTab.add("世界");
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MyCircleFragment();
        } else if (position == 1) {
            return new NearbyFragment();
        } else {
            return new WorldFragment();
        }
    }

    @Override
    public int getCount() {
        return mTab.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTab.get(position);
    }
}
