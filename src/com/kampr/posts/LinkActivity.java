package com.kampr.posts;

import android.os.Bundle;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;
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

        _linkTitle.setText(_post.getProperty("title"));
        _linkUrl.setText(_post.getProperty("url"));
        URLSpanUtils.removeUnderlines((Spannable) _linkUrl.getText());
        _linkUsername.setText(_post.getProperty("name"));
        _linkDate.setText(_post.getProperty("created_at"));
        _linkDescription.setText(TextUtils.cleanseText(_post.getProperty("description")));
        _linkDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _linkUserIcon.setImageBitmap(_userIconBitmap);
    }

}