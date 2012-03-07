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

import com.nitindhar.forrst.model.User;
import com.nitindhar.kampr.async.UserPostsTask;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.posts.PostActivity;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.SpanUtils;
import com.nitindhar.kampr.util.TextUtils;

public class UserActivity extends ListActivity implements OnItemClickListener {
    
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
        _userPostsCount.setText(TextUtils.numberToUSFormat(_user.getPosts()));
        _userCommentsCount.setText(TextUtils.numberToUSFormat(_user.getComments()));
        _userLikesCount.setText(TextUtils.numberToUSFormat(_user.getLikes()));
        _userFollowersCount.setText(TextUtils.numberToUSFormat(_user.getFollowers()));
        _userFollowingCount.setText(TextUtils.numberToUSFormat(_user.getFollowing()));
        if(_user.getBio() == null || _user.getBio().length() == 0)
            _userBio.setVisibility(View.GONE);
        else {
            _userBio.setText(_user.getBio());
            SpanUtils.setFont(this, _userBio);
        }
        if(_user.getHomepageUrl() == null || _user.getHomepageUrl().length() == 0)
            _userUrl.setVisibility(View.GONE);
        else {
            _userUrl.setText(_user.getHomepageUrl());
            SpanUtils.setFont(this, _userUrl);
        }
        _userRole.setText(_user.getIsA());
        _userIcon.setImageBitmap(ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("user_icon")));

        _userPosts = getListView();
        _userPosts.setVerticalScrollBarEnabled(false);
        _userPosts.setVerticalFadingEdgeEnabled(false);
        _userPosts.setDivider(this.getResources().getDrawable(R.color.post_item_divider));
        _userPosts.setDividerHeight(1);
        _userPosts.setOnItemClickListener(this);
        
        _userPostsTask = new UserPostsTask(this, _userPosts);
        _userPostsTask.execute(_user.getId());
        
        SpanUtils.setFont(this, _name);
        SpanUtils.setFont(this, _username);
        SpanUtils.setFont(this, _userPostsCount);
        SpanUtils.setFont(this, _userCommentsCount);
        SpanUtils.setFont(this, _userLikesCount);
        SpanUtils.setFont(this, _userFollowingCount);
        SpanUtils.setFont(this, _userFollowersCount);
        SpanUtils.setFont(this, _userRole);
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent postIntent = new Intent(getApplicationContext(), PostActivity.class);
        PostDecorator pd = (PostDecorator) _userPosts.getItemAtPosition(position);
        postIntent.putExtra("post", pd.getPost());
        postIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
        startActivity(postIntent);
    }
    
    public static ProgressBar getSpinner() {
        return _spinner;
    }

}
