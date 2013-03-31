package com.nitindhar.kampr.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import com.nitindhar.kampr.R;
import com.nitindhar.kampr.async.LogoutTask;
import com.nitindhar.kampr.async.PostsTask;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;

public class PostsActivity extends ListActivity implements OnMenuItemClickListener, OnItemClickListener {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static PostsTask postsTask;

    private ListView posts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        setContentView(R.layout.posts);

        posts = getListView();
        posts.setVerticalScrollBarEnabled(false);
        posts.setVerticalFadingEdgeEnabled(false);
        posts.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        posts.setDividerHeight(1);
        posts.setOnItemClickListener(this);
        registerForContextMenu(posts);

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
        inflater.inflate(R.menu.actionbar_posts_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PopupMenu popup = new PopupMenu(this, findViewById(item.getItemId()));
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);

        switch (item.getItemId()) {
        case R.id.actionbar_settings:
            inflater.inflate(R.menu.actionbar_settings_menu, popup.getMenu());
            break;
        case R.id.actionbar_post_types:
            inflater.inflate(R.menu.actionbar_post_types_menu, popup.getMenu());
            break;
        case R.id.actionbar_refresh:
            postsTask = new PostsTask(this, posts);
            postsTask.execute("all");
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
        case R.id.post_types_menu_all:
            postsTask.execute("all");
            return true;
        case R.id.post_types_menu_links:
            postsTask.execute("link");
            return true;
        case R.id.post_types_menu_snaps:
            postsTask.execute("snap");
            return true;
        case R.id.post_types_menu_code:
            postsTask.execute("code");
            return true;
        case R.id.post_types_menu_questions:
            postsTask.execute("question");
            return true;
        default:
            return false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent postIntent = new Intent(getApplicationContext(), PostActivity.class);
        PostDecorator pd = postsTask.getAdapter().getViewObject(position);
        postIntent.putExtra("post", pd.getPost());
        postIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
        startActivity(postIntent);
    }

}