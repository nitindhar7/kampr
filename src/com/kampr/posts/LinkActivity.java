package com.kampr.posts;

import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.posts.comments.CommentsActivity;
import com.kampr.runnables.posts.LinkRunnable;

public class LinkActivity extends PostActivity implements OnClickListener {

    private int _postId;
    
    private TextView _linkTitle;
    private TextView _linkUrl;
    private TextView _linkUsername;
    private TextView _linkDate;
    private TextView _linkDescription;
    private TextView _linkLikes;
    private TextView _linkViews;
    private TextView _linkComments;
    private ImageView _linkUserIcon;
    private ScrollView _linkDesciptionScrollView;
    
    private Bitmap _linkUserIconBitmap;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link);
        
        _linkTitle = (TextView) findViewById(R.id.link_title);
        _linkUrl = (TextView) findViewById(R.id.link_url);
        _linkUsername = (TextView) findViewById(R.id.link_user_name);
        _linkDate = (TextView) findViewById(R.id.link_date);
        _linkDescription = (TextView) findViewById(R.id.link_description);
        _linkLikes = (TextView) findViewById(R.id.link_likes);
        _linkViews = (TextView) findViewById(R.id.link_views);
        _linkComments = (TextView) findViewById(R.id.link_comments);
        _linkUserIcon = (ImageView) findViewById(R.id.link_user_icon);
        _linkDesciptionScrollView = (ScrollView) findViewById(R.id.link_description_scroll);
        _linkDesciptionScrollView.setVerticalScrollBarEnabled(false);
        _linkDesciptionScrollView.setVerticalFadingEdgeEnabled(false);
        
        _postId = getIntent().getIntExtra("id", DEFAULT_POST_ID);
        
        _fetchPostThread = new Thread(new LinkRunnable(_postId, _handler, _post));
        _fetchPostThread.start();
        
        _linkComments.setOnClickListener(this);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _linkTitle.setText(_post.getProperty("title"));
                    _linkUrl.setText(getTruncatedText(_post.getProperty("url"), TRUNCATED_URL_LENGTH));
                    _linkUsername.setText(_post.getProperty("name"));
                    _linkDate.setText(_post.getProperty("created_at"));
                    _linkDescription.setText(_post.getProperty("description"));
                    _linkLikes.setText(_post.getProperty("like_count") + " Likes");
                    _linkViews.setText(_post.getProperty("view_count") + " Views");
                    _linkComments.setText(_post.getProperty("comment_count") + " Comments");
                    _linkDescription.setVerticalScrollBarEnabled(false);
                    _linkUserIconBitmap = fetchUserIcon(_post.getProperty("user_photos_thumb_url"));
                    _linkUserIcon.setImageBitmap(_linkUserIconBitmap);
                    _dialog.cancel();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        Intent comments = new Intent(LinkActivity.this, CommentsActivity.class);
        comments.putExtra("post_id", _postId);
        comments.putExtra("post_title", _post.getProperty("title"));
        comments.putExtra("post_name", _post.getProperty("name"));
        comments.putExtra("post_created_at", _post.getProperty("created_at"));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        _linkUserIconBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        comments.putExtra("post_user_icon", stream.toByteArray());
        startActivity(comments);
    }

}