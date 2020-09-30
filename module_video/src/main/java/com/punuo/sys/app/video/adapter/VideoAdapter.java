package com.punuo.sys.app.video.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
import com.punuo.sys.app.video.event.VideoDelEvent;
import com.punuo.sys.app.video.request.DeleteVideoRequest;
import com.punuo.sys.sdk.activity.BaseActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.CommonUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mPathList;
    private String devId;


    public VideoAdapter(Context context, List<String> pathList, String devId) {
        mContext = context;
        mPathList = pathList;
        this.devId = devId;
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
        final int index = position;
        final String path = mPathList.get(position);
        final String fileName = path.substring(path.lastIndexOf("/") + 1);
        String date = fileName.substring(0, fileName.lastIndexOf("."));
        try {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            String day = date.substring(6, 8);
            String hour = date.substring(8, 10);
            String minute = date.substring(10, 12);
            String second = date.substring(12);
            holder.CreateTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second);
        } catch (Exception e) {
            e.printStackTrace();
            holder.CreateTime.setText(date);
        }
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
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVideo(fileName, index);
            }
        });

    }

    private void deleteVideo(String fileName, final int position) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).showLoadingDialog();
        }
        final DeleteVideoRequest request = new DeleteVideoRequest();
        request.addUrlParam("filename", fileName);
        request.addUrlParam("devid", devId);
        request.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                if (mContext instanceof BaseActivity) {
                    ((BaseActivity) mContext).dismissLoadingDialog();
                }
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result == null) {
                    return;
                }
                if (result.success) {
                    EventBus.getDefault().post(new VideoDelEvent());
                }
                ToastUtils.showToast(result.message);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(request);
    }

    @Override
    public int getItemCount() {
        return mPathList == null ? 0 : mPathList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CreateTime;
        ImageView mFirstFrame;
        ImageView mPlay;
        TextView mDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            mFirstFrame = itemView.findViewById(R.id.video_first);
            CreateTime = itemView.findViewById(R.id.create_time);
            mPlay = itemView.findViewById(R.id.video_play);
            mDelete = itemView.findViewById(R.id.delete_video);
        }
    }
}
