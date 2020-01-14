package com.punuo.sys.app.video.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.punuo.sys.app.video.R;
import com.punuo.sys.sdk.util.CommonUtil;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mPathList;

    public VideoAdapter(Context context, List<String> pathList) {
        mContext = context;
        mPathList = pathList;
    }

    public void addData(List<String> pathList) {
        mPathList.clear();
        if (pathList != null) {
            mPathList.addAll(pathList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.video_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String path = mPathList.get(position);
        String year = path.substring(56, 60);
        String month = path.substring(60, 62);
        String day = path.substring(62, 64);
        String hour = path.substring(64, 66);
        String minute = path.substring(66, 68);
        String second = path.substring(68, 70);
        holder.CreateTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        int width = CommonUtil.getWidth() - CommonUtil.dip2px(20);
        int height = 480 * width / 640;
        holder.mFirstFrame.getLayoutParams().width = width;
        holder.mFirstFrame.getLayoutParams().height = height;
        holder.mVideoView.getLayoutParams().width = width;
        holder.mVideoView.getLayoutParams().height = height;
        RequestOptions requestOptions = new RequestOptions()
                .frame(1000000)
                .error(R.drawable.ic_video_error)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop();
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(path)
                .into(holder.mFirstFrame);
        if (holder.mVideoView.isPlaying()) {
            holder.mVideoView.stopPlayback();
        }
        holder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                holder.mFirstFrame.setVisibility(View.VISIBLE);
                holder.mPlay.setVisibility(View.VISIBLE);
                holder.mPlay.setImageResource(R.drawable.video_ic_play);
            }
        });

        holder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.mVideoView.isPlaying()) {
                    holder.mVideoView.pause();
                    holder.mPlay.setVisibility(View.VISIBLE);
                    holder.mPlay.setImageResource(R.drawable.video_ic_pause);
                } else {
                    holder.mVideoView.setVideoPath(path);
                    holder.mVideoView.start();
                    holder.mPlay.setVisibility(View.GONE);
                    holder.mFirstFrame.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPathList == null ? 0 : mPathList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CreateTime;
        VideoView mVideoView;
        ImageView mFirstFrame;
        ImageView mPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            mFirstFrame = itemView.findViewById(R.id.video_first);
            CreateTime = itemView.findViewById(R.id.create_time);
            mVideoView = itemView.findViewById(R.id.videoView);
            mPlay = itemView.findViewById(R.id.video_play);
        }
    }
}