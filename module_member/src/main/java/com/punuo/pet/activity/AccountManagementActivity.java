package com.punuo.pet.activity;

import android.app.FragmentManager;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
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
    private RelativeLayout mChangeAvater;
    private RelativeLayout mSetNickname;
    private RelativeLayout mChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_management);
        init();
    }

    public void init(){
        mTitle = (TextView) findViewById(R.id.title);
        mBack = (ImageView) findViewById(R.id.back);
        mNickname = (TextView) findViewById(R.id.text_nickname);
        mSetNickname  = (RelativeLayout)findViewById(R.id.set_nickname);
        mTitle.setText("账号管理");

        mBack.setOnClickListener(this);
        mSetNickname.setOnClickListener(this);

        //昵称
        String nickname = AccountManager.getUserInfo().nickName;
        mNickname.setText(nickname);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.set_nickname){
            ARouter.getInstance().build(MemberRouter.ROUTER_SET_NICKNAME_ACTIVITY).navigation();
        } else if(id == R.id.back){
            scrollToFinishActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
