package com.kampr.posts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.SettingsActivity;
import com.kampr.posts.runnables.LinkRunnable;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        Action settingsAction = new IntentAction(this, new Intent(this, SettingsActivity.class), R.drawable.ic_actionbar_settings);
        _actionBar.addAction(settingsAction);
        
        _linkTitle = (TextView) findViewById(R.id.link_title);
        _linkUrl = (TextView) findViewById(R.id.link_url);
        _linkUsername = (TextView) findViewById(R.id.link_user_name);
        _linkDate = (TextView) findViewById(R.id.link_date);
        _linkDescription = (TextView) findViewById(R.id.link_description);
        _linkUserIcon = (ImageView) findViewById(R.id.link_user_icon);
        _linkDesciptionScrollView = (ScrollView) findViewById(R.id.link_description_scroll);
        _linkDesciptionScrollView.setVerticalScrollBarEnabled(false);
        
        _fetchPostThread = new Thread(new LinkRunnable(getIntent().getIntExtra("id", DEFAULT_POST_ID), _forrst, _handler, _post));
        _fetchPostThread.start();
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
                    _linkDescription.setVerticalScrollBarEnabled(false);
                    _linkUserIcon.setImageBitmap(fetchUserIcon(_post.getProperty("user_photos_thumb_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };

}