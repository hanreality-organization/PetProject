package com.punuo.pet.feed.feednow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.sip.SipManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.punuo.pet.feed.FeedFragment;
import com.punuo.pet.feed.R;
import com.punuo.sip.SipUserManager;
import com.punuo.sys.sdk.account.AccountManager;

public class FeedDialog extends Dialog implements View.OnClickListener{
    //在构造方法里提前加载了样式
    private Context context;//上下文
    private int layoutResID;//布局文件id
    private int[] listenedItem;//监听的控件id
    private TextView mCountText;
    private TextView mSubCountText;
    private TextView mAddCountText;
    private Button mComplete;
    public static int defaultCount = 3;
    public FeedDialog(Context context,int layoutResID,int[] listenedItem){
        super(context, R.style.MyDialog);//加载dialog的样式
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItem = listenedItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //提前设置Dialog的一些样式
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);//设置dialog显示居中
        //dialogWindow.setWindowAnimations();设置动画效果
        setContentView(layoutResID);
        initDialog();
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5;// 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失

    }

    public void initDialog(){
        mCountText = findViewById(R.id.count);
        mSubCountText = findViewById(R.id.sub_count);
        mAddCountText = findViewById(R.id.add_count);
        mComplete = findViewById(R.id.complete);
        mSubCountText.setOnClickListener(this);
        mAddCountText.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        String count = defaultCount+"份";
        mCountText.setText(count);
    }


    private OnCenterItemClickListener listener;
    public interface OnCenterItemClickListener {
        void OnCenterItemClick(FeedDialog dialog, View view);
    }
    //很明显我们要在这里面写个接口，然后添加一个方法
    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }


//    @Override
//    public void onClick(View v) {
//        dismiss();//注意：我在这里加了这句话，表示只要按任何一个控件的id,弹窗都会消失，不管是确定还是取消。
//        listener.OnCenterItemClick(this,v);
//    }

    private int outCount;
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id==R.id.sub_count){
            if (defaultCount>0) {
                mCountText.setText((defaultCount -= 1)+"份");
            }else {return;}
        }else if(id==R.id.add_count){
            mCountText.setText((defaultCount+=1)+"份");
        }else if(id==R.id.complete){
            if(defaultCount==0){
                return;
            }else{
                //TODO 调动云台使其出粮
               String feedCount =  mCountText.getText().toString().trim();//获取到相应的份数
               outGrain(feedCount);
               dismiss();
            }
        }
        outCount+=defaultCount;//TODO 无法实现统计一天内的喂食份数；
    }

    public void outGrain(String feedcount){//出粮
        FeedNowSipRequest mFeedNowSipRequest = new FeedNowSipRequest(AccountManager.getUserName(),feedcount);
        SipUserManager.getInstance().addRequest(mFeedNowSipRequest);
        Log.i("feed", "出粮请求发送中......");
    }

}

