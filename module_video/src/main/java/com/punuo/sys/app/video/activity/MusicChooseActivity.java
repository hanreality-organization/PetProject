package com.punuo.sys.app.video.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.VideoRouter;
import com.punuo.sip.SipUserManager;
import com.punuo.sip.request.SipSendMusicRequest;
import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.R2;
import com.punuo.sys.app.video.adapter.MusicAdapter;
import com.punuo.sys.app.video.audio.G711Code;
import com.punuo.sys.app.video.model.MusicItem;
import com.punuo.sys.app.video.model.MusicModel;
import com.punuo.sys.app.video.request.GetMusicListRequest;
import com.punuo.sys.app.video.request.UploadAudioRequest;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.httplib.upload.UploadResult;
import com.punuo.sys.sdk.util.HandlerExceptionUtils;
import com.punuo.sys.sdk.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by han.chen.
 * Date on 2020-01-04.
 **/
@Route(path = VideoRouter.ROUTER_MUSIC_CHOOSE_ACTIVITY)
@RuntimePermissions
public class MusicChooseActivity extends BaseSwipeBackActivity {

    @BindView(R2.id.music_list)
    RecyclerView mMusicList;
    @BindView(R2.id.title)
    TextView mTitle;
    @BindView(R2.id.back)
    ImageView mBack;
    @BindView(R2.id.sub_title)
    TextView mSubTitle;
    @BindView(R2.id.record_voice)
    TextView mRecordVoice;

    @Autowired(name = "devId")
    String devId;

    private MusicAdapter mMusicAdapter;
    private File recordFile;
    private File finalFile;
    private String finalFileName = "";
    private static final String prefixPath = Environment.getExternalStorageDirectory().getPath() + "/punuo/audio/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_choose);
        ARouter.getInstance().inject(this);
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

        mRecordVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    MusicChooseActivityPermissionsDispatcher.checkPermissionsWithCheck(MusicChooseActivity.this);
                } else {
                    closeRecord();
                    mRecordVoice.setText("录制音频");
                    renameFile();
                }
            }
        });
    }

    private void stopMusic() {
        SipSendMusicRequest sipSendMusicRequest
                = new SipSendMusicRequest(devId, "stop");
        SipUserManager.getInstance().addRequest(sipSendMusicRequest);
        if (mMusicAdapter != null) {
            mMusicAdapter.reset();
        }
    }

    private void renameFile() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_item, null);
        final EditText input = view.findViewById(R.id.input_view);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("保存录音")
                .setView(view)
                .setPositiveButton("保存并上传", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newFileName = input.getText().toString().trim();
                        if (TextUtils.isEmpty(newFileName)) {
                            ToastUtils.showToast("请输入文件名");
                            return;
                        }
                        File newFile = new File(prefixPath, newFileName + ".g711");
                        if (recordFile != null) {
                            boolean result = recordFile.renameTo(newFile);
                            if (result) {
                                finalFile = newFile;
                                finalFileName = newFileName;
                                uploadAudio();
                                dialog.dismiss();
                            }
                        }
                    }
                })
                .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (recordFile != null) {
                            recordFile.delete();
                        }
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
    }

    private AudioRecord audioRecord = null;//声明AudioRecord对象
    private boolean isRecording = false;//是否录音

    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO})
    void checkPermissions() {
        createAudioRecord();
        mRecordVoice.setText("停止录音");
    }

    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO})
    void permissionsDenied() {
        ToastUtils.showToast("权限能获取失败");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MusicChooseActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private void createAudioRecord() {
        final int minBufferSize = AudioRecord.getMinBufferSize(8000,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,//the recording source
                8000,
                AudioFormat.CHANNEL_IN_STEREO,
                AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize);

        recordFile = new File(prefixPath, System.currentTimeMillis() + ".g711");
        if (!recordFile.mkdirs()) {
            Log.e("demo failed---->", "Directory not created");
        }
        if (recordFile.exists()) {
            recordFile.delete();
        }
        audioRecord.startRecording();
        isRecording = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream os = null;
                short[] audioData = new short[160];
                byte[] encodeData = new byte[160];

                try {
                    os = new FileOutputStream(recordFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (null != os) {
                    while (isRecording) {
                        int read = audioRecord.read(audioData, 0, 160);
                        if (read < 0) continue;
                        calc2(audioData, 0, read);
                        //调用G711编码
                        G711Code.G711aEncoder(audioData, encodeData, read);
                        try {
                            //写G711本地
                            os.write(encodeData);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("写g711异常", e.toString() + "");
                        }
                    }
                    try {
                        Log.e("run------>", "close file output stream !");
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    void calc2(short[] lin, int off, int len) {
        int i, j;

        for (i = 0; i < len; i++) {
            j = lin[i + off];
            lin[i + off] = (short) (j >> 1);
        }
    }

    private void closeRecord() {
        isRecording = false;
        if (null != audioRecord) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeRecord();
    }
    private UploadAudioRequest request;
    private void uploadAudio() {
        if (request != null && !request.isFinish()) {
            return;
        }
        if (finalFile == null) {
            return;
        }
        showLoadingDialog("正在上传...");
        request = new UploadAudioRequest();
        request.addEntityParam("audio", finalFile);
        request.addEntityParam("devid", devId);
        request.addEntityParam("fileName", finalFileName);
        request.setRequestListener(new RequestListener<UploadResult>() {
            @Override
            public void onComplete() {
                dismissLoadingDialog();
                finalFile.delete();
            }

            @Override
            public void onSuccess(UploadResult result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    getData();
                }
                if (!TextUtils.isEmpty(result.message)) {
                    ToastUtils.showToast(result.message);
                }
            }

            @Override
            public void onError(Exception e) {
                HandlerExceptionUtils.handleException(e);
            }
        });
        HttpManager.addRequest(request);

    }
}
