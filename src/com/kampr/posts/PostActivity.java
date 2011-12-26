package com.kampr.posts;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.kampr.util.LayoutUtils;

public class PostActivity extends Activity implements OnClickListener {

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
    protected TextView _postViewsCount;
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
        super.onCreate(savedInstanceState);
        _actionbarLogo = (TextView) findViewById(R.id.actionbar_logo);
        _postComments = (TextView) findViewById(R.id.post_comments);
        _postLikesCount = (TextView) findViewById(R.id.post_likes_count);
        _postViewsCount = (TextView) findViewById(R.id.post_views_count);
        _postCommentsCount = (TextView) findViewById(R.id.post_comments_count);
        _postCommentsCount.setOnClickListener(this);
        _postComments.setOnClickListener(this);
        
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

        _post = (PropertyContainer) getIntent().getSerializableExtra("post");
    }
    
    @Override
    public void onClick(View v) {
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