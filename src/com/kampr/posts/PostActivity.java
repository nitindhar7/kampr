package com.kampr.posts;

import java.text.SimpleDateFormat;

import org.json.JSONObject;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.KamprActivity;
import com.kampr.LogoutActivity;
import com.kampr.R;
import com.kampr.models.Post;
import com.kampr.util.ImageUtils;
import com.kampr.util.LayoutUtils;
import com.kampr.util.SpanUtils;
import com.kampr.util.TextUtils;

public class PostActivity extends Activity implements OnClickListener {

    public static final int POST_LINK = 100;
    public static final int POST_SNAP = 101;
    public static final int POST_CODE = 102;
    public static final int POST_QUESTION = 103;

    protected static final int LOGOUT_RESULT_CODE = 1;
    protected static final int TRUNCATED_URL_LENGTH = 35;
    protected static final int DEFAULT_POST_ID = -1;
    protected static final int FETCH_COMPLETE = 1;
    protected static final String FETCH_STATUS = "fetch_status";
    protected static final ForrstAPI _forrst = new ForrstAPIClient();
    protected static final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected static Post _post;

    protected JSONObject _postJSON;
    protected Thread _fetchPostThread;
    protected TextView _actionbarLogo;
    protected Bitmap _userIconBitmap;
    protected TextView _postLikesCount;
    protected TextView _postLikes;
    protected TextView _postViewsCount;
    protected TextView _postViews;
    protected TextView _postCommentsCount;
    protected TextView _postComments;
    protected TextView _postTitle;
    protected TextView _postUrl;
    protected TextView _postUsername;
    protected TextView _postDate;
    protected TextView _postDescription;
    protected TextView _postContent;
    protected RelativeLayout _postViewComments;
    protected ImageView _postUserIcon;
    protected ImageView _postOriginal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.post);
        super.onCreate(savedInstanceState);

        LayoutUtils.layoutOverride(findViewById(R.id.post_comments), View.GONE);
        LayoutUtils.layoutOverride(findViewById(R.id.post_comments_count), View.GONE);

        _actionbarLogo = (TextView) findViewById(R.id.actionbar_logo);
        _postTitle = (TextView) findViewById(R.id.post_title);
        _postUrl = (TextView) findViewById(R.id.post_url);
        _postUsername = (TextView) findViewById(R.id.post_user_name);
        _postDate = (TextView) findViewById(R.id.post_date);
        _postDescription = (TextView) findViewById(R.id.post_description);
        _postUserIcon = (ImageView) findViewById(R.id.user_icon_thumbnail);
        _postLikes = (TextView) findViewById(R.id.post_likes);
        _postViews = (TextView) findViewById(R.id.post_views);
        _postLikesCount = (TextView) findViewById(R.id.post_likes_count);
        _postViewsCount = (TextView) findViewById(R.id.post_views_count);
        _postOriginal = (ImageView) findViewById(R.id.snap_large_url);
        _postContent = (TextView) findViewById(R.id.post_content);
        _postViewComments = (RelativeLayout) findViewById(R.id.post_view_comments);

        _postOriginal.setOnClickListener(this);

        _post = (Post) getIntent().getSerializableExtra("post");

        if (_post.getCommentCount() == 0) {
            LayoutUtils.layoutOverride(_postViewComments, View.GONE);
        } else {
            _postComments = (TextView) findViewById(R.id.post_view_comments_arrow);
            _postCommentsCount = (TextView) findViewById(R.id.post_view_comments_count);
            _postCommentsCount.setText(Integer.toString(_post.getCommentCount()));
            _postViewComments.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent comments = new Intent(PostActivity.this, CommentsActivity.class);
                    comments.putExtra("post_id", _post.getId());
                    startActivity(comments);
                }
            });
        }

        _postTitle.setText(_post.getTitle());
        _postUsername.setText(_post.getUserName());
        _postDate.setText(_post.getCreatedAt());
        _postDescription.setText(TextUtils.cleanseText(_post.getDescription()));
        _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(Integer.toString(_post.getLikeCount()));
        _postViewsCount.setText(Integer.toString(_post.getViewCount()));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _postUserIcon.setImageBitmap(_userIconBitmap);

        switch (getIntent().getIntExtra("post_type", POST_LINK)) {
            case POST_LINK:
                _actionbarLogo.setText("Link");
                _postOriginal.setVisibility(View.GONE);
                _postContent.setVisibility(View.GONE);
                _postUrl.setText(_post.getUrl());
                SpanUtils.removeUnderlines((Spannable) _postUrl.getText());
                break;
            case POST_SNAP:
                _actionbarLogo.setText("Snap");
                _postUrl.setVisibility(View.GONE);
                _postContent.setVisibility(View.GONE);
                _postOriginal.setImageBitmap(ImageUtils.fetchImageBitmap(_post.getSnap()));
                break;
            case POST_CODE:
                _actionbarLogo.setText("Code");
                _postOriginal.setVisibility(View.GONE);
                _postUrl.setVisibility(View.GONE);
                _postContent.setText(_post.getContent());
                _postContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case POST_QUESTION:
                _actionbarLogo.setText("Question");
                _postOriginal.setVisibility(View.GONE);
                _postUrl.setVisibility(View.GONE);
                _postContent.setVisibility(View.GONE);
                _postDescription.setText(TextUtils.cleanseText(_post.getContent()));
                _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snap_large_url:
                Intent fullScreen = new Intent(PostActivity.this, SnapFullscreenActivity.class);
                fullScreen.putExtra("snaps_original_url", _post.getSnap());
                startActivity(fullScreen);
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

}