package com.nitindhar.kampr.posts;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.nitindhar.kampr.R;

public class SnapFullscreenActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap_fullscreen);
        
        WebView webview = (WebView) findViewById(R.id.webview);
        webview.setBackgroundColor(R.color.black);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.setHorizontalScrollBarEnabled(false);
        webview.setVerticalScrollBarEnabled(false);
        webview.loadUrl(getIntent().getStringExtra("snaps_original_url"));
    }

}
