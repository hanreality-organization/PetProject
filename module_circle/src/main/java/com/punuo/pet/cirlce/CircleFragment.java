package com.punuo.pet.cirlce;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.CircleRouter;
import com.punuo.sys.sdk.fragment.BaseFragment;

/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 宠友圈
 **/
@Route(path = CircleRouter.ROUTER_CIRCLE_FRAGMENT)
public class CircleFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_circle, container, false);
        return mFragmentView;
    }
}
