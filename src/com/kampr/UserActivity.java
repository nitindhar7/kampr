package com.kampr;

import java.text.NumberFormat;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.models.User;
import com.kampr.util.ImageUtils;
import com.kampr.util.LayoutUtils;

public class UserActivity extends Activity {
    
    private static final NumberFormat _usFormat = NumberFormat.getIntegerInstance(Locale.US);
    
    private static User _user;

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

        LayoutUtils.layoutOverride(findViewById(R.id.actionbar_spinner), View.GONE);
    }

}
