package com.kampr.posts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.runnables.posts.SnapRunnable;

public class SnapActivity extends PostActivity {

    private TextView _snapTitle;
    private TextView _snapUrl;
    private TextView _snapUsername;
    private TextView _snapDate;
    private TextView _snapDescription;
    private ImageView _snapUserIcon;
    private ImageView _snapLargeUrl;
    private ScrollView _snapDesciptionScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.snap);
        super.onCreate(savedInstanceState);

        _snapTitle = (TextView) findViewById(R.id.snap_title);
        _snapUrl = (TextView) findViewById(R.id.snap_url);
        _snapUsername = (TextView) findViewById(R.id.snap_user_name);
        _snapDate = (TextView) findViewById(R.id.snap_date);
        _snapDescription = (TextView) findViewById(R.id.snap_description);
        _snapUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
        _snapLargeUrl = (ImageView) findViewById(R.id.snap_large_url);
        _snapDesciptionScrollView = (ScrollView) findViewById(R.id.snap_description_scroll);
        _snapDesciptionScrollView.setVerticalScrollBarEnabled(false);
        _snapDesciptionScrollView.setVerticalFadingEdgeEnabled(false);
        
        _fetchPostThread = new Thread(new SnapRunnable(_postId, _handler, _post));
        _fetchPostThread.start();
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _snapTitle.setText(_post.getProperty("title"));
                    
                    String localSnapUrl = _post.getProperty("url");
                    if(localSnapUrl == null || localSnapUrl.length() == 0) {
                        _snapUrl.setVisibility(View.GONE);
                    }
                    else {
                        _snapUrl.setText(getTruncatedText(localSnapUrl, TRUNCATED_URL_LENGTH));
                    }
                    
                    _snapUsername.setText(_post.getProperty("name"));
                    _snapDate.setText(_post.getProperty("created_at"));
                    _snapDescription.setText(_post.getProperty("description"));
                    _postLikes.setText(_post.getProperty("like_count") + " Likes");
                    _postViews.setText(_post.getProperty("view_count") + " Views");
                    _postComments.setText(_post.getProperty("comment_count") + " Comments");
                    _userIconBitmap = fetchUserIcon(_post.getProperty("user_photos_thumb_url"));
                    _snapUserIcon.setImageBitmap(_userIconBitmap);
                    _snapLargeUrl.setImageBitmap(fetchImageBitmap(_post.getProperty("snaps_large_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };

}