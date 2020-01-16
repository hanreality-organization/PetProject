package com.punuo.pet.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.request.ChangePwdRequest;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.view.CleanEditText;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Kuiya on 2019/7/29.
 */
@Route(path = MemberRouter.ROUTER_CHANGE_PASSWORD_ACTIVITY)
public class ChangePasswordActivity extends SwipeBackActivity {

    private CleanEditText mNewPwd;
    private CleanEditText mNewPwdAgain;
    private TextView mBtn;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
    }


    public void init(){
        mTitle = (TextView)findViewById(R.id.title);
        mNewPwd = (CleanEditText) findViewById(R.id.edit_new_password);
        mNewPwdAgain =(CleanEditText) findViewById(R.id.edit_password_again);
        mBtn =(TextView) findViewById(R.id.edit_btn);

        mTitle.setText("修改密码");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToFinishActivity();
            }
        });
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newPwd = mNewPwd.getText().toString().trim();
                String newPwdAgain = mNewPwdAgain.getText().toString().trim();

                if(TextUtils.isEmpty(newPwd)){
                    ToastUtils.showToast("请输入新密码");
                }else if(TextUtils.isEmpty(newPwdAgain)){
                    ToastUtils.showToast("请再次输入新密码");
                }else if(!newPwd.equals(newPwdAgain)){
                    ToastUtils.showToast("输入的密码不一致，请再次输入");
                }
//                changePwd(AccountManager.getUserName(),newPwd);
                ToastUtils.showToast("功能尚未完善");
                //TODO 修改密码部分还未完成（1、如何判断是否是8-32位数字+字母、符号组合；2、剩余问题）
            }
        });
    }

    private ChangePwdRequest mChangePwdRequest;
    public void changePwd(String userName,String newPwd){
        if(mChangePwdRequest!=null&&mChangePwdRequest.isFinish()){
            return;
        }
        mChangePwdRequest = new ChangePwdRequest();
        mChangePwdRequest.addUrlParam("userName",userName);
        mChangePwdRequest.addUrlParam("newPwd",newPwd);
        mChangePwdRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                if(result==null){
                    return;
                }
                if(result.success){
                    Log.i("update_password", "成功修改密码");
                    ToastUtils.showToast(result.message+"请重新登陆");
                    ARouter.getInstance().build(MemberRouter.ROUTER_LOGIN_ACTIVITY).navigation();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mChangePwdRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
