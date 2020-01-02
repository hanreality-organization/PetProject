package com.punuo.pet.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.member.R;
import com.punuo.pet.member.R2;
import com.punuo.pet.router.MemberRouter;
import com.punuo.pet.router.SDKRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.IntentUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = MemberRouter.ROUTER_TEST_ACTIVITY)
public class TestActivtity extends BaseSwipeBackActivity {
    @BindView(R2.id.edit)
    EditText edit;
    @BindView(R2.id.btn)
    Button btn;
    Context context;
    static String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        ButterKnife.bind(this);
        init();
    }

    public void init(){

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = edit.getText().toString().trim();
                Log.i("url", url);
                ARouter.getInstance().build(SDKRouter.ROUTER_WEB_VIEW_ACTIVITY).withString("url",url).navigation();
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
