package com.punuo.pet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.member.R;
import com.punuo.pet.member.R2;
import com.punuo.pet.router.MemberRouter;
import com.punuo.sys.sdk.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

@Route(path = MemberRouter.ROUTER_SYSTEM_NEWS_ACTIVITY)
public class SystemNewsActivity extends SwipeBackActivity {
    @BindView(R2.id.title)
    TextView title;
    @BindView(R2.id.back)
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_systemnews);
        ButterKnife.bind(this);
        initView();
    }

    public void initView(){
        title.setText("系统消息");
        ToastUtils.showToast("该功能善未完善");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
