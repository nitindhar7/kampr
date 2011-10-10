package com.kampr;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TabHost;

import com.kampr.tabs.LinksActivity;

public class PostsActivity extends TabActivity {

    private final String ACTIVITY_TAG = "PostsActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.my_posts);
        
        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent().setClass(this, LinksActivity.class);
        spec = tabHost.newTabSpec("links").setIndicator("Links", res.getDrawable(R.drawable.tab_size_mdpi_links)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, LinksActivity.class);
        spec = tabHost.newTabSpec("snaps").setIndicator("Snaps", res.getDrawable(R.drawable.tab_size_mdpi_snaps)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, LinksActivity.class);
        spec = tabHost.newTabSpec("codes").setIndicator("Codes", res.getDrawable(R.drawable.tab_size_mdpi_codes)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, LinksActivity.class);
        spec = tabHost.newTabSpec("questions").setIndicator("Questions", res.getDrawable(R.drawable.tab_size_mdpi_questions)).setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_TAG, "onStart");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_TAG, "onPause");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_TAG, "onStop");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_TAG, "onDestroy");
    }

}