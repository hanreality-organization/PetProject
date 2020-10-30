package com.punuo.sys.app.video.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.punuo.sys.app.video.R;
import com.punuo.sys.app.video.model.MusicItem;
import com.punuo.sys.sdk.recyclerview.BaseViewHolder;

/**
 * Created by han.chen.
 * Date on 2020-01-04.
 **/
public class MusicHolder extends BaseViewHolder<MusicItem> {
    private TextView mMusicName;
    private TextView mPlayMusic;
    private TextView mDeleteMusic;
    private View.OnClickListener playMusicListener;
    private View.OnClickListener deleteMusicListener;

    public MusicHolder(View itemView) {
        super(itemView);
        mMusicName = itemView.findViewById(R.id.music_name);
        mPlayMusic = itemView.findViewById(R.id.play_music);
        mDeleteMusic = itemView.findViewById(R.id.delete_music);
    }

    public void setPlayMusicListener(View.OnClickListener playMusicListener) {
        this.playMusicListener = playMusicListener;
    }

    public void setDeleteMusicListener(View.OnClickListener deleteMusicListener) {
        this.deleteMusicListener = deleteMusicListener;
    }

    @Override
    protected void bindData(MusicItem musicItem, int position) {
        if (!TextUtils.isEmpty(musicItem.url)) {
            String title = musicItem.getFileName();
            mMusicName.setText(title);
            mPlayMusic.setOnClickListener(v -> {
                if (playMusicListener != null) {
                    playMusicListener.onClick(v);
                }
            });
            mDeleteMusic.setOnClickListener(v -> {
                if (deleteMusicListener != null) {
                    deleteMusicListener.onClick(v);
                }
            });
        }

    }
}
