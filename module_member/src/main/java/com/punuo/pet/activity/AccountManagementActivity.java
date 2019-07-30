package com.punuo.pet.activity;

import android.app.FragmentManager;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import android.widget.Scroller;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;


/**
 * Created by Kuiya on 2019/7/29.
 */

@Route(path = MemberRouter.ROUTER_ACCOUNT_MANAGEMENT_ACTIVITY)
public class AccountManagementActivity extends BaseSwipeBackActivity implements View.OnClickListener{

    private FragmentManager mfragmentManager;
    private TextView mTitle;
    private ImageView mAvater;
    private ImageView mBack;
    private TextView mNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        init();
    }

    public void init(){
        mTitle = (TextView) findViewById(R.id.title);
        mBack = (ImageView) findViewById(R.id.back);
        mAvater = (ImageView) findViewById(R.id.image_change_avater);
        mNickname = (TextView) findViewById(R.id.text_nickname);

        mTitle.setText("账号管理");

        mBack.setOnClickListener(this);

        //头像
        String avater = AccountManager.getUserInfo().avatar;
        Glide.with(this).load(avater).into(mAvater);
        //昵称
        String nickname = AccountManager.getUserInfo().nickName;
        mNickname.setText(nickname);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.change_avater){
            ARouter.getInstance().build(MemberRouter.ROUTER_CHANGE_AVATER_ACTIVITY).navigation();
        }else if(id == R.id.set_nickname){
            ARouter.getInstance().build(MemberRouter.ROUTER_SET_NICKNAME_ACTIVITY).navigation();
        }else if(id == R.id.set_password){
            ARouter.getInstance().build(MemberRouter.ROUTER_SET_PASSWORD_ACTIVITY).navigation();
        }else if(id == R.id.back){
            scrollToFinishActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
