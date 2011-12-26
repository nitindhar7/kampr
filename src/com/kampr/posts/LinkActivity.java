package com.kampr.posts;

import android.os.Bundle;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.util.ImageUtils;
import com.kampr.util.LayoutUtils;
import com.kampr.util.TextUtils;

public class LinkActivity extends PostActivity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.link);

        _postTitle = (TextView) findViewById(R.id.post_title);
        _postUrl = (TextView) findViewById(R.id.post_url);
        _postUsername = (TextView) findViewById(R.id.post_user_name);
        _postDate = (TextView) findViewById(R.id.post_date);
        _postDescription = (TextView) findViewById(R.id.post_description);
        _postUserIcon = (ImageView) findViewById(R.id.post_user_icon);
        
        super.onCreate(savedInstanceState);

        _postTitle.setText(_post.getProperty("title"));
        _postUrl.setText(_post.getProperty("url"));
        LayoutUtils.removeUnderlines((Spannable) _postUrl.getText());
        _postUsername.setText(_post.getProperty("name"));
        _postDate.setText(_post.getProperty("created_at"));
        _postDescription.setText(TextUtils.cleanseText(_post.getProperty("description")));
        _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _postUserIcon.setImageBitmap(_userIconBitmap);
    }

}