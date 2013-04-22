package com.nitindhar.kampr.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.nitindhar.forrst.model.User;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.async.LogoutTask;
import com.nitindhar.kampr.async.UserPostsTask;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;
import com.nitindhar.kampr.util.TextUtils;

public class UserActivity extends ListActivity implements OnMenuItemClickListener {

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static User user;
    private static ListView userPosts;
    private static UserPostsTask userPostsTask;
    private static View spinner;
    private static LayoutInflater layoutInflater;

    private ImageView userIcon;
    private TextView name;
    private TextView username;
    private TextView userPostsCount;
    private TextView userCommentsCount;
    private TextView userLikesCount;
    private TextView userFollowersCount;
    private TextView userFollowingCount;
    private TextView userBio;
    private TextView userUrl;
    private TextView userRole;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        LayoutUtils.createActionBar(this, true);

        layoutInflater = getLayoutInflater();
        spinner = layoutInflater.inflate(R.layout.actionbar_spinner, null);

        user = (User) getIntent().getSerializableExtra("user");

        userIcon = (ImageView) findViewById(R.id.user_icon_thumbnail);
        name = (TextView) findViewById(R.id.user_infobar_name);
        username = (TextView) findViewById(R.id.user_infobar_username);
        userPostsCount = (TextView) findViewById(R.id.user_post_count_value);
        userCommentsCount = (TextView) findViewById(R.id.user_comment_count_value);
        userLikesCount = (TextView) findViewById(R.id.user_like_count_value);
        userFollowersCount = (TextView) findViewById(R.id.user_follower_count_value);
        userFollowingCount = (TextView) findViewById(R.id.user_following_count_value);
        userBio = (TextView) findViewById(R.id.user_bio);
        userUrl = (TextView) findViewById(R.id.user_url);
        userRole = (TextView) findViewById(R.id.user_infobar_role);

        name.setText(user.getName());
        username.setText("@" + user.getUsername());
        userPostsCount.setText(TextUtils.numberToUSFormat(user.getPosts()));
        userCommentsCount
                .setText(TextUtils.numberToUSFormat(user.getComments()));
        userLikesCount.setText(TextUtils.numberToUSFormat(user.getLikes()));
        userFollowersCount.setText(TextUtils.numberToUSFormat(user
                .getFollowers()));
        userFollowingCount.setText(TextUtils.numberToUSFormat(user
                .getFollowing()));
        if (user.getBio() == null || user.getBio().length() == 0) {
            userBio.setVisibility(View.GONE);
        } else {
            userBio.setText(user.getBio());
        }
        if (user.getHomepageUrl() == null
                || user.getHomepageUrl().length() == 0) {
            userUrl.setVisibility(View.GONE);
        } else {
            userUrl.setText(user.getHomepageUrl());
        }
        userRole.setText(user.getIsA());
        userIcon.setImageBitmap(ImageUtils.getBitmapFromByteArray(getIntent()
                .getByteArrayExtra("user_icon")));

        userPosts = getListView();
        userPosts.setVerticalScrollBarEnabled(false);
        userPosts.setVerticalFadingEdgeEnabled(false);
        userPosts.setDivider(this.getResources().getDrawable(
                R.color.kampr_light_green));
        userPosts.setDividerHeight(1);

        userPostsTask = new UserPostsTask(this, userPosts);
        userPostsTask.execute(user.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        MenuItem actionbarRefresh = menu.findItem(R.id.actionbar_refresh);
        actionbarRefresh.setActionView(spinner);
        userPostsTask.setActionbarRefresh(actionbarRefresh);
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
            userPostsTask = new UserPostsTask(this, userPosts);
            userPostsTask.execute(user.getId());
            userPostsTask.setActionbarRefresh(item);
            break;
        case android.R.id.home:
            finish();
            break;
        default:
            return super.onOptionsItemSelected(item);
        }

        popup.show();
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.settings_menu_logout:
            executor.submit(new LogoutTask());
            Intent kampr = new Intent(UserActivity.this, KamprActivity.class);
            startActivity(kampr);
            return true;
        default:
            return false;
        }
    }

}