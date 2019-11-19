package com.punuo.pet.feed.plan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.punuo.pet.feed.R;
import com.punuo.pet.feed.feednow.FeedDialog;

public class SelectCountDialog extends Dialog implements View.OnClickListener {

    private TextView mSelectCount;
    private TextView mAddCount;
    private TextView mLessCount;
    private Button mComplete;
    int defaultCount = 3;

    //在构造方法里提前加载了样式
    private Context context;//上下文
    private int layoutResID;//布局文件id
    private int[] ListenedItem;
    public SelectCountDialog(Context context,int layoutResID,int[] ListenedItem){
        super(context, R.style.MyDialog);
        this.context = context;
        this.layoutResID = layoutResID;
        this.ListenedItem = ListenedItem;
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
        mSelectCount = findViewById(R.id.select_count);
        mLessCount = findViewById(R.id.less_count);
        mAddCount = findViewById(R.id.add_count);
        mComplete = findViewById(R.id.select_complete);
        mLessCount.setOnClickListener(this);
        mAddCount.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        String count = String.valueOf(defaultCount);
        mSelectCount.setText(count);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id== R.id.less_count){
            if(defaultCount>0){
                mSelectCount.setText(""+(defaultCount-=1));
            }else { return; }
        }
        if(id== R.id.add_count){
            mSelectCount.setText(""+(defaultCount+=1));
        }
        if(id== R.id.select_complete){
            dismiss();
        }
    }

    private FeedDialog.OnCenterItemClickListener listener;
    public interface OnCenterItemClickListener {
        void OnCenterItemClick(FeedDialog dialog, View view);
    }
    //很明显我们要在这里面写个接口，然后添加一个方法
    public void setOnCenterItemClickListener(FeedDialog.OnCenterItemClickListener listener) {
        this.listener = listener;
    }
}
