package com.punuo.sys.app.video.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipSendMusicRequest;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sys.app.video.adapter.MusicAdapter;
import com.punuo.sys.app.video.model.MusicItem;
import com.punuo.sys.app.video.model.MusicModel;
import com.punuo.sys.app.video.request.GetMusicListRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by han.chen.
 * Date on 2020-01-04.
 **/
@Route(path = VideoRouter.ROUTER_MUSIC_CHOOSE_ACTIVITY)
public class MusicChooseActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.music_list)
    RecyclerView mMusicList;
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;

    private MusicAdapter mMusicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_choose);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void getData() {
        GetMusicListRequest getMusicListRequest = new GetMusicListRequest();
        getMusicListRequest.addUrlParam("userName", AccountManager.getUserName());
        getMusicListRequest.setRequestListener(new RequestListener<MusicModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(MusicModel result) {
                if (result == null) {
                    return;
                }
                if (result.mBasicMusicList != null) {
                    mMusicAdapter.addMusicList(result.mBasicMusicList);
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(getMusicListRequest);
    }

    private void initView() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText("选择音乐");
        mSubTitle.setVisibility(View.VISIBLE);
        mSubTitle.setText("停止播放");
        mSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusic();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mMusicList.setLayoutManager(layoutManager);
        mMusicAdapter = new MusicAdapter(this, new ArrayList<MusicItem>());
        mMusicList.setAdapter(mMusicAdapter);
    }

    private void stopMusic() {
        SipSendMusicRequest sipSendMusicRequest
                = new SipSendMusicRequest("310023001301920001", "stop");
        SipUserManager.getInstance().addRequest(sipSendMusicRequest);
        if (mMusicAdapter != null) {
            mMusicAdapter.reset();
        }
    }
}
