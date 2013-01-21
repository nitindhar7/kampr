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

    private static ListView notificationsList;
    private static ProgressBar spinner;
    private static TextView allTabLabel;
    private static TextView linkTabLabel;
    private static TextView snapTabLabel;
    private static TextView codeTabLabel;
    private static TextView questionTabLabel;
    private static TabHost tabHost;
    private static TabHost.TabSpec spec;
    private static Intent intent;
    private static LayoutInflater inflater;
    private static SlidingDrawer notificationbar;
    private static NotificationsTask notificationsTask;

    private View tab;
    private View tabSelectedDivider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts);

        spinner = (ProgressBar) findViewById(R.id.actionbar_spinner);
        notificationbar = (SlidingDrawer) findViewById(R.id.notificationbar);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tabHost = getTabHost();

        tab = inflater.inflate(R.layout.tab, getTabWidget(), false);
        tab.setBackgroundResource(R.drawable.tab_selected);
        allTabLabel = (TextView) tab.findViewById(R.id.tabLabel);
        allTabLabel.setText("ACTIVITY");
        tabSelectedDivider = tab.findViewById(R.id.tabSelectedDivider);
        tabSelectedDivider.setVisibility(View.VISIBLE);
        SpanUtils.setFont(this, allTabLabel);
        intent = new Intent(PostsActivity.this, PostsListActivity.class);
        intent.putExtra("post_type", "all");
        spec = tabHost.newTabSpec("all").setIndicator(tab).setContent(intent);
        tabHost.addTab(spec);

        tab = inflater.inflate(R.layout.tab, getTabWidget(), false);
        linkTabLabel = (TextView) tab.findViewById(R.id.tabLabel);
        linkTabLabel.setText("LINKS");
        SpanUtils.setFont(this, linkTabLabel);
        intent = new Intent(PostsActivity.this, PostsListActivity.class);
        intent.putExtra("post_type", "link");
        spec = tabHost.newTabSpec("links").setIndicator(tab).setContent(intent);
        tabHost.addTab(spec);

        tab = inflater.inflate(R.layout.tab, getTabWidget(), false);
        snapTabLabel = (TextView) tab.findViewById(R.id.tabLabel);
        snapTabLabel.setText("SNAPS");
        SpanUtils.setFont(this, snapTabLabel);
        intent = new Intent(PostsActivity.this, PostsListActivity.class);
        intent.putExtra("post_type", "snap");
        spec = tabHost.newTabSpec("snaps").setIndicator(tab).setContent(intent);
        tabHost.addTab(spec);

        tab = inflater.inflate(R.layout.tab, getTabWidget(), false);
        codeTabLabel = (TextView) tab.findViewById(R.id.tabLabel);
        codeTabLabel.setText("CODE");
        SpanUtils.setFont(this, codeTabLabel);
        intent = new Intent(PostsActivity.this, PostsListActivity.class);
        intent.putExtra("post_type", "code");
        spec = tabHost.newTabSpec("codes").setIndicator(tab).setContent(intent);
        tabHost.addTab(spec);

        tab = inflater.inflate(R.layout.tab, getTabWidget(), false);
        questionTabLabel = (TextView) tab.findViewById(R.id.tabLabel);
        questionTabLabel.setText("Q & A");
        SpanUtils.setFont(this, questionTabLabel);
        intent = new Intent(PostsActivity.this, PostsListActivity.class);
        intent.putExtra("post_type", "question");
        spec = tabHost.newTabSpec("questions").setIndicator(tab).setContent(intent);
        tabHost.addTab(spec);

        // http://bit.ly/w1QV6k
        tabHost.setOnTabChangedListener(new OnTabChangeListener() {
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
                tabHost.getCurrentTabView().setBackgroundResource(R.drawable.tab_selected);
            }
        });

        // TODO: start notifications tasks from executor. check every 2 minutes or so.

        notificationsList = (ListView) findViewById(R.id.notifications_list);
        notificationsList.setVerticalScrollBarEnabled(false);
        notificationsList.setScrollbarFadingEnabled(false);
        notificationsList.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        notificationsList.setDividerHeight(1);

        notificationsTask = new NotificationsTask(this, notificationsList);
        notificationsTask.execute();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case LOGOUT_RESULT_CODE:
                if(resultCode == LogoutActivity.RESULT_SUCCESS) {
                    Intent kampr = new Intent(PostsActivity.this, KamprActivity.class);
                    startActivity(kampr);
                }
                else if(resultCode == LogoutActivity.RESULT_FAILURE) {
                    Toast.makeText(getApplicationContext() , "Error logging out. Try Again!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void clearTabStyles() {
        for (int i = 0; i < getTabWidget().getChildCount(); i++) {
            tab = getTabWidget().getChildAt(i);
            tab.setBackgroundResource(R.drawable.tab);
            tab.findViewById(R.id.tabSelectedDivider).setVisibility(View.GONE);
        }
    }

    public static ProgressBar getSpinner() {
        return spinner;
    }

    public static SlidingDrawer getNotificationbar() {
        return notificationbar;
    }

}