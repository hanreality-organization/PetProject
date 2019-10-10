package com.punuo.pet.cirlce.world;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.punuo.pet.cirlce.R;
import com.punuo.sys.sdk.fragment.BaseFragment;

/**
 * Created by Kuiya on 2019/8/1.
 */

public class WorldFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_circlr_world, container, false);
        return mFragmentView;
    }
}
