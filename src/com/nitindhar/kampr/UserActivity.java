package com.nitindhar.kampr;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.forrst.api.model.User;
import com.nitindhar.kampr.async.UserPostsTask;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.posts.PostActivity;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.SpanUtils;
import com.nitindhar.kampr.util.TextUtils;
import com.nitindhar.kampr.util.TimeUtils;

public class UserActivity extends ListActivity implements OnItemClickListener {
    
    private static User user;
    private static ListView userPosts;
    private static ProgressBar spinner;
    private static UserPostsTask userPostsTask;

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

        user = (User) getIntent().getSerializableExtra("user");
        spinner = (ProgressBar) findViewById(R.id.actionbar_spinner);

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
        userCommentsCount.setText(TextUtils.numberToUSFormat(user.getComments()));
        userLikesCount.setText(TextUtils.numberToUSFormat(user.getLikes()));
        userFollowersCount.setText(TextUtils.numberToUSFormat(user.getFollowers()));
        userFollowingCount.setText(TextUtils.numberToUSFormat(user.getFollowing()));
        if(user.getBio() == null || user.getBio().length() == 0)
            userBio.setVisibility(View.GONE);
        else {
            userBio.setText(user.getBio());
            SpanUtils.setFont(this, userBio);
        }
        if(user.getHomepageUrl() == null || user.getHomepageUrl().length() == 0)
            userUrl.setVisibility(View.GONE);
        else {
            userUrl.setText(user.getHomepageUrl());
            SpanUtils.setFont(this, userUrl);
        }
        userRole.setText(user.getIsA());
        userIcon.setImageBitmap(ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("user_icon")));

        userPosts = getListView();
        userPosts.setVerticalScrollBarEnabled(false);
        userPosts.setVerticalFadingEdgeEnabled(false);
        userPosts.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        userPosts.setDividerHeight(1);
        userPosts.setOnItemClickListener(this);
        
        userPostsTask = new UserPostsTask(this, userPosts);
        userPostsTask.execute(user.getId());
        
        SpanUtils.setFont(this, name);
        SpanUtils.setFont(this, username);
        SpanUtils.setFont(this, userPostsCount);
        SpanUtils.setFont(this, userCommentsCount);
        SpanUtils.setFont(this, userLikesCount);
        SpanUtils.setFont(this, userFollowingCount);
        SpanUtils.setFont(this, userFollowersCount);
        SpanUtils.setFont(this, userRole);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent postIntent = new Intent(getApplicationContext(), PostActivity.class);
        PostDecorator pd = (PostDecorator) userPosts.getItemAtPosition(position);
        pd.getPost().setCreatedAt(TimeUtils.getPostDate(pd.getPost().getCreatedAt()));
        postIntent.putExtra("post", pd.getPost());
        postIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
        startActivity(postIntent);
    }
    
    public static ProgressBar getSpinner() {
        return spinner;
    }

}
