package com.punuo.pet.cirlce.publish.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.punuo.pet.cirlce.R;
import com.punuo.pet.cirlce.R2;
import com.punuo.pet.cirlce.publish.adapter.GridImageAdapter;
import com.punuo.pet.cirlce.publish.event.ChooseImageResultEvent;
import com.punuo.pet.cirlce.publish.event.EditImageEvent;
import com.punuo.pet.cirlce.publish.helpclass.FriendReLoadEvent;
import com.punuo.pet.cirlce.publish.helpclass.ICallBack;
import com.punuo.pet.cirlce.publish.request.UploadPostRequest;
import com.punuo.pet.cirlce.publish.util.CommonUtil;
import com.punuo.pet.cirlce.publish.util.FileUtils;
import com.punuo.pet.router.CircleRouter;
import com.punuo.sys.sdk.account.AccountManager;
import com.punuo.sys.sdk.activity.BaseSwipeBackActivity;
import com.punuo.sys.sdk.httplib.HttpManager;
import com.punuo.sys.sdk.httplib.RequestListener;
import com.punuo.sys.sdk.model.BaseModel;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kuiya on 2019/8/8.
 */

@Route(path = CircleRouter.ROUTER_PUBLISH_ACTIVITY)
public class PublishActivity extends BaseSwipeBackActivity {


    @BindView(R2.id.edit_input)
    EditText mEditText;
    @BindView(R2.id.grid_view)
    RecyclerView mGridView;
    private GridImageAdapter mGridImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        init();

        StatusBarUtil.translucentStatusBar(this, Color.TRANSPARENT, false);
        View statusBar = findViewById(R.id.status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBar.setVisibility(View.VISIBLE);
            statusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(this);
            statusBar.requestLayout();
        }
    }

    public void init() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mGridView.setLayoutManager(gridLayoutManager);
        mGridImageAdapter = new GridImageAdapter(this, new ArrayList<String>(), new ICallBack() {
            @Override
            public void itemClick(String path, int position) {
                CommonUtil.hideKeyboard(PublishActivity.this);
                if (path.equals("add")) {
                    new PopupWindows(PublishActivity.this, mGridView);
                } else {
                    Intent intent = new Intent(PublishActivity.this, PhotoActivity.class);
                    intent.putExtra(PhotoActivity.EXTRA_INDEX, position);
                    intent.putStringArrayListExtra("photos", (ArrayList<String>) mGridImageAdapter.getData());
                    startActivity(intent);
                }
            }
        });
        mGridView.setAdapter(mGridImageAdapter);
        mGridImageAdapter.resetData(new ArrayList<String>());
        TextView publish = (TextView) findViewById(R.id.activity_selectimg_send);
        publish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String dongTai = mEditText.getText().toString();
                uploadPost(dongTai, compressBitmap(mGridImageAdapter.getData()));
            }
        });

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private List<String> compressBitmap(List<String> selectBitmap) {
        showLoadingDialog("正在片压缩图...");
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < selectBitmap.size(); i++) {
            Bitmap bitmap = FileUtils.compressBitmap(selectBitmap.get(i));
            String temp = FileUtils.saveBitmap(bitmap, String.valueOf(System.currentTimeMillis()));
            tempList.add(temp);
        }
        return tempList;
    }

    private UploadPostRequest mUploadPostRequest;

    public void uploadPost(String content, List<String> list) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showToast("发送的内容不能为空");
            return;
        }
        if (mUploadPostRequest != null && !mUploadPostRequest.isFinish()) {
            return;
        }
        showLoadingDialog("正在上传...");
        mUploadPostRequest = new UploadPostRequest();
        mUploadPostRequest.addEntityParam("username", AccountManager.getUserInfo().userName);
        mUploadPostRequest.addEntityParam("content", content);
        List<File> files = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String filePath = list.get(i);
                File file = new File(filePath);
                if (file.exists()) {
                    files.add(file);
                }
            }
        }
        if (!files.isEmpty()) {
            mUploadPostRequest.addEntityParam("file[]", files);
        }
        mUploadPostRequest.setRequestListener(new RequestListener<BaseModel>() {
            @Override
            public void onComplete() {
                dismissLoadingDialog();
                FileUtils.deleteCircleDir();
            }

            @Override
            public void onSuccess(BaseModel result) {
                if (result.success) {
                    ToastUtils.showToast("状态上传成功");
                    EventBus.getDefault().post(new FriendReLoadEvent());
                    finish();
                } else {
                    ToastUtils.showToast("状态上传失败请重试");
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
        HttpManager.addRequest(mUploadPostRequest);
    }

    public String getString(String s) {
        String path = null;
        if (s == null)
            return "";
        for (int i = s.length() - 1; i > 0; i++) {
            s.charAt(i);
        }
        return path;
    }

    public class PopupWindows extends PopupWindow {
        @BindView(R2.id.item_popupwindows_camera)
        Button bt1;
        @BindView(R2.id.item_popupwindows_Photo)
        Button bt2;
        @BindView(R2.id.item_popupwindows_cancel)
        Button bt3;

        public PopupWindows(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
            LinearLayout popLayout = view.findViewById(R.id.ll_popup);
            popLayout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_button_in_2));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            //拍照
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(PublishActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PublishActivity.this, new String[]
                                {Manifest.permission.CAMERA}, 1001);
                    } else {
                        photo();
                    }
                    dismiss();
                }
            });
            //从相册中选取
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(PublishActivity.this, ChoosePictureActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void photo() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        path = Environment.getExternalStorageDirectory() + "/fanxin/Files/Camera/Image/" + System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        Uri uri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    mGridImageAdapter.addData(path);
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ChooseImageResultEvent event) {
        List<String> images = event.mImages;
        mGridImageAdapter.resetData(images);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EditImageEvent event) {
        List<String> images = event.mImages;
        mGridImageAdapter.resetData(images);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}
