package com.kampr.posts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.runnables.posts.CodeRunnable;
import com.kampr.util.KamprUtils;

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
        setContentView(R.layout.code);
        super.onCreate(savedInstanceState);

        _codeTitle = (TextView) findViewById(R.id.code_title);
        _codeUsername = (TextView) findViewById(R.id.code_user_name);
        _codeDate = (TextView) findViewById(R.id.code_date);
        _codeContent = (TextView) findViewById(R.id.code_content);
        _codeDescription = (TextView) findViewById(R.id.code_description);
        _codeUserIcon = (ImageView) findViewById(R.id.code_user_icon);
        _codeScroll = (ScrollView) findViewById(R.id.code_scroll);
        _codeScroll.setVerticalScrollBarEnabled(false);
        
        _codeTitle.setText(_post.getProperty("title"));
        _codeUsername.setText(_post.getProperty("name"));
        _codeDate.setText(_post.getProperty("created_at"));
        _codeContent.setText(_post.getProperty("content"));
        _codeContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _codeDescription.setText(KamprUtils.cleanseText(_post.getProperty("description")));
        _codeDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        
        _fetchPostThread = new Thread(new CodeRunnable(_handler, _post));
        _fetchPostThread.start();
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:                    
                    _userIconBitmap = fetchUserIcon(_post.getProperty("user_photos_thumb_url"));
                    _codeUserIcon.setImageBitmap(_userIconBitmap);
                    _dialog.cancel();
                    break;
            }
        }
    };

}