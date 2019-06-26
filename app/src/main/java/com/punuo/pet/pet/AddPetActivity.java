package com.punuo.pet.pet;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.punuo.pet.R;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-06-26.
 **/
public class AddPetActivity extends BaseSwipeBackActivity {

    @Bind(R.id.title)
    TextView mTitle;
    @Bind(R.id.back)
    ImageView mBack;
    @Bind(R.id.sub_title)
    TextView mSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToFinishActivity();
            }
        });
        mTitle.setText("添加宠物");
        mSubTitle.setText("下一步");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
