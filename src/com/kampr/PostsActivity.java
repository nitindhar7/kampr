package com.kampr;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class PostsActivity extends Activity {

    private final String ACTIVITY_TAG = "PostsActivity";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        setContentView(R.layout.posts);
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