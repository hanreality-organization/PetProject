package com.punuo.pet.cirlce.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.punuo.pet.cirlce.R;

/**
 * Created by Kuiya on 2019/8/1.
 */

public class MyCircleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_circle_one,container,false);

        return view;
    }
}
