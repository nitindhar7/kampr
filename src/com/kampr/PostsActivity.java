package com.kampr;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kampr.tabs.AllActivity;
import com.kampr.tabs.CodesActivity;
import com.kampr.tabs.LinksActivity;
import com.kampr.tabs.QuestionsActivity;
import com.kampr.tabs.SnapsActivity;
import com.kampr.util.LayoutUtils;

public class PostsActivity extends TabActivity {

    private static final LinearLayout.LayoutParams _params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    
    private final int LOGOUT_RESULT_CODE = 1;
    
    private static TextView _actionbarLogo;
    
    private static LinearLayout _allTabLayout;
    private static LinearLayout _linkTabLayout;
    private static LinearLayout _snapTabLayout;
    private static LinearLayout _codeTabLayout;
    private static LinearLayout _questionTabLayout;
    
    private static ImageView _allTab;
    private static ImageView _linkTab;
    private static ImageView _snapTab;
    private static ImageView _codeTab;
    private static ImageView _questionTab;
    
    private static TabHost _tabHost;
    private static TabHost.TabSpec _spec;
    private static Intent _intent;
    
    public PostsActivity() {
        _params.setMargins(4, 0, 4, 0);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        _tabHost = getTabHost();
        
        _actionbarLogo = (TextView) findViewById(R.id.actionbar_logo);
        LayoutUtils.setFont(this, _actionbarLogo, LayoutUtils.FONT_BOLD);

        _allTabLayout = new LinearLayout(this);
        _allTabLayout.setBackgroundResource(R.drawable.tab_selected);
        _allTabLayout.setLayoutParams(_params);
        _allTabLayout.setPadding(20, 0, 20, 0);
        
        _linkTabLayout = new LinearLayout(this);
        _linkTabLayout.setBackgroundResource(R.drawable.tab);
        _linkTabLayout.setLayoutParams(_params);
        _linkTabLayout.setPadding(20, 0, 20, 0);
        
        _snapTabLayout = new LinearLayout(this);
        _snapTabLayout.setBackgroundResource(R.drawable.tab);
        _snapTabLayout.setLayoutParams(_params);
        _snapTabLayout.setPadding(20, 0, 20, 0);
        
        _codeTabLayout = new LinearLayout(this);
        _codeTabLayout.setBackgroundResource(R.drawable.tab);
        _codeTabLayout.setLayoutParams(_params);
        _codeTabLayout.setPadding(20, 0, 20, 0);
        
        _questionTabLayout = new LinearLayout(this);
        _questionTabLayout.setBackgroundResource(R.drawable.tab);
        _questionTabLayout.setLayoutParams(_params);
        _questionTabLayout.setPadding(20, 0, 20, 0);
        
        _allTab = new ImageView(this);
        _allTab.setImageResource(R.drawable.ic_tab_code_selected);
        _allTabLayout.addView(_allTab);
        _intent = new Intent(PostsActivity.this, AllActivity.class);
        _spec = _tabHost.newTabSpec("all").setIndicator(_allTabLayout).setContent(_intent);
        _tabHost.addTab(_spec);

        _linkTab = new ImageView(this);
        _linkTab.setImageResource(R.drawable.ic_tab_link_nouveau);
        _linkTabLayout.addView(_linkTab);
        _intent = new Intent(PostsActivity.this, LinksActivity.class);
        _spec = _tabHost.newTabSpec("links").setIndicator(_linkTabLayout).setContent(_intent);
        _tabHost.addTab(_spec);
        
        _snapTab = new ImageView(this);
        _snapTab.setImageResource(R.drawable.ic_tab_snap_nouveau);
        _snapTabLayout.addView(_snapTab);
        _intent = new Intent(PostsActivity.this, SnapsActivity.class);
        _spec = _tabHost.newTabSpec("snaps").setIndicator(_snapTabLayout).setContent(_intent);
        _tabHost.addTab(_spec);
        
        _codeTab = new ImageView(this);
        _codeTab.setImageResource(R.drawable.ic_tab_code_nouveau);
        _codeTabLayout.addView(_codeTab);
        _intent = new Intent(PostsActivity.this, CodesActivity.class);
        _spec = _tabHost.newTabSpec("codes").setIndicator(_codeTabLayout).setContent(_intent);
        _tabHost.addTab(_spec);
        
        _questionTab = new ImageView(this);
        _questionTab.setImageResource(R.drawable.ic_tab_question_nouveau);
        _questionTabLayout.addView(_questionTab);
        _intent = new Intent(PostsActivity.this, QuestionsActivity.class);
        _spec = _tabHost.newTabSpec("questions").setIndicator(_questionTabLayout).setContent(_intent);
        _tabHost.addTab(_spec);
        
        // http://bit.ly/w1QV6k
        _tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tag) {
                clearTabStyles();
                if (tag.equals("all")) {
                    _allTab.setImageResource(R.drawable.ic_tab_code_selected);
                    _allTabLayout.setBackgroundResource(R.drawable.tab_selected);
                    _allTabLayout.setLayoutParams(_params);
                    _allTabLayout.setPadding(20, 0, 20, 0);
                    _actionbarLogo.setText("Posts > All");
                }
                else if (tag.equals("links")) {
                    _linkTab.setImageResource(R.drawable.ic_tab_link_nouveau_selected);
                    _linkTabLayout.setBackgroundResource(R.drawable.tab_selected);
                    _linkTabLayout.setLayoutParams(_params);
                    _linkTabLayout.setPadding(20, 0, 20, 0);
                    _actionbarLogo.setText("Posts > Links");
                }
                else if (tag.equals("snaps")) {
                    _snapTab.setImageResource(R.drawable.ic_tab_snap_nouveau_selected);
                    _snapTabLayout.setBackgroundResource(R.drawable.tab_selected);
                    _snapTabLayout.setLayoutParams(_params);
                    _snapTabLayout.setPadding(20, 0, 20, 0);
                    _actionbarLogo.setText("Posts > Snaps");
                }
                else if (tag.equals("codes")) {
                    _codeTab.setImageResource(R.drawable.ic_tab_code_nouveau_selected);
                    _codeTabLayout.setBackgroundResource(R.drawable.tab_selected);
                    _codeTabLayout.setLayoutParams(_params);
                    _codeTabLayout.setPadding(20, 0, 20, 0);
                    _actionbarLogo.setText("Posts > Codes");
                }
                else if (tag.equals("questions")) {
                    _questionTab.setImageResource(R.drawable.ic_tab_question_nouveau_selected);
                    _questionTabLayout.setBackgroundResource(R.drawable.tab_selected);
                    _questionTabLayout.setLayoutParams(_params);
                    _questionTabLayout.setPadding(20, 0, 20, 0);
                    _actionbarLogo.setText("Posts > Questions");
                }
            }       
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    
    private void clearTabStyles() {
        _allTab.setImageResource(R.drawable.ic_tab_code);
        _allTabLayout.setBackgroundResource(R.drawable.tab);
        _allTabLayout.setLayoutParams(_params);
        _allTabLayout.setPadding(20, 0, 20, 0);
        _linkTab.setImageResource(R.drawable.ic_tab_link_nouveau);
        _linkTabLayout.setBackgroundResource(R.drawable.tab);
        _linkTabLayout.setLayoutParams(_params);
        _linkTabLayout.setPadding(20, 0, 20, 0);
        _snapTab.setImageResource(R.drawable.ic_tab_snap_nouveau);
        _snapTabLayout.setBackgroundResource(R.drawable.tab);
        _snapTabLayout.setLayoutParams(_params);
        _snapTabLayout.setPadding(20, 0, 20, 0);
        _codeTab.setImageResource(R.drawable.ic_tab_code_nouveau);
        _codeTabLayout.setBackgroundResource(R.drawable.tab);
        _codeTabLayout.setLayoutParams(_params);
        _codeTabLayout.setPadding(20, 0, 20, 0);
        _questionTab.setImageResource(R.drawable.ic_tab_question_nouveau);
        _questionTabLayout.setBackgroundResource(R.drawable.tab);
        _questionTabLayout.setLayoutParams(_params);
        _questionTabLayout.setPadding(20, 0, 20, 0);
    }

}