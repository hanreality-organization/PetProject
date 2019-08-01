package com.punuo.pet.message;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.punuo.pet.router.MemberRouter;
import com.punuo.pet.router.MessageRouter;
import com.punuo.sys.sdk.fragment.BaseFragment;
import com.punuo.sys.sdk.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by han.chen.
 * Date on 2019-06-25.
 **/
@Route(path = MessageRouter.ROUTER_MESSAGE_FRAGMENT)
public class MessageFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG ="MessageFragment" ;

    private RelativeLayout mComment;
    private RelativeLayout mLike;
    private RelativeLayout mAite;
    private RelativeLayout mEquipmentNotify;
    private RelativeLayout mToolNotify;
    private RelativeLayout mMyIssue;

    private TextView mTitle;
    private  TextView count1;
    private  TextView count2;
    private  TextView count3;

    //新增的评论、点赞、艾特的数目
    private static int commentCount;
    private static int likeCount;
    private static int aiteCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_message, container, false);
        EventBus.getDefault().register(this);
        initView();
        return mFragmentView;

    }

    public void initView(){
        mComment = mFragmentView.findViewById(R.id.comment);
        mLike = mFragmentView.findViewById(R.id.like);
        mAite = mFragmentView.findViewById(R.id.aite);
        mEquipmentNotify = mFragmentView.findViewById(R.id.equipment_notify);
        mToolNotify = mFragmentView.findViewById(R.id.tool_notify);
        mMyIssue = mFragmentView.findViewById(R.id.myissue);
        mTitle = mFragmentView.findViewById(R.id.title);
        count1 = mFragmentView.findViewById(R.id.count1);

        mTitle.setText("消息");
        /**
        if(commentCount != 0){
            count1.setText(String.valueOf(commentCount));
            count1.setVisibility(View.VISIBLE);
        }else{
            count1.setVisibility(View.INVISIBLE);
        }
         */

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message message){
        if(message.equals("取消新评论提示")){
            handler.sendEmptyMessage(0x11);
        }else if(message.equals("取消新点赞提示")){
            handler.sendEmptyMessage(0x22);
        }
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View view) {
        //在Android Library中R.java中的资源ID不是常数，不再使用switch—case
        int id = view.getId();
        if(id == R.id.comment) {
            ARouter.getInstance().build(MessageRouter.ROUTER_COMMENTVIEW).navigation();
        }
        else if(id == R.id.like){
            ARouter.getInstance().build(MessageRouter.ROUTER_ADDLIKEVIEW).navigation();
        }
        else if(id == R.id.aite){
            ARouter.getInstance().build(MessageRouter.ROUTER_AITEVIEW).navigation();
        }
        else if(id == R.id.equipment_notify){
            ARouter.getInstance().build(MessageRouter.ROUTER_EQUIPMENTNOTIFY).navigation();
        }
        else if(id == R.id.tool_notify){
            ARouter.getInstance().build(MessageRouter.ROUTER_TOOLNOTIFY).navigation();
        }
        else if(id == R.id.myissue){
            ARouter.getInstance().build(MessageRouter.ROUTER_MYISSUE).navigation();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

}
