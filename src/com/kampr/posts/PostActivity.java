package com.kampr.posts;

import java.io.ByteArrayOutputStream;
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
import android.widget.TextView;
import android.widget.Toast;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.KamprActivity;
import com.kampr.LogoutActivity;
import com.kampr.R;
import com.kampr.models.PropertyContainer;
import com.kampr.util.ImageUtils;
import com.kampr.util.LayoutUtils;
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

    protected static PropertyContainer _post;
    
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
    protected ImageView _postUserIcon;
    protected ImageView _postOriginal;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.post);
        super.onCreate(savedInstanceState);
        _actionbarLogo = (TextView) findViewById(R.id.actionbar_logo);
        _postTitle = (TextView) findViewById(R.id.post_title);
        _postUrl = (TextView) findViewById(R.id.post_url);
        _postUsername = (TextView) findViewById(R.id.post_user_name);
        _postDate = (TextView) findViewById(R.id.post_date);
        _postDescription = (TextView) findViewById(R.id.post_description);
        _postUserIcon = (ImageView) findViewById(R.id.post_user_icon);
        _postLikes = (TextView) findViewById(R.id.post_likes);
        _postViews = (TextView) findViewById(R.id.post_views);
        _postComments = (TextView) findViewById(R.id.post_comments);
        _postLikesCount = (TextView) findViewById(R.id.post_likes_count);
        _postViewsCount = (TextView) findViewById(R.id.post_views_count);
        _postCommentsCount = (TextView) findViewById(R.id.post_comments_count);
        _postOriginal = (ImageView) findViewById(R.id.snap_large_url);
        _postContent = (TextView) findViewById(R.id.post_content);

        _postCommentsCount.setOnClickListener(this);
        _postComments.setOnClickListener(this);
        _postOriginal.setOnClickListener(this);
        
        LayoutUtils.setFont(this, _actionbarLogo, LayoutUtils.FONT_BOLD);
        LayoutUtils.setFont(this, _postLikesCount);
        LayoutUtils.setFont(this, _postViewsCount);
        LayoutUtils.setFont(this, _postCommentsCount);
        LayoutUtils.setFont(this, _postTitle);
        LayoutUtils.setFont(this, _postUrl);
        LayoutUtils.setFont(this, _postUsername);
        LayoutUtils.setFont(this, _postDate);
        LayoutUtils.setFont(this, _postDescription);
        LayoutUtils.setFont(this, _postContent);
        LayoutUtils.setFont(this, _postLikes);
        LayoutUtils.setFont(this, _postViews);
        LayoutUtils.setFont(this, _postComments);

        _post = (PropertyContainer) getIntent().getSerializableExtra("post");
        
        _postTitle.setText(_post.getProperty("title"));
        _postUsername.setText(_post.getProperty("name"));
        _postDate.setText(_post.getProperty("created_at"));
        _postDescription.setText(TextUtils.cleanseText(_post.getProperty("description")));
        _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _postUserIcon.setImageBitmap(_userIconBitmap);
        
        switch(this.getIntent().getIntExtra("post_type", POST_LINK)) {
            case POST_LINK:
                _postOriginal.setVisibility(View.GONE);
                _postContent.setVisibility(View.GONE);
                _postUrl.setText(_post.getProperty("url"));
                LayoutUtils.removeUnderlines((Spannable) _postUrl.getText());
                break;
            case POST_SNAP:
                _postUrl.setVisibility(View.GONE);
                _postContent.setVisibility(View.GONE);
                _postOriginal.setImageBitmap(ImageUtils.fetchImageBitmap(_post.getProperty("snaps_original_url")));
                break;
            case POST_CODE:
                _postOriginal.setVisibility(View.GONE);
                _postUrl.setVisibility(View.GONE);
                _postContent.setText(_post.getProperty("content"));
                _postContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case POST_QUESTION:
                _postOriginal.setVisibility(View.GONE);
                _postUrl.setVisibility(View.GONE);
                _postContent.setVisibility(View.GONE);
                _postDescription.setText(TextUtils.cleanseText(_post.getProperty("content")));
                _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
        }
    }
    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.post_comments: case R.id.post_comments_count:
                if (Integer.parseInt(_post.getProperty("comment_count")) > 0) {
                    Intent comments = new Intent(PostActivity.this, CommentsActivity.class);
                    comments.putExtra("post_id", _post.getProperty("id"));
                    comments.putExtra("post_title", _post.getProperty("title"));
                    comments.putExtra("post_name", _post.getProperty("name"));
                    comments.putExtra("post_created_at", _post.getProperty("created_at"));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    _userIconBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    comments.putExtra("post_user_icon", stream.toByteArray());
                    startActivity(comments);
                }
                else {
                    Toast.makeText(getApplicationContext() , "Post has no comments", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.snap_large_url:
                Intent fullScreen = new Intent(PostActivity.this, SnapFullscreenActivity.class);
                fullScreen.putExtra("snaps_original_url", _post.getProperty("snaps_original_url"));
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

        switch(requestCode) {
            case LOGOUT_RESULT_CODE:
                if(resultCode == LogoutActivity.RESULT_SUCCESS) {
                    Intent kampr = new Intent(PostActivity.this, KamprActivity.class);
                    startActivity(kampr);
                }
                else if(resultCode == LogoutActivity.RESULT_FAILURE)
                    Toast.makeText(getApplicationContext() , "Error logging out. Try Again!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}