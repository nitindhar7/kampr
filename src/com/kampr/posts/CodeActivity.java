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
import com.kampr.runnables.posts.CodeRunnable;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class CodeActivity extends PostActivity {

    private TextView _codeTitle;
    private TextView _codeUsername;
    private TextView _codeDate;
    private TextView _codeContent;
    private TextView _codeDescription;
    private ImageView _codeUserIcon;
    private ScrollView _codeScroll;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        Action settingsAction = new IntentAction(this, new Intent(this, SettingsActivity.class), R.drawable.ic_actionbar_settings);
        _actionBar.addAction(settingsAction);

        _fetchPostThread = new Thread(new CodeRunnable(getIntent().getIntExtra("id", DEFAULT_POST_ID), _handler, _post));
        _fetchPostThread.start();
        
        _codeTitle = (TextView) findViewById(R.id.code_title);
        _codeUsername = (TextView) findViewById(R.id.code_user_name);
        _codeDate = (TextView) findViewById(R.id.code_date);
        _codeContent = (TextView) findViewById(R.id.code_content);
        _codeDescription = (TextView) findViewById(R.id.code_description);
        _codeUserIcon = (ImageView) findViewById(R.id.code_user_icon);
        _codeScroll = (ScrollView) findViewById(R.id.code_scroll);
        _codeScroll.setVerticalScrollBarEnabled(false);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _codeTitle.setText(_post.getProperty("title"));
                    _codeUsername.setText(_post.getProperty("name"));
                    _codeDate.setText(_post.getProperty("created_at"));
                    _codeContent.setText(_post.getProperty("content"));
                    _codeDescription.setText(_post.getProperty("description"));
                    _codeUserIcon.setImageBitmap(fetchUserIcon(_post.getProperty("user_photos_thumb_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };

}