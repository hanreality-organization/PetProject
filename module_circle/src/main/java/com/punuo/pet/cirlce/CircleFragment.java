package com.punuo.pet.cirlce;

import android.os.Build;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.cirlce.adapter.CircleTabFragmentPagerAdapter;
import com.punuo.pet.router.CircleRouter;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.view.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kuiya on 2019/8/1.
 */

@Route(path = CircleRouter.ROUTER_CIRCLE_FRAGMENT)
public class CircleFragment extends BaseFragment {

    @BindView(R2.id.iv_contacts)
    ImageView mIvContacts;
    @BindView(R2.id.circle_tab)
    PagerSlidingTabStrip mCircleTab;
    @BindView(R2.id.iv_add)
    ImageView mIvAdd;
    @BindView(R2.id.circle_viewpager)
    ViewPager mCircleViewpager;
    private CircleTabFragmentPagerAdapter mCircleTabFragmentPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_main_circle, container, false);
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        ButterKnife.bind(this, mFragmentView);
        initView();
        return mFragmentView;
    }

    //初始化布局
    public void initView() {
        mCircleTabFragmentPagerAdapter = new CircleTabFragmentPagerAdapter(getChildFragmentManager());
        mCircleViewpager.setAdapter(mCircleTabFragmentPagerAdapter);
        mCircleTab.setViewPager(mCircleViewpager);

        mIvContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(CircleRouter.ROUTER_CONTACT_ACTIVITY)
                        .navigation();
            }
        });

        mIvAdd.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(CircleRouter.ROUTER_PUBLISH_ACTIVITY)
                        .navigation();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
