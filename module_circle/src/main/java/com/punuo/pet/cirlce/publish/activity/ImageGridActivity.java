package com.punuo.pet.cirlce.publish.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.punuo.pet.cirlce.R;
import com.punuo.pet.cirlce.publish.adapter.ImageGridAdapter;
import com.punuo.pet.cirlce.publish.event.ChooseImageResultEvent;
import com.punuo.pet.cirlce.publish.helpclass.AlbumHelper;
import com.punuo.pet.cirlce.publish.helpclass.ImageItem;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Kuiya on 2019/8/9.
 */

public class ImageGridActivity extends BaseSwipeBackActivity {

    // ArrayList<Entity> dataList;//用来装载数据源的列表
    private ArrayList<ImageItem> dataList;
    private ImageGridAdapter mImageGridAdapter;
    private Button mOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);
        StatusBarUtil.translucentStatusBar(this, Color.TRANSPARENT, false);
        View statusBar = findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);
            statusBar.requestLayout();
        }

        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        dataList = getIntent().getParcelableArrayListExtra(ChoosePictureActivity.EXTRA_IMAGE_LIST);

        initView();
        TextView cancel = (TextView) findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mOK = (Button) findViewById(R.id.bt);
        mOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(new ChooseImageResultEvent(mImageGridAdapter.mList));
                finish();
            }

        });
    }

    /**
     * 初始化view视图
     */
    private void initView() {
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mImageGridAdapter = new ImageGridAdapter(this, dataList);
        gridView.setAdapter(mImageGridAdapter);
        mImageGridAdapter.setTextCallback(new ImageGridAdapter.TextCallback() {
            public void onListen(int count) {
                mOK.setText("完成" + "(" + count + ")");
            }
        });
    }
}
