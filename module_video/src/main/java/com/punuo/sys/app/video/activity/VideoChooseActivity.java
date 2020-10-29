package com.punuo.sys.app.video.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.dev.DevManager;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sys.app.video.adapter.VideoAdapter;
import com.punuo.sys.app.video.event.VideoDelEvent;
import com.punuo.sys.app.video.model.VideoModel;
import com.punuo.sys.app.video.request.GetVideoListRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.DownloadManager;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.MD5Util;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
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

    private VideoAdapter mVideoAdapter;

    private DownloadManager mDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_choose);
        ButterKnife.bind(this);
        mDownloadManager = new DownloadManager();
        getData();
        initView();
        EventBus.getDefault().register(this);
    }

    private void getData() {
        GetVideoListRequest getVideoListRequest = new GetVideoListRequest();
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
                if (result.videolist != null) {
                    mVideoAdapter.addData(result.videolist);
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(getVideoListRequest);
    }

    public void initView() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitle.setText("选择视频");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mVideoList.setLayoutManager(layoutManager);
        mVideoAdapter = new VideoAdapter(this, new ArrayList<String>(), DevManager.getInstance().getDevId());
        mVideoAdapter.setShareClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = (String) v.getTag();
                VideoChooseActivityPermissionsDispatcher.checkPermissionsWithPermissionCheck(VideoChooseActivity.this, path);
            }
        });
        mVideoList.setAdapter(mVideoAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(VideoDelEvent event) {
        getData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void checkPermissions(String url) {
        showLoadingDialog();
        if (mDownloadManager != null) {
            mDownloadManager.download(url, getVideoPath(url), new DownloadManager.DownloadListener() {
                @Override
                public void onDownloadSuccess() {
                    dismissLoadingDialog();
                    File file = new File(getVideoPath(url));
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                    runOnUiThread(() -> shareVideoToWeiXin(file));
                }

                @Override
                public void onDownloadFailed() {
                    ToastUtils.showToast("下载视频,失败请重新下载");
                    dismissLoadingDialog();
                }

                @Override
                public void onDownloadProgress(float progress) {

                }
            });
        }
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permissionsDenied() {
        ToastUtils.showToast("权限能获取失败");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        VideoChooseActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode, grantResults);
    }

    private String getVideoPath(String url) {
        File appDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String imgPath = appDir.getAbsolutePath() + File.separator + MD5Util.getMD5String(url) + ".mp4";
        Log.i("generateImgPath", imgPath);
        return imgPath;
    }

    private void shareVideoToWeiXin(File file) {
        try {
            Intent intent = new Intent();
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
            intent.setComponent(comp);
            Uri uri = Uri.fromFile(file);
            intent.setAction("android.intent.action.SEND");
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
