package com.kampr;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kampr.models.User;
import com.kampr.runnables.UserPostsTask;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;

public class UserActivity extends ListActivity {
    
    private static User _user;
    private static ListView _userPosts;
    private static ProgressBar _spinner;
    private static UserPostsTask _userPostsTask;

    private ImageView _userIcon;
    private TextView _name;
    private TextView _username;
    private TextView _userPostsCount;
    private TextView _userCommentsCount;
    private TextView _userLikesCount;
    private TextView _userFollowersCount;
    private TextView _userFollowingCount;
    private TextView _userBio;
    private TextView _userUrl;
    private TextView _userRole;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        _user = (User) getIntent().getSerializableExtra("user");
        _spinner = (ProgressBar) findViewById(R.id.actionbar_spinner);

        _userIcon = (ImageView) findViewById(R.id.user_icon_thumbnail);
        _name = (TextView) findViewById(R.id.user_infobar_name);
        _username = (TextView) findViewById(R.id.user_infobar_username);
        _userPostsCount = (TextView) findViewById(R.id.user_post_count_value);
        _userCommentsCount = (TextView) findViewById(R.id.user_comment_count_value);
        _userLikesCount = (TextView) findViewById(R.id.user_like_count_value);
        _userFollowersCount = (TextView) findViewById(R.id.user_follower_count_value);
        _userFollowingCount = (TextView) findViewById(R.id.user_following_count_value);
        _userBio = (TextView) findViewById(R.id.user_bio);
        _userUrl = (TextView) findViewById(R.id.user_url);
        _userRole = (TextView) findViewById(R.id.user_infobar_role);
        
        _name.setText(_user.getName());
        _username.setText("@" + _user.getUsername());
        _userPostsCount.setText(TextUtils.numberToUSFormat(_user.getPostsCount()));
        _userCommentsCount.setText(TextUtils.numberToUSFormat(_user.getCommentsCount()));
        _userLikesCount.setText(TextUtils.numberToUSFormat(_user.getLikesCount()));
        _userFollowersCount.setText(TextUtils.numberToUSFormat(_user.getFollowersCount()));
        _userFollowingCount.setText(TextUtils.numberToUSFormat(_user.getFollowingCount()));
        if(_user.getBio() == null || _user.getBio().length() == 0)
            _userBio.setVisibility(View.GONE);
        else
            _userBio.setText(_user.getBio());
        if(_user.getHomepageUrl() == null || _user.getHomepageUrl().length() == 0)
            _userUrl.setVisibility(View.GONE);
        else
            _userUrl.setText(_user.getHomepageUrl());
        _userRole.setText(_user.getRole());
        _userIcon.setImageBitmap(ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("user_icon")));

        _userPosts = getListView();
        _userPosts.setVerticalScrollBarEnabled(false);
        _userPosts.setVerticalFadingEdgeEnabled(false);
        _userPosts.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        _userPosts.setDividerHeight(1);
        
        _userPostsTask = new UserPostsTask(getApplicationContext(), _userPosts);
        _userPostsTask.execute(_user.getId());
    }
    
    public static ProgressBar getSpinner() {
        return _spinner;
    }

}
