package com.kampr.posts;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.util.KamprImageUtils;
import com.kampr.util.KamprUtils;

public class QuestionActivity extends PostActivity {

    private TextView _questionTitle;
    private TextView _questionUsername;
    private TextView _questionDate;
    private TextView _questionContent;
    private ImageView _questionUserIcon;
    private ScrollView _questionContentScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.question);
        super.onCreate(savedInstanceState);

        _questionTitle = (TextView) findViewById(R.id.question_title);
        _questionUsername = (TextView) findViewById(R.id.question_user_name);
        _questionDate = (TextView) findViewById(R.id.question_date);
        _questionUserIcon = (ImageView) findViewById(R.id.question_user_icon);
        _questionContent = (TextView) findViewById(R.id.question_content);
        _questionContentScrollView = (ScrollView) findViewById(R.id.question_content_scroll);
        _questionContentScrollView.setVerticalScrollBarEnabled(false);
        
        _questionTitle.setText(_post.getProperty("title"));
        _questionUsername.setText(_post.getProperty("name"));
        _questionDate.setText(_post.getProperty("created_at"));
        _questionContent.setText(KamprUtils.cleanseText(_post.getProperty("content")));
        _questionContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = KamprImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _questionUserIcon.setImageBitmap(_userIconBitmap);
    }

}