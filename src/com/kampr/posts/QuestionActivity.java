package com.kampr.posts;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;

public class QuestionActivity extends PostActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.question);

        _postTitle = (TextView) findViewById(R.id.question_title);
        _postUsername = (TextView) findViewById(R.id.question_user_name);
        _postDate = (TextView) findViewById(R.id.question_date);
        _postUserIcon = (ImageView) findViewById(R.id.question_user_icon);
        _postContent = (TextView) findViewById(R.id.question_content);
        
        super.onCreate(savedInstanceState);
        
        _postTitle.setText(_post.getProperty("title"));
        _postUsername.setText(_post.getProperty("name"));
        _postDate.setText(_post.getProperty("created_at"));
        _postContent.setText(TextUtils.cleanseText(_post.getProperty("content")));
        _postContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _postUserIcon.setImageBitmap(_userIconBitmap);
    }

}