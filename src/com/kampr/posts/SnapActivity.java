package com.kampr.posts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.runnables.posts.SnapRunnable;
import com.kampr.util.KamprUtils;

public class SnapActivity extends PostActivity {

    private TextView _snapTitle;
    private TextView _snapUsername;
    private TextView _snapDate;
    private TextView _snapDescription;
    private ImageView _snapUserIcon;
    private ImageView _snapOriginalUrl;
    private ScrollView _snapDesciptionScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.snap);
        super.onCreate(savedInstanceState);

        _snapTitle = (TextView) findViewById(R.id.snap_title);
        _snapUsername = (TextView) findViewById(R.id.snap_user_name);
        _snapDate = (TextView) findViewById(R.id.snap_date);
        _snapDescription = (TextView) findViewById(R.id.snap_description);
        _snapUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
        _snapOriginalUrl = (ImageView) findViewById(R.id.snap_large_url);
        _snapDesciptionScrollView = (ScrollView) findViewById(R.id.snap_description_scroll);
        _snapDesciptionScrollView.setVerticalScrollBarEnabled(false);
        
        _fetchPostThread = new Thread(new SnapRunnable(_postId, _handler, _post));
        _fetchPostThread.start();
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _snapTitle.setText(_post.getProperty("title"));
                    _snapUsername.setText(_post.getProperty("name"));
                    _snapDate.setText(_post.getProperty("created_at"));
                    _snapDescription.setText(KamprUtils.cleanseText(_post.getProperty("description")));
                    _snapDescription.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    _postLikesCount.setText(_post.getProperty("like_count"));
                    _postViewsCount.setText(_post.getProperty("view_count"));
                    _postCommentsCount.setText(_post.getProperty("comment_count"));
                    _userIconBitmap = fetchUserIcon(_post.getProperty("user_photos_thumb_url"));
                    _snapUserIcon.setImageBitmap(_userIconBitmap);
                    _snapOriginalUrl.setImageBitmap(fetchImageBitmap(_post.getProperty("snaps_original_url")));
                    _snapOriginalUrl.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent fullScreen = new Intent(SnapActivity.this, SnapFullscreenActivity.class);
                            fullScreen.putExtra("snaps_original_url", _post.getProperty("snaps_original_url"));
                            startActivity(fullScreen);
                        }
                     });
                    _dialog.cancel();
                    break;
            }
        }
    };

}