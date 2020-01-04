package com.punuo.sys.app.video.holder;

import android.view.View;
import android.widget.ImageView;
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
    private ImageView mSelectedView;
    public MusicHolder(View itemView) {
        super(itemView);
        mMusicName = itemView.findViewById(R.id.music_name);
        mSelectedView = itemView.findViewById(R.id.music_selected);
    }

    @Override
    protected void bindData(MusicItem musicItem, int position) {
        if (musicItem.selected) {
            mSelectedView.setVisibility(View.VISIBLE);
        } else {
            mSelectedView.setVisibility(View.INVISIBLE);
        }
        if (musicItem.url != null) {
            mMusicName.setText(musicItem.url);
        }
    }
}
