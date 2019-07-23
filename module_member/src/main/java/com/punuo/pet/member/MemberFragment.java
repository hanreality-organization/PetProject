package com.punuo.pet.member;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.request.LogoutRequest;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.StatusBarUtil;


/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 我的页面
 **/
@Route(path = MemberRouter.ROUTER_MEMBER_FRAGMENT)
public class MemberFragment extends BaseFragment {

    private TextView mTitle;
    private ImageView mBack;
    private TextView mExitButton;
    private TextView mAddPetButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_member, container, false);
        initView();
        View statusBar = mFragmentView.findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(getActivity());
            statusBar.setVisibility(View.VISIBLE);
            statusBar.requestLayout();
        }
        return mFragmentView;
    }

    private void initView() {
        mBack = mFragmentView.findViewById(R.id.back);
        mTitle = mFragmentView.findViewById(R.id.title);
        mExitButton = mFragmentView.findViewById(R.id.exit_button);
        mAddPetButton = mFragmentView.findViewById(R.id.add_pet_button);
        mBack.setVisibility(View.GONE);
        mTitle.setText("我的");

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(AccountManager.getSession());
            }
        });
        mAddPetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(MemberRouter.ROUTER_ADD_PET_ACTIVITY)
                        .navigation();
            }
        });
    }


    private LogoutRequest mLogoutRequest;
    public void logout(String userName) {
        if (mLogoutRequest != null && !mLogoutRequest.isFinish()) {
            return;
        }
        showLoadingDialog("正在退出...");
        mLogoutRequest = new LogoutRequest();
        mLogoutRequest.addUrlParam("userName", userName);
        mLogoutRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                dismissLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    AccountManager.clearAccountData();
                    ARouter.getInstance().build(MemberRouter.ROUTER_LOGIN_ACTIVITY).navigation();
                    getActivity().finish();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mLogoutRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
