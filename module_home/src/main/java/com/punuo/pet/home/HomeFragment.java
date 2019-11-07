package com.punuo.pet.home;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.punuo.pet.PetManager;
import com.punuo.pet.home.adapter.HomeAdapter;
import com.punuo.pet.home.module.HomeHeadModule;
import com.punuo.pet.model.PetModel;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 首页
 **/
@Route(path = HomeRouter.ROUTER_HOME_FRAGMENT)
public class HomeFragment extends BaseFragment {
    @BindView(R2.id.rv_list)
    PullToRefreshRecyclerView mPullToRefreshRecyclerView;

    private RecyclerView mRecyclerView;
    private HomeAdapter mHomeAdapter;
    private LinearLayout mHeadView;
    private HomeHeadModule mHomeHeadModule;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, mFragmentView);
        initView();
        View mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.requestLayout();
        }
        EventBus.getDefault().register(this);
        PetManager.getPetInfo(mRequestListener);
        return mFragmentView;
    }

    private void initView() {
        mHeadView = new LinearLayout(getActivity());
        mHeadView.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView = mPullToRefreshRecyclerView.getRefreshableView();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mHomeAdapter = new HomeAdapter(getActivity(), new ArrayList<Object>());
        mHomeAdapter.setHeaderView(mHeadView);
        mRecyclerView.setAdapter(mHomeAdapter);
        mPullToRefreshRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                PetManager.getPetInfo(mRequestListener);
            }
        });
        mHomeHeadModule = new HomeHeadModule(getActivity(), mHeadView);
        mHeadView.addView(mHomeHeadModule.getRootView());
    }
    private RequestListener<PetModel> mRequestListener = new RequestListener<PetModel>() {
        @Override
        public void onComplete() {
            mPullToRefreshRecyclerView.onRefreshComplete();
        }

        @Override
        public void onSuccess(PetModel result) {
            if (result != null) {
                mHomeHeadModule.updateView(result);
            }
        }

        @Override
        public void onError(Exception e) {
            HandlerExceptionUtils.handleException(e);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final PetModel model) {
        if (model == null) {
            return;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
