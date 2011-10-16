package com.kampr;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;

import com.kampr.tabs.CodesActivity;
import com.kampr.tabs.LinksActivity;
import com.kampr.tabs.QuestionsActivity;
import com.kampr.tabs.SnapsActivity;

public class PostsActivity extends TabActivity {

    private final String ACTIVITY_TAG = "PostsActivity";
    private final int LOGOUT_RESULT_CODE = 1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.posts);
        
        // Tab styling: http://stackoverflow.com/questions/3538946/create-smaller-tabs-in-android
        Resources res = getResources();
        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        intent = new Intent(PostsActivity.this, LinksActivity.class);
        spec = tabHost.newTabSpec("links").setIndicator("Links", res.getDrawable(R.drawable.tab_size_mdpi_links)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(PostsActivity.this, SnapsActivity.class);
        spec = tabHost.newTabSpec("snaps").setIndicator("Snaps", res.getDrawable(R.drawable.tab_size_mdpi_snaps)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(PostsActivity.this, CodesActivity.class);
        spec = tabHost.newTabSpec("codes").setIndicator("Codes", res.getDrawable(R.drawable.tab_size_mdpi_codes)).setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent(PostsActivity.this, QuestionsActivity.class);
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(ACTIVITY_TAG, "onCreateOptionsMenu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.posts_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.posts_menu_logout:
                Intent addProfile = new Intent(PostsActivity.this, LogoutActivity.class);
                startActivityForResult(addProfile, LOGOUT_RESULT_CODE);
                break;
        }
        return true;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case LOGOUT_RESULT_CODE:
                if(resultCode == LogoutActivity.RESULT_SUCCESS) {
                    Intent kampr = new Intent(PostsActivity.this, KamprActivity.class);
                    startActivity(kampr);
                }
                else if(resultCode == LogoutActivity.RESULT_FAILURE)
                    Toast.makeText(getApplicationContext() , "Error logging out. Try Again!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}