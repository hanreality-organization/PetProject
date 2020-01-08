package com.punuo.sys.app.video.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sys.app.video.adapter.VideoAdapter;
import com.punuo.sys.app.video.model.VideoItem;
import com.punuo.sys.app.video.model.VideoModel;
import com.punuo.sys.app.video.request.GetVideoListRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = VideoRouter.ROUTER_VIDEO_CHOOSE_ACTIVITY)
public class VideoChooseActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.video_list)
    RecyclerView mVideoList;
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;

    private VideoAdapter mvideoAdapter;
    private static List<String> pathlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_choose);
        ButterKnife.bind(this);
        getData();
        initView();
    }

    private void getData() {
        GetVideoListRequest getVideoListRequest=new GetVideoListRequest();
        getVideoListRequest.addUrlParam("userName", AccountManager.getUserName());
        getVideoListRequest.setRequestListener(new RequestListener<VideoModel>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(VideoModel result) {
                if (result == null) {
                    return;
                }
                 if(result.videolist!=null){
                   pathlist=result.videolist;
                 }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(getVideoListRequest);
    }

    public void initView(){
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText("选择视频" );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mVideoList.setLayoutManager(layoutManager);
        mvideoAdapter=new VideoAdapter(this,pathlist);
        mVideoList.setAdapter(mvideoAdapter);
    }
}
