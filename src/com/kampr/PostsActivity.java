package com.kampr;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.kampr.posts.PostsListActivity;

public class PostsActivity extends TabActivity {
    
    protected static ProgressBar _spinner;
    
    private final int LOGOUT_RESULT_CODE = 1;

    private static TextView _allTabLabel;
    private static TextView _linkTabLabel;
    private static TextView _snapTabLabel;
    private static TextView _codeTabLabel;
    private static TextView _questionTabLabel;
    private static ImageView _allTabIcon;
    private static ImageView _linkTabIcon;
    private static ImageView _snapTabIcon;
    private static ImageView _codeTabIcon;
    private static ImageView _questionTabIcon;
    private static TabHost _tabHost;
    private static TabHost.TabSpec _spec;
    private static Intent _intent;
    private static LayoutInflater _inflater;

    private View _view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);
        
        _spinner = (ProgressBar) findViewById(R.id.actionbar_spinner);

        _inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _tabHost = getTabHost();

        _view = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _view.setBackgroundResource(R.drawable.tab_selected);
        _allTabIcon = (ImageView) _view.findViewById(R.id.tabImage);
        _allTabIcon.setImageResource(R.drawable.tab_activity_selected);
        _allTabLabel = (TextView) _view.findViewById(R.id.tabLabel);
        _allTabLabel.setText("Activity");
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "all");
        _spec = _tabHost.newTabSpec("all").setIndicator(_view).setContent(_intent);
        _tabHost.addTab(_spec);

        _view = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _linkTabIcon = (ImageView) _view.findViewById(R.id.tabImage);
        _linkTabIcon.setImageResource(R.drawable.tab_links_unselected);
        _linkTabLabel = (TextView) _view.findViewById(R.id.tabLabel);
        _linkTabLabel.setText("Links");
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "link");
        _spec = _tabHost.newTabSpec("links").setIndicator(_view).setContent(_intent);
        _tabHost.addTab(_spec);
        
        _view = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _snapTabIcon = (ImageView) _view.findViewById(R.id.tabImage);
        _snapTabIcon.setImageResource(R.drawable.tab_snaps_unselected);
        _snapTabLabel = (TextView) _view.findViewById(R.id.tabLabel);
        _snapTabLabel.setText("Snaps");
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "snap");
        _spec = _tabHost.newTabSpec("snaps").setIndicator(_view).setContent(_intent);
        _tabHost.addTab(_spec);

        _view = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _codeTabIcon = (ImageView) _view.findViewById(R.id.tabImage);
        _codeTabIcon.setImageResource(R.drawable.tab_code_unselected);
        _codeTabLabel = (TextView) _view.findViewById(R.id.tabLabel);
        _codeTabLabel.setText("Code");
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "code");
        _spec = _tabHost.newTabSpec("codes").setIndicator(_view).setContent(_intent);
        _tabHost.addTab(_spec);

        _view = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _questionTabIcon = (ImageView) _view.findViewById(R.id.tabImage);
        _questionTabIcon.setImageResource(R.drawable.tab_question_unselected);
        _questionTabLabel = (TextView) _view.findViewById(R.id.tabLabel);
        _questionTabLabel.setText("Questions");
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "question");
        _spec = _tabHost.newTabSpec("questions").setIndicator(_view).setContent(_intent);
        _tabHost.addTab(_spec);
        
        // http://bit.ly/w1QV6k
        _tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tag) {
                clearTabStyles();
                if (tag.equals("all")) {
                    _allTabIcon.setImageResource(R.drawable.tab_activity_selected);
                }
                else if (tag.equals("links")) {
                    _linkTabIcon.setImageResource(R.drawable.tab_links_selected);
                }
                else if (tag.equals("snaps")) {
                    _snapTabIcon.setImageResource(R.drawable.tab_snaps_selected);
                }
                else if (tag.equals("codes")) {
                    _codeTabIcon.setImageResource(R.drawable.tab_code_selected);
                }
                else if (tag.equals("questions")) {
                    _questionTabIcon.setImageResource(R.drawable.tab_question_selected);
                }
                _tabHost.getCurrentTabView().setBackgroundResource(R.drawable.tab_selected);
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
    
    @Override
    public void onBackPressed() {
        finish();
    }
    
    private void clearTabStyles() {
        _allTabIcon.setImageResource(R.drawable.tab_activity_unselected);
        _linkTabIcon.setImageResource(R.drawable.tab_links_unselected);
        _snapTabIcon.setImageResource(R.drawable.tab_snaps_unselected);
        _codeTabIcon.setImageResource(R.drawable.tab_code_unselected);
        _questionTabIcon.setImageResource(R.drawable.tab_question_unselected);
        for (int i = 0; i < getTabWidget().getChildCount(); i++) {
            _view = getTabWidget().getChildAt(i);
            _view.setBackgroundResource(R.drawable.tab);
        }
    }
    
    public static ProgressBar getSpinner() {
        return _spinner;
    }

}