package com.punuo.pet.member.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.punuo.pet.member.R;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

/**
 * Created by han.chen.
 * Date on 2019-06-25.
 * 忘记密码页面
 **/
public class ForgetPasswordActivity extends BaseSwipeBackActivity {

    private TextView mTitle;
    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.title);
        mBack = (ImageView) findViewById(R.id.back);
        mTitle.setText("忘记密码");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
    }
}
