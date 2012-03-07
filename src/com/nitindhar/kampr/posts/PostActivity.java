package com.nitindhar.kampr.posts;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.forrst.model.User;
import com.nitindhar.kampr.KamprActivity;
import com.nitindhar.kampr.LogoutActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.UserActivity;
import com.nitindhar.kampr.async.SnapTask;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;
import com.nitindhar.kampr.util.SpanUtils;
import com.nitindhar.kampr.util.TextUtils;
import com.nitindhar.kampr.util.TimeUtils;

public class PostActivity extends Activity implements OnClickListener {

    private static final int LOGOUT_RESULT_CODE = 1;

    private static Post _post;
    private static ProgressBar _spinner;

    private RelativeLayout _infobar;
    private Bitmap _userIconBitmap;
    private TextView _postLikes;
    private TextView _postViews;
    private TextView _postTitle;
    private TextView _postUrl;
    private TextView _postUsername;
    private TextView _postDate;
    private TextView _postDescription;
    private TextView _postContent;
    private TextView _postViewComments;
    private TextView _postSnapLabel;
    private ImageView _postUserIcon;
    private ImageView _postOriginal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.post);
        super.onCreate(savedInstanceState);

        _spinner = (ProgressBar) findViewById(R.id.actionbar_spinner);
        _infobar = (RelativeLayout) findViewById(R.id.post_infobar);
        _postTitle = (TextView) findViewById(R.id.post_title);
        _postUrl = (TextView) findViewById(R.id.post_url);
        _postUsername = (TextView) findViewById(R.id.post_user_name);
        _postDate = (TextView) findViewById(R.id.post_date);
        _postDescription = (TextView) findViewById(R.id.post_description);
        _postUserIcon = (ImageView) findViewById(R.id.user_icon_thumbnail);
        _postLikes = (TextView) findViewById(R.id.post_like_count_label);
        _postViews = (TextView) findViewById(R.id.post_view_count_label);
        _postOriginal = (ImageView) findViewById(R.id.snap_large_url);
        _postSnapLabel = (TextView) findViewById(R.id.post_snap_call_to_action);
        _postContent = (TextView) findViewById(R.id.post_content);

        _postOriginal.setOnClickListener(this);
        _infobar.setOnClickListener(this);

        _post = (Post) getIntent().getSerializableExtra("post");

        _postTitle.setText(_post.getTitle().toUpperCase());
        _postUsername.setText(_post.getUser().getUsername());
        _postDate.setText(TimeUtils.getPostDate(_post.getCreatedAt()));
        _postDescription.setText(TextUtils.cleanseText(_post.getDescription()));
        _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikes.setText(Integer.toString(_post.getLikeCount()));
        _postViews.setText(Integer.toString(_post.getViewCount()));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("user_icon"));
        _postUserIcon.setImageBitmap(_userIconBitmap);
        
        if (_post.getCommentCount() > 0) {
            _postViewComments = (TextView) findViewById(R.id.actionbar_comment);
            
            LayoutUtils.layoutOverride(_postViewComments, View.VISIBLE);
            SpanUtils.setFont(getApplicationContext(), _postViewComments);
            
            _postViewComments.setText(Integer.toString(_post.getCommentCount()));
            _postViewComments.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent comments = new Intent(PostActivity.this, CommentsActivity.class);
                    comments.putExtra("post", _post);
                    comments.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(_userIconBitmap));
                    startActivity(comments);
                }
            });
        }

        if (_post.getPostType().equals("link")) {
            _postOriginal.setVisibility(View.GONE);
            _postContent.setVisibility(View.GONE);
            _postSnapLabel.setVisibility(View.GONE);
            _postUrl.setText(_post.getUrl());
            SpanUtils.setFont(getApplicationContext(), _postDescription);
            SpanUtils.setFont(getApplicationContext(), _postUrl);
            SpanUtils.removeUnderlines((Spannable) _postUrl.getText());
            LayoutUtils.layoutOverride(findViewById(R.id.actionbar_spinner), View.GONE);
        }
        else if (_post.getPostType().equals("snap")) {
            _postUrl.setVisibility(View.GONE);
            _postContent.setVisibility(View.GONE);
            SnapTask snapTask = new SnapTask(_postOriginal);
            snapTask.execute(_post);
            SpanUtils.setFont(getApplicationContext(), _postDescription);
        }
        else if (_post.getPostType().equals("code")) {
            _postOriginal.setVisibility(View.GONE);
            _postUrl.setVisibility(View.GONE);
            _postSnapLabel.setVisibility(View.GONE);
            _postContent.setText(_post.getContent());
            _postContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            SpanUtils.setFont(getApplicationContext(), _postDescription);
            SpanUtils.setFont(getApplicationContext(), _postContent);
            LayoutUtils.layoutOverride(findViewById(R.id.actionbar_spinner), View.GONE);
        }
        else {
            _postOriginal.setVisibility(View.GONE);
            _postUrl.setVisibility(View.GONE);
            _postContent.setVisibility(View.GONE);
            _postSnapLabel.setVisibility(View.GONE);
            _postDescription.setText(TextUtils.cleanseText(_post.getContent()));
            _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            SpanUtils.setFont(getApplicationContext(), _postDescription);
            LayoutUtils.layoutOverride(findViewById(R.id.actionbar_spinner), View.GONE);
        }
        
        SpanUtils.setFont(getApplicationContext(), _postTitle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snap_large_url:
                Intent fullScreen = new Intent(PostActivity.this, SnapFullscreenActivity.class);
                fullScreen.putExtra("snaps_original_url", _post.getSnap().getOriginalUrl());
                startActivity(fullScreen);
                break;
            case R.id.post_infobar:
                User user = _post.getUser();
                Intent userIntent = new Intent(getApplicationContext(), UserActivity.class);
                userIntent.putExtra("user", user);
                userIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(_userIconBitmap));
                startActivity(userIntent);
                break;
        }
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
            Intent logout = new Intent(PostActivity.this, LogoutActivity.class);
            startActivityForResult(logout, LOGOUT_RESULT_CODE);
            break;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case LOGOUT_RESULT_CODE:
            if (resultCode == LogoutActivity.RESULT_SUCCESS) {
                Intent kampr = new Intent(PostActivity.this, KamprActivity.class);
                startActivity(kampr);
            } else if (resultCode == LogoutActivity.RESULT_FAILURE)
                Toast.makeText(getApplicationContext(), "Error logging out. Try Again!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
            break;
        }
    }
    
    public static ProgressBar getSpinner() {
        return _spinner;
    }

}