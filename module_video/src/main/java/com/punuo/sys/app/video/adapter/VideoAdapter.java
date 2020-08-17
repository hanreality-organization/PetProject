package com.punuo.sys.app.video.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.punuo.pet.router.VideoRouter;
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
        String year = path.substring(59, 63);
        String month = path.substring(63, 65);
        String day = path.substring(65, 67);
        String hour = path.substring(67, 69);
        String minute = path.substring(69, 71);
        String second = path.substring(71, 73);
        holder.CreateTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        int width = CommonUtil.getWidth() - CommonUtil.dip2px(20);
        int height = 480 * width / 640;
        holder.mFirstFrame.getLayoutParams().width = width;
        holder.mFirstFrame.getLayoutParams().height = height;
        RequestOptions requestOptions = new RequestOptions()
                .frame(1000000)
                .error(R.drawable.ic_video_error)
                .placeholder(R.drawable.ic_placeholder)
                .centerCrop();
        Glide.with(mContext)
                .setDefaultRequestOptions(requestOptions)
                .load(path)
                .into(holder.mFirstFrame);
        holder.mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(VideoRouter.ROUTER_VIDEO_PLAY_ACTIVITY)
                        .withString("url", path)
                        .withString("title", holder.CreateTime.getText().toString())
                        .navigation();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPathList == null ? 0 : mPathList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CreateTime;
        ImageView mFirstFrame;
        ImageView mPlay;

        public ViewHolder(View itemView) {
            super(itemView);
            mFirstFrame = itemView.findViewById(R.id.video_first);
            CreateTime = itemView.findViewById(R.id.create_time);
            mPlay = itemView.findViewById(R.id.video_play);
        }
    }
}
