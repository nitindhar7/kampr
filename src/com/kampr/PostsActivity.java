package com.kampr;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        setContentView(R.layout.posts);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 0, 5, 0);
        
        LinearLayout linkTabLayout = new LinearLayout(this);
        linkTabLayout.setBackgroundResource(R.drawable.tab);
        linkTabLayout.setLayoutParams(params);
        linkTabLayout.setPadding(30, 5, 30, 5);
        
        LinearLayout snapTabLayout = new LinearLayout(this);
        snapTabLayout.setBackgroundResource(R.drawable.tab);
        snapTabLayout.setLayoutParams(params);
        snapTabLayout.setPadding(30, 5, 30, 5);
        
        LinearLayout codeTabLayout = new LinearLayout(this);
        codeTabLayout.setBackgroundResource(R.drawable.tab);
        codeTabLayout.setLayoutParams(params);
        codeTabLayout.setPadding(30, 5, 30, 5);
        
        LinearLayout questionTabLayout = new LinearLayout(this);
        questionTabLayout.setBackgroundResource(R.drawable.tab);
        questionTabLayout.setLayoutParams(params);
        questionTabLayout.setPadding(30, 5, 30, 5);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        ImageView linkTab = new ImageView(this);
        linkTab.setImageResource(R.drawable.ic_tab_link_nouveau);
        linkTabLayout.addView(linkTab);
        intent = new Intent(PostsActivity.this, LinksActivity.class);
        spec = tabHost.newTabSpec("links").setIndicator(linkTabLayout).setContent(intent);
        tabHost.addTab(spec);
        
        ImageView snapTab = new ImageView(this);
        snapTab.setImageResource(R.drawable.ic_tab_snap_nouveau);
        snapTabLayout.addView(snapTab);
        intent = new Intent(PostsActivity.this, SnapsActivity.class);
        spec = tabHost.newTabSpec("snaps").setIndicator(snapTabLayout).setContent(intent);
        tabHost.addTab(spec);
        
        ImageView codeTab = new ImageView(this);
        codeTab.setImageResource(R.drawable.ic_tab_code_nouveau);
        codeTabLayout.addView(codeTab);
        intent = new Intent(PostsActivity.this, CodesActivity.class);
        spec = tabHost.newTabSpec("code").setIndicator(codeTabLayout).setContent(intent);
        tabHost.addTab(spec);
        
        ImageView questionTab = new ImageView(this);
        questionTab.setImageResource(R.drawable.ic_tab_question_nouveau);
        questionTabLayout.addView(questionTab);
        intent = new Intent(PostsActivity.this, QuestionsActivity.class);
        spec = tabHost.newTabSpec("question").setIndicator(questionTabLayout).setContent(intent);
        tabHost.addTab(spec);
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
                Intent logout = new Intent(PostsActivity.this, LogoutActivity.class);
                startActivityForResult(logout, LOGOUT_RESULT_CODE);
                break;
            case R.id.posts_menu_settings:
                Intent settings = new Intent(PostsActivity.this, SettingsActivity.class);
                startActivity(settings);
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