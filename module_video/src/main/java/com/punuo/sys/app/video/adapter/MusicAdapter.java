package com.punuo.sys.app.video.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.activity.MusicChooseActivity;
import com.punuo.sys.app.video.holder.MusicHolder;
import com.punuo.sys.app.video.model.MusicItem;
import com.punuo.sys.app.video.request.DeleteAudioRequest;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.httplib.upload.UploadResult;
import com.punuo.sys.sdk.recyclerview.BaseRecyclerViewAdapter;
import com.punuo.sys.sdk.util.ToastUtils;

import java.util.List;

/**
 * Created by han.chen.
 * Date on 2020-01-04.
 **/
public class MusicAdapter extends BaseRecyclerViewAdapter<MusicItem> {

    private String devId;

    public MusicAdapter(Context context, List<MusicItem> data, String devId) {
        super(context, data);
        this.devId = devId;
    }

    public void addMusicList(List<String> urls) {
        mData.clear();
        for (int i = 0; i < urls.size(); i++) {
            MusicItem musicItem = new MusicItem();
            musicItem.url = urls.get(i);
            mData.add(musicItem);
        }
        notifyDataSetChanged();
    }

    public void reset() {
        for (int i = 0; i < mData.size(); i++) {
            MusicItem musicItem = mData.get(i);
            musicItem.selected = false;
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MusicHolder(LayoutInflater.from(mContext).inflate(R.layout.music_item, parent, false));
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder baseViewHolder, final int position) {
        if (baseViewHolder instanceof MusicHolder) {
            ((MusicHolder) baseViewHolder).bind(mData.get(position), position);
            ((MusicHolder) baseViewHolder).setPlayMusicListener(v -> {
                MusicItem item = mData.get(position);
                if (v.getContext() instanceof MusicChooseActivity) {
                    ((MusicChooseActivity) v.getContext()).prepareListen(item);
                }
            });
            ((MusicHolder) baseViewHolder).setDeleteMusicListener(v -> {
                final MusicItem item = mData.get(position);
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("温馨提示")
                        .setMessage("确认删除" + item.getFileName())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDevAudio(item.getFileName(), position);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(true);
                dialog.show();
            });
        }
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    @Override
    public int getBasicItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private void deleteDevAudio(String name, final int position) {
        final DeleteAudioRequest request = new DeleteAudioRequest();
        request.addUrlParam("devid", devId);
        request.addUrlParam("fileName", name) ;
        request.setRequestListener(new RequestListener<UploadResult>() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(UploadResult result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    remove(position);
                }
                ToastUtils.showToast(result.message);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(request);
    }


}
