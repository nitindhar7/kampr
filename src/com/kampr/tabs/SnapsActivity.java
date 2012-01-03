package com.kampr.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.Post;
import com.kampr.posts.PostActivity;
import com.kampr.runnables.PostsRunnable;
import com.kampr.util.ImageUtils;

public class SnapsActivity extends PostsListActivity<Post> {
    
    private static final String POST_TYPE = "snap";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Post>(this, _dialog, _posts, _listOfPosts);
        _fetchPostsThread = new Thread(new PostsRunnable(this, _handler, _listOfPosts, null, POST_TYPE));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent snap = new Intent(SnapsActivity.this, PostActivity.class);
        Post post = _handler.getAdapter().getViewObject(position);
        snap.putExtra("post", post);
        snap.putExtra("post_user_icon", ImageUtils.getByteArrayFromBitmap(post.getUserIcon()));
        startActivity(snap);
    }

}
