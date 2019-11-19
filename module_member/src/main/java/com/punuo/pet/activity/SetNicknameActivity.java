package com.punuo.pet.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.request.ChangeNickRequest;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.ToastUtils;
import com.punuo.sys.sdk.view.CleanEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Kuiya on 2019/7/29.
 */
@Route(path = MemberRouter.ROUTER_SET_NICKNAME_ACTIVITY)
public class SetNicknameActivity extends SwipeBackActivity {

    private TextView mSetNickBtn;
    private ImageView mBack;
    private TextView mTitle;
    private CleanEditText mNewNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setnick);
        init();
        super.onCreate(savedInstanceState);
    }

    public void init() {

        mTitle = (TextView)findViewById(R.id.title);
        mNewNick = (CleanEditText)findViewById(R.id.edit_new_nick);
        mSetNickBtn = (TextView) findViewById(R.id.set_nick_btn);

        mTitle.setText("修改昵称");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToFinishActivity();
            }
        });


        mSetNickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = mNewNick.getText().toString().trim();
                Log.i("update_nickname", "新昵称"+nickname);
                if(nickname.length()==0){
                    ToastUtils.showToast("请输入昵称");
                }
                setNickName(AccountManager.getUserName(),nickname);
            }
        });
    }

    private ChangeNickRequest mChangeNickRequest;
    public void setNickName(String username,String nickname){
        if(mChangeNickRequest != null&& mChangeNickRequest.isFinished){
            return;
        }
        mChangeNickRequest = new ChangeNickRequest();
        mChangeNickRequest.addUrlParam("userName", username);
        mChangeNickRequest.addUrlParam("nickName",nickname);
        mChangeNickRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(BaseModel result) {
                if(result==null){
                    return;
                }
                if(result.success){
                    scrollToFinishActivity();
                    ToastUtils.showToast("昵称修改成功");
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mChangeNickRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
