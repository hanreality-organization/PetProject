package com.punuo.pet.feed.feednow;

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
import com.punuo.sip.SipUserManager;
import com.punuo.sys.sdk.account.AccountManager;

public class FeedDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private int layoutResID;
    private TextView mCountText;
    private TextView mSubCountText;
    private TextView mAddCountText;
    private Button mComplete;
    public static int defaultCount = 3;

    public FeedDialog(Context context, int layoutResID) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.layoutResID = layoutResID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        setContentView(layoutResID);
        initDialog();
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 4 / 5;
        getWindow().setAttributes(lp);
        setCanceledOnTouchOutside(true);

    }

    public void initDialog() {
        mCountText = findViewById(R.id.count);
        mSubCountText = findViewById(R.id.sub_count);
        mAddCountText = findViewById(R.id.add_count);
        mComplete = findViewById(R.id.complete);
        mSubCountText.setOnClickListener(this);
        mAddCountText.setOnClickListener(this);
        mComplete.setOnClickListener(this);
        String count = defaultCount + context.getString(R.string.string_feed_copies);
        mCountText.setText(count);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.sub_count) {
            if (defaultCount > 0) {
                mCountText.setText((defaultCount -= 1) + context.getString(R.string.string_feed_copies));
            } else {
                return;
            }
        } else if (id == R.id.add_count) {
            mCountText.setText((defaultCount += 1) + context.getString(R.string.string_feed_copies));
        } else if (id == R.id.complete) {
            if (defaultCount == 0) {
                return;
            } else {
                String feedCount = mCountText.getText().toString().trim();//获取到相应的份数
                outGrain(feedCount);
                dismiss();
            }
        }
    }

    public void outGrain(String feedcount) {//出粮
        FeedNowSipRequest mFeedNowSipRequest = new FeedNowSipRequest(AccountManager.getUserName(), feedcount);
        SipUserManager.getInstance().addRequest(mFeedNowSipRequest);
    }

}

