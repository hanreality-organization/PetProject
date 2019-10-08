package com.punuo.pet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.R2;
import com.punuo.pet.member.pet.fragment.AddUserInfoFragment;
import com.punuo.pet.member.pet.model.UserParam;
import com.punuo.pet.member.pet.request.AddUserInfoRequest;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.account.UserManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = MemberRouter.ROUTER_EDITUSERINFO_ACTIVITY)
public class EditUserInfoActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;
    @BindView(R2.id.sub_title)
    TextView subTitle;

    private AddUserInfoFragment mAddUserInfoFragment;
    private AddUserInfoRequest mAddUserInfoRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituserinfo);

        loadFragment();
        init();
        ButterKnife.bind(this);
    }

    public void init() {
        title.setText("修改个人信息");
        subTitle.setText("完成");
    }

    public void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.userinfo_container, mAddUserInfoFragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void editUserInfo(UserParam userParam) {
        if (mAddUserInfoRequest != null && !mAddUserInfoRequest.isFinish()) {
            return;
        }
        mAddUserInfoRequest = new AddUserInfoRequest();
        mAddUserInfoRequest.addUrlParam("userName", AccountManager.getUserName());
        mAddUserInfoRequest.addUrlParam("photo", userParam.avatar);
        mAddUserInfoRequest.addUrlParam("birth", userParam.birth);
        mAddUserInfoRequest.addUrlParam("nickName", userParam.nickName);
        mAddUserInfoRequest.addUrlParam("gender", userParam.gender);
        mAddUserInfoRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    UserManager.getUserInfo(AccountManager.getUserName());
                    finish();
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mAddUserInfoRequest);
    }

    @OnClick({R2.id.title, R2.id.back, R2.id.sub_title})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.back) {
            scrollToFinishActivity();
        } else if (id == R.id.sub_title) {
            editUserInfo(mAddUserInfoFragment.getUserParam());
            ARouter.getInstance().build(MemberRouter.ROUTER_MEMBER_FRAGMENT).navigation();
        }
    }
}
