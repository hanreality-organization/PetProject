package com.punuo.pet.home.device;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.home.R;
import com.punuo.pet.home.R2;
import com.punuo.pet.router.HomeRouter;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.view.progress.WaterWaveProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2019-08-05.
 **/
@Route(path = HomeRouter.ROUTER_CONNECT_RESULT_ACTIVITY)
public class ConnectResultActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.left_text)
    TextView mLeftText;
    @BindView(R2.id.water_wave_progress)
    WaterWaveProgress mProgress;
    @BindView(R2.id.connect_status)
    TextView mConnectStatus;

    private ValueAnimator mValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_result_activity);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mBack.setVisibility(View.GONE);
        mLeftText.setText("取消");
        mLeftText.setTextColor(ContextCompat.getColor(this, R.color.cancel_text_color));
        mLeftText.setVisibility(View.VISIBLE);
        mSubTitle.setText("完成");
        mSubTitle.setVisibility(View.VISIBLE);
        mTitle.setText("尝试与设备建立连接");
        mLeftText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mProgress.setShowProgress(true);
        mProgress.animateWave();
        mProgress.setProgressListener(new WaterWaveProgress.ProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (progress == 100) {
                    mConnectStatus.setText("连接成功");
                    mProgress.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            onBackPressed();
                        }
                    }, 1000);
                }
            }
        });

        mValueAnimator = ValueAnimator.ofInt(100);
        mValueAnimator.setDuration(5000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                mProgress.setProgress(value);
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }
    }
}
