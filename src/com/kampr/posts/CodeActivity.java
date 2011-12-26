package com.kampr.posts;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;

public class CodeActivity extends PostActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.code);

        _postTitle = (TextView) findViewById(R.id.code_title);
        _postUsername = (TextView) findViewById(R.id.code_user_name);
        _postDate = (TextView) findViewById(R.id.code_date);
        _postContent = (TextView) findViewById(R.id.code_content);
        _postDescription = (TextView) findViewById(R.id.code_description);
        _postUserIcon = (ImageView) findViewById(R.id.code_user_icon);
        
        super.onCreate(savedInstanceState);
        
        _postTitle.setText(_post.getProperty("title"));
        _postUsername.setText(_post.getProperty("name"));
        _postDate.setText(_post.getProperty("created_at"));
        _postContent.setText(_post.getProperty("content"));
        _postContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postDescription.setText(TextUtils.cleanseText(_post.getProperty("description")));
        _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _postUserIcon.setImageBitmap(_userIconBitmap);
    }

}