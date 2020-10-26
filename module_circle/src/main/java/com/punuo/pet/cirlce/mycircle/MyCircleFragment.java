package com.punuo.pet.cirlce.mycircle;


import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.punuo.pet.cirlce.R;
import com.punuo.pet.cirlce.R2;
import com.punuo.pet.cirlce.mycircle.adapter.MyCircleAdapter;
import com.punuo.pet.cirlce.mycircle.model.CircleItem;
import com.punuo.pet.cirlce.mycircle.module.HeadModule;
import com.punuo.sys.sdk.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kuiya on 2019/8/1.
 */

public class MyCircleFragment extends BaseFragment {

    @BindView(R2.id.pull_to_refresh)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;
    private RecyclerView mRecyclerView;
    private MyCircleAdapter mMyCircleAdapter;
    private LinearLayout mHeadContainer;
    private HeadModule mHeadModule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_circle_pet, container, false);
        ButterKnife.bind(this, mFragmentView);
        initView();
        return mFragmentView;
    }

    private void initView() {
        mPullToRefreshRecyclerView.setMode(PullToRefreshBase.Mode.DISABLED);//设置刷新模式
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mHeadContainer = new LinearLayout(getActivity());
        mHeadContainer.setOrientation(LinearLayout.VERTICAL);
        mMyCircleAdapter = new MyCircleAdapter(getActivity(), new ArrayList<CircleItem>());
        mMyCircleAdapter.setHeaderView(mHeadContainer);//设置头部布局
        mRecyclerView.setAdapter(mMyCircleAdapter);

        initHeader();
        mMyCircleAdapter.appendData();
    }

    private void initHeader() {
        mHeadModule = new HeadModule(getActivity(), mHeadContainer);
        mHeadContainer.addView(mHeadModule.getRootView());
        mHeadModule.updateView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
