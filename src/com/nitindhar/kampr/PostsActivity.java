package com.nitindhar.kampr;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.nitindhar.kampr.async.NotificationsTask;
import com.nitindhar.kampr.posts.PostsListActivity;
import com.nitindhar.kampr.util.SpanUtils;

public class PostsActivity extends TabActivity {
    
    private static final int LOGOUT_RESULT_CODE = 1;
    
    private static ListView _notificationsList;
    private static ProgressBar _spinner;
    private static TextView _allTabLabel;
    private static TextView _linkTabLabel;
    private static TextView _snapTabLabel;
    private static TextView _codeTabLabel;
    private static TextView _questionTabLabel;
    private static TabHost _tabHost;
    private static TabHost.TabSpec _spec;
    private static Intent _intent;
    private static LayoutInflater _inflater;
    private static SlidingDrawer _notificationbar;
    private static NotificationsTask _notificationsTask;

    private View _tab;
    private View _tabSelectedDivider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);
        
        _spinner = (ProgressBar) findViewById(R.id.actionbar_spinner);
        _notificationbar = (SlidingDrawer) findViewById(R.id.notificationbar);

        _inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _tabHost = getTabHost();

        _tab = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _tab.setBackgroundResource(R.drawable.tab_selected);
        _allTabLabel = (TextView) _tab.findViewById(R.id.tabLabel);
        _allTabLabel.setText("ACTIVITY");
        _tabSelectedDivider = (TextView) _tab.findViewById(R.id.tabSelectedDivider);
        _tabSelectedDivider.setVisibility(View.VISIBLE);
        SpanUtils.setFont(this, _allTabLabel);
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "all");
        _spec = _tabHost.newTabSpec("all").setIndicator(_tab).setContent(_intent);
        _tabHost.addTab(_spec);

        _tab = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _linkTabLabel = (TextView) _tab.findViewById(R.id.tabLabel);
        _linkTabLabel.setText("LINKS");
        SpanUtils.setFont(this, _linkTabLabel);
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "link");
        _spec = _tabHost.newTabSpec("links").setIndicator(_tab).setContent(_intent);
        _tabHost.addTab(_spec);
        
        _tab = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _snapTabLabel = (TextView) _tab.findViewById(R.id.tabLabel);
        _snapTabLabel.setText("SNAPS");
        SpanUtils.setFont(this, _snapTabLabel);
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "snap");
        _spec = _tabHost.newTabSpec("snaps").setIndicator(_tab).setContent(_intent);
        _tabHost.addTab(_spec);

        _tab = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _codeTabLabel = (TextView) _tab.findViewById(R.id.tabLabel);
        _codeTabLabel.setText("CODE");
        SpanUtils.setFont(this, _codeTabLabel);
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "code");
        _spec = _tabHost.newTabSpec("codes").setIndicator(_tab).setContent(_intent);
        _tabHost.addTab(_spec);

        _tab = _inflater.inflate(R.layout.tab, getTabWidget(), false);
        _questionTabLabel = (TextView) _tab.findViewById(R.id.tabLabel);
        _questionTabLabel.setText("Q & A");
        SpanUtils.setFont(this, _questionTabLabel);
        _intent = new Intent(PostsActivity.this, PostsListActivity.class);
        _intent.putExtra("post_type", "question");
        _spec = _tabHost.newTabSpec("questions").setIndicator(_tab).setContent(_intent);
        _tabHost.addTab(_spec);
        
        // http://bit.ly/w1QV6k
        _tabHost.setOnTabChangedListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(String tag) {
                clearTabStyles();
                View tabView = null;
                if (tag.equals("all")) {
                    tabView = getTabWidget().getChildAt(0);
                }
                else if (tag.equals("links")) {
                    tabView = getTabWidget().getChildAt(1);
                }
                else if (tag.equals("snaps")) {
                    tabView = getTabWidget().getChildAt(2);
                }
                else if (tag.equals("codes")) {
                    tabView = getTabWidget().getChildAt(3);
                }
                else if (tag.equals("questions")) {
                    tabView = getTabWidget().getChildAt(4);
                }
                tabView.findViewById(R.id.tabSelectedDivider).setVisibility(View.VISIBLE);
                _tabHost.getCurrentTabView().setBackgroundResource(R.drawable.tab_selected);
            }
        });
        
        // TODO: start notifications tasks from executor. check every 2 minutes or so.

        _notificationsList = (ListView) findViewById(R.id.notifications_list);
        _notificationsList.setVerticalScrollBarEnabled(false);
        _notificationsList.setScrollbarFadingEnabled(false);
        _notificationsList.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        _notificationsList.setDividerHeight(1);

        _notificationsTask = new NotificationsTask(this, _notificationsList);
        _notificationsTask.execute();
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
        for (int i = 0; i < getTabWidget().getChildCount(); i++) {
            _tab = getTabWidget().getChildAt(i);
            _tab.setBackgroundResource(R.drawable.tab);
            _tab.findViewById(R.id.tabSelectedDivider).setVisibility(View.GONE);
        }
    }
    
    public static ProgressBar getSpinner() {
        return _spinner;
    }
    
    public static SlidingDrawer getNotificationbar() {
        return _notificationbar;
    }

}