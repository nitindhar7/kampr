package com.kampr.posts;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.util.ImageUtils;
import com.kampr.util.LayoutUtils;
import com.kampr.util.TextUtils;

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
        _codeDescription.setText(TextUtils.cleanseText(_post.getProperty("description")));
        _codeDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _codeUserIcon.setImageBitmap(_userIconBitmap);
        
        LayoutUtils.setFont(this, _codeTitle);
        LayoutUtils.setFont(this, _codeUsername);
        LayoutUtils.setFont(this, _codeDate);
        LayoutUtils.setFont(this, _codeContent);
        LayoutUtils.setFont(this, _codeDescription);
    }

}