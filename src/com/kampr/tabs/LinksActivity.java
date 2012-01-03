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

public class LinksActivity extends PostsListActivity<Post> {
    
    private static final String POST_TYPE = "link";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Post>(this, _dialog, _posts, _listOfPosts);
        _fetchPostsThread = new Thread(new PostsRunnable(this, _handler, _listOfPosts, null, POST_TYPE));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent link = new Intent(LinksActivity.this, PostActivity.class);
        Post post = _handler.getAdapter().getViewObject(position);
        link.putExtra("post", post);
        link.putExtra("post_user_icon", ImageUtils.getByteArrayFromBitmap(post.getUserIcon()));
        startActivity(link);
    }
    
}