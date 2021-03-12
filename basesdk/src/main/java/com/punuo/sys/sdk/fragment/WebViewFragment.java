package com.punuo.sys.sdk.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;
import com.punuo.sys.sdk.R;
import com.punuo.sys.sdk.activity.WebViewActivity;
import com.punuo.sys.sdk.util.BaseHandler;
import com.punuo.sys.sdk.util.StatusBarUtil;
import com.punuo.sys.sdk.util.ToastUtils;

import java.util.HashMap;

/**
 * Created by han.chen.
 * Date on 2019/5/29.
 **/
public class WebViewFragment extends BaseFragment {
    private WebViewActivity mActivity;
    private PullToRefreshWebView mPullToRefreshWebView;
    private WebView mWebView;
    private View mStatusBar;
    private boolean isRefreshing;
    private BaseHandler mBaseHandler;
    private String mUrl = "";
    private String mTitle = "";
    private HashMap<String,String> webViewHead =new HashMap<>();
    private String referer = "qinqingonline.com";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = (WebViewActivity) getActivity();
        mBaseHandler = mActivity.getBaseHandler();
        mFragmentView = inflater.inflate(R.layout.webview_fragment, container, false);
        View backIcon = mFragmentView.findViewById(R.id.back);
        backIcon.setOnClickListener(v -> mActivity.finish());
        TextView title = mFragmentView.findViewById(R.id.title);
        mPullToRefreshWebView = mFragmentView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mStatusBar = mFragmentView.findViewById(R.id.status_bar);
        mWebView = mPullToRefreshWebView.getRefreshableView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBar.setVisibility(View.VISIBLE);
            mStatusBar.getLayoutParams().height = StatusBarUtil.getStatusBarHeight(mActivity);
            mStatusBar.requestLayout();
        }
        mTitle = getArguments().getString("title", "");
        title.setText(mTitle);
        mUrl = getArguments().getString("url", "");
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setAllowFileAccess(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setTextZoom(100);
        settings.setJavaScriptEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        mWebView.setWebViewClient(new MyWebViewClient());
        mPullToRefreshWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>() {

            @Override
            public void onRefresh(PullToRefreshBase<WebView> refreshView) {
                loadUrl();
            }
        });
        loadUrl();
        return mFragmentView;
    }

    public String getUrl() {
        return mUrl;
    }

    public void onRefreshComplete() {
        if (mPullToRefreshWebView != null) {
            mPullToRefreshWebView.onRefreshComplete();
        }
    }

    public void handleMessage(Message msg) {
        if (msg.what == 1200) {
            if (isRefreshing) {
                isRefreshing = false;
                onRefreshComplete();
            }
        }
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("wx.tenpay.com/cgi-bin")) {
                webViewHead.put("Referer","http://pet.qinqingonline.com:8001/");
                view.loadUrl(url, webViewHead);
                return true;
            }
            if (url.startsWith("weixin://wap/pay?")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showToast("未安装微信");
                }
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            WebViewFragment.this.onPageStarted(view, url, favicon);
            isRefreshing = true;
            //只为兼容某手机 onPageFinished()回调失效
            Message msg = Message.obtain();
            msg.what = 1200;
            mBaseHandler.sendMessageDelayed(msg, 3000);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WebViewFragment.this.onPageFinished(view, url);
            isRefreshing = false;
        }
    }

    public void onPageStarted(WebView webView, String url, Bitmap favicon) {

    }

    protected void onPageFinished(WebView view, String url) {
        if (mPullToRefreshWebView != null) {
            mPullToRefreshWebView.onRefreshComplete();
        }
    }

    public void loadUrl() {
        String url = getUrl();
        if (url != null) {
            mWebView.loadUrl(url, webViewHead);
        }
    }

    public void removeMessage() {
        mBaseHandler.removeMessages(1200);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeMessage();
    }
}
