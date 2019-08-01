package com.punuo.pet.cirlce.adapter;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kuiya on 2019/8/1.
 */

public class CircleFragmentPagerAdapter extends FragmentPagerAdapter {

    //存储所有的fragment
    List<Fragment> list;

    public CircleFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
