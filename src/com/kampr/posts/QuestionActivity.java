package com.kampr.posts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.runnables.posts.QuestionRunnable;

public class QuestionActivity extends PostActivity {

    private TextView _questionTitle;
    private TextView _questionUsername;
    private TextView _questionDate;
    private TextView _questionContent;
    private TextView _questionLikes;
    private TextView _questionViews;
    private TextView _questionComments;
    private ImageView _questionUserIcon;
    private ScrollView _questionContentScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);

        _questionTitle = (TextView) findViewById(R.id.question_title);
        _questionUsername = (TextView) findViewById(R.id.question_user_name);
        _questionDate = (TextView) findViewById(R.id.question_date);
        _questionUserIcon = (ImageView) findViewById(R.id.question_user_icon);
        _questionContent = (TextView) findViewById(R.id.question_content);
        _questionLikes = (TextView) findViewById(R.id.question_likes);
        _questionViews = (TextView) findViewById(R.id.question_views);
        _questionComments = (TextView) findViewById(R.id.question_comments);
        _questionContentScrollView = (ScrollView) findViewById(R.id.question_content_scroll);
        _questionContentScrollView.setVerticalScrollBarEnabled(false);
        _questionContentScrollView.setVerticalFadingEdgeEnabled(false);
        
        _fetchPostThread = new Thread(new QuestionRunnable(getIntent().getIntExtra("id", DEFAULT_POST_ID), _handler, _post));
        _fetchPostThread.start();
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _questionTitle.setText(_post.getProperty("title"));
                    _questionUsername.setText(_post.getProperty("name"));
                    _questionDate.setText(_post.getProperty("created_at"));
                    _questionContent.setText(_post.getProperty("content"));
                    _questionLikes.setText(_post.getProperty("like_count") + " Likes");
                    _questionViews.setText(_post.getProperty("view_count") + " Views");
                    _questionComments.setText(_post.getProperty("comment_count") + " Comments");
                    _questionUserIcon.setImageBitmap(fetchUserIcon(_post.getProperty("user_photos_thumb_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };

}