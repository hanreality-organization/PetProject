package com.punuo.pet.home.feed;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.model.PetData;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.ViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
@Route(path = HomeRouter.ROUTER_FEED_ACTIVITY)
public class FeedActivity extends BaseSwipeBackActivity {
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.pet_avatar)
    RoundedImageView mPetAvatar;
    @BindView(R2.id.pet_nick)
    TextView mPetNick;

    @Autowired(name = "petData")
    PetData mPetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTitle.setText("健康喂养");
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (mPetData != null) {
            Glide.with(this).load(mPetData.avatar).into(mPetAvatar);
            ViewUtil.setText(mPetNick, mPetData.petname);
        }

    }
}
