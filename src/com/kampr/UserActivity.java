package com.kampr;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.models.User;
import com.kampr.util.ImageUtils;

public class UserActivity extends Activity {
    
    protected static final NumberFormat _usFormat = NumberFormat.getIntegerInstance(Locale.US);
    
    protected static User _user;
    
    protected TextView _actionbarLogo;
    protected ImageView _userIcon;
    protected TextView _name;
    protected TextView _username;
    protected TextView _userPostsCount;
    protected TextView _userCommentsCount;
    protected TextView _userLikesCount;
    protected TextView _userFollowersCount;
    protected TextView _userFollowingCount;
    protected TextView _userBio;
    protected TextView _userUrl;
    protected TextView _userRole;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        _user = (User) getIntent().getSerializableExtra("user");

        _actionbarLogo = (TextView) findViewById(R.id.actionbar_logo);
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
        _userPostsCount.setText(_usFormat.format(_user.getPostsCount()));
        _userCommentsCount.setText(_usFormat.format(_user.getCommentsCount()));
        _userLikesCount.setText(_usFormat.format(_user.getLikesCount()));
        _userFollowersCount.setText(_usFormat.format(_user.getFollowersCount()));
        _userFollowingCount.setText(_usFormat.format(_user.getFollowingCount()));
        _userBio.setText(_user.getBio());
        _userUrl.setText(_user.getHomepageUrl());
        _userRole.setText(_user.getRole());
        _userIcon.setImageBitmap(ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("user_icon")));
        _actionbarLogo.setText(_user.getName());
    }

}
