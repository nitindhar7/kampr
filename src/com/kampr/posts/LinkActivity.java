package com.kampr.posts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.runnables.posts.LinkRunnable;
import com.kampr.util.KamprUtils;
import com.kampr.util.URLSpanUtils;

public class LinkActivity extends PostActivity {
    
    private TextView _linkTitle;
    private TextView _linkUrl;
    private TextView _linkUsername;
    private TextView _linkDate;
    private TextView _linkDescription;
    private ImageView _linkUserIcon;
    private ScrollView _linkDesciptionScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.link);
        super.onCreate(savedInstanceState);

        _linkTitle = (TextView) findViewById(R.id.link_title);
        _linkUrl = (TextView) findViewById(R.id.link_url);
        _linkUsername = (TextView) findViewById(R.id.link_user_name);
        _linkDate = (TextView) findViewById(R.id.link_date);
        _linkDescription = (TextView) findViewById(R.id.link_description);
        _linkUserIcon = (ImageView) findViewById(R.id.link_user_icon);
        _linkDesciptionScrollView = (ScrollView) findViewById(R.id.link_description_scroll);
        _linkDesciptionScrollView.setVerticalScrollBarEnabled(false);
        
        _fetchPostThread = new Thread(new LinkRunnable(_postId, _handler, _post));
        _fetchPostThread.start();
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _linkTitle.setText(_post.getProperty("title"));
                    _linkUrl.setText(_post.getProperty("url"));
                    URLSpanUtils.removeUnderlines((Spannable) _linkUrl.getText());
                    _linkUsername.setText(_post.getProperty("name"));
                    _linkDate.setText(_post.getProperty("created_at"));
                    _linkDescription.setText(KamprUtils.cleanseText(_post.getProperty("description")));
                    _postLikes.setText(_post.getProperty("like_count") + " Likes");
                    _postViews.setText(_post.getProperty("view_count") + " Views");
                    _postComments.setText(_post.getProperty("comment_count") + " Comments");
                    _linkDescription.setVerticalScrollBarEnabled(false);
                    _userIconBitmap = fetchUserIcon(_post.getProperty("user_photos_thumb_url"));
                    _linkUserIcon.setImageBitmap(_userIconBitmap);
                    _dialog.cancel();
                    break;
            }
        }
    };

}