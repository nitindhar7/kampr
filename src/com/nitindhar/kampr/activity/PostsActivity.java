package com.nitindhar.kampr.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.nitindhar.kampr.R;
import com.nitindhar.kampr.async.LogoutTask;
import com.nitindhar.kampr.async.PostsTask;

public class PostsActivity extends ListActivity implements OnMenuItemClickListener {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static PostsTask postsTask;
    private static LayoutInflater layoutInflater;
    private static View spinner;

    private ListView posts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setLogo(this.getResources().getDrawable(R.drawable.ic_action_kampr));
        actionBar.setBackgroundDrawable(this.getResources().getDrawable(R.color.kampr_green));
        actionBar.setHomeButtonEnabled(true);

        layoutInflater = getLayoutInflater();
        spinner = layoutInflater.inflate(R.layout.actionbar_spinner, null);

        setContentView(R.layout.posts);

        posts = getListView();
        posts.setVerticalScrollBarEnabled(false);
        posts.setVerticalFadingEdgeEnabled(false);
        posts.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        posts.setDividerHeight(1);

        postsTask = new PostsTask(this, posts);
        postsTask.execute("all");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        MenuItem actionbarRefresh = menu.findItem(R.id.actionbar_refresh);
        actionbarRefresh.setActionView(spinner);
        postsTask.setActionbarRefresh(actionbarRefresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PopupMenu popup = new PopupMenu(this, findViewById(item.getItemId()));
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);

        switch (item.getItemId()) {
        case R.id.actionbar_settings:
            inflater.inflate(R.menu.actionbar_setting_items, popup.getMenu());
            break;
        case R.id.actionbar_refresh:
            item.setActionView(spinner);
            postsTask = new PostsTask(this, posts);
            postsTask.execute("all");
            postsTask.setActionbarRefresh(item);
            break;
        default:
            return super.onOptionsItemSelected(item);
        }

        popup.show();
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        postsTask = new PostsTask(this, posts);

        switch (item.getItemId()) {
        case R.id.settings_menu_logout:
            executor.submit(new LogoutTask());
            Intent kampr = new Intent(PostsActivity.this, KamprActivity.class);
            startActivity(kampr);
            return true;
        default:
            return false;
        }
    }

}