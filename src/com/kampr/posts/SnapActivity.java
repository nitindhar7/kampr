package com.kampr.posts;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;

public class SnapActivity extends PostActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.snap);

        _postTitle = (TextView) findViewById(R.id.snap_title);
        _postUsername = (TextView) findViewById(R.id.snap_user_name);
        _postDate = (TextView) findViewById(R.id.snap_date);
        _postDescription = (TextView) findViewById(R.id.snap_description);
        _postUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
        _postOriginal = (ImageView) findViewById(R.id.snap_large_url);

        super.onCreate(savedInstanceState);
        
        _postTitle.setText(_post.getProperty("title"));
        _postUsername.setText(_post.getProperty("name"));
        _postDate.setText(_post.getProperty("created_at"));
        _postDescription.setText(TextUtils.cleanseText(_post.getProperty("description")));
        _postDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        _postLikesCount.setText(_post.getProperty("like_count"));
        _postViewsCount.setText(_post.getProperty("view_count"));
        _postCommentsCount.setText(_post.getProperty("comment_count"));
        _userIconBitmap = ImageUtils.getBitmapFromByteArray(getIntent().getByteArrayExtra("post_user_icon"));
        _postUserIcon.setImageBitmap(_userIconBitmap);
        _postOriginal.setImageBitmap(ImageUtils.fetchImageBitmap(_post.getProperty("snaps_original_url")));
        _postOriginal.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fullScreen = new Intent(SnapActivity.this, SnapFullscreenActivity.class);
                fullScreen.putExtra("snaps_original_url", _post.getProperty("snaps_original_url"));
                startActivity(fullScreen);
            }
         });
    }

}