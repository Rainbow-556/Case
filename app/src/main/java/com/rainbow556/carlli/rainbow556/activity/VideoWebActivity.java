package com.rainbow556.carlli.rainbow556.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.rainbow556.carlli.rainbow556.R;
import com.rainbow556.carlli.rainbow556.util.JLog;

/**
 * webview播放视频全屏
 */
public class VideoWebActivity extends AppCompatActivity{

    private WebView mWebView;
    private WebChromeClient webChromeClient;
    private RelativeLayout rlFull;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_web);
        JLog.e("onCreate");
        mWebView = (WebView)findViewById(R.id.webView);
        rlFull = (RelativeLayout) findViewById(R.id.videoLayout);
        initWebView();
//        mWebView.loadUrl("http://10.3.20.38/activity/other/tg2");
//        mWebView.loadUrl("http://10.3.20.38/fortune/video/74");
        mWebView.loadUrl("http://m.jfz.com/vip/wm-live-91.html");
//        mWebView.loadUrl("http://www.baidu.com");

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        JLog.e("onDestroy");
    }

    private void initWebView(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback){
                super.onShowCustomView(view, callback);
                JLog.e("onShowCustomView");
                rlFull.addView(view);
                rlFull.setVisibility(View.VISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            @Override
            public void onHideCustomView(){
                super.onHideCustomView();
                JLog.e("onHideCustomView");
                rlFull.removeAllViews();
                rlFull.setVisibility(View.INVISIBLE);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(mWebView.canGoBack()){
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }
}
