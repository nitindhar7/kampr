package com.kampr.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.PropertyContainer;
import com.kampr.posts.PostActivity;
import com.kampr.runnables.PostsRunnable;
import com.kampr.util.ImageUtils;

public class SnapsActivity extends PostsListActivity<PropertyContainer> {
    
    private static final String POST_TYPE = "snap";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<PropertyContainer>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new PostsRunnable(this, _handler, _listOfPosts, _userIcons, null, POST_TYPE));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent snap = new Intent(SnapsActivity.this, PostActivity.class);
        PropertyContainer post = _handler.getAdapter().getViewObject(position);
        snap.putExtra("post", post);
        
        Bitmap bmp = _userIcons.get(post.getProperty("id"));
        snap.putExtra("post_user_icon", ImageUtils.getByteArrayFromBitmap(bmp));
        
        snap.putExtra("post_type", PostActivity.POST_SNAP);

        startActivity(snap);
    }

}
