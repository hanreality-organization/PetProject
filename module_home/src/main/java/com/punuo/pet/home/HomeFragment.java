package com.punuo.pet.home;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 首页
 **/
@Route(path = HomeRouter.ROUTER_HOME_FRAGMENT)
public class HomeFragment extends BaseFragment {
    private ViewPager mViewPager;
    private List<View> mViewList = new ArrayList<View>();//存放待滑动的view
    private LayoutInflater mInflater;
    private View view_01, view_02, view_03;//三个待滑动的view
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        return mFragmentView;
    }

    private void initView() {
        mInflater = getLayoutInflater();
        view_01 = mInflater.inflate(R.layout.viewpager1, null);
        view_02 = mInflater.inflate(R.layout.viewpager2, null);
        view_03 = mInflater.inflate(R.layout.viewpager3, null);
//		view_01 = findViewById(R.layout.layout1);
//		view_02 = findViewById(R.layout.layout2);
//		view_03 = findViewById(R.layout.layout3);

        mViewList.add(view_01);
        mViewList.add(view_02);
        mViewList.add(view_03);

        mViewPager = (ViewPager) mFragmentView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setCurrentItem(0);//设置当前pager

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
