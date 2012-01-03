package com.kampr.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.Post;
import com.kampr.models.PropertyContainer;
import com.kampr.posts.PostActivity;
import com.kampr.runnables.AllRunnable;
import com.kampr.util.ImageUtils;

public class AllActivity extends PostsListActivity<PropertyContainer> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Post>(this, _dialog, _posts, _listOfPosts);
        _fetchPostsThread = new Thread(new AllRunnable(this, _handler, _listOfPosts, null));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent postIntent = new Intent(AllActivity.this, PostActivity.class);
        Post post = _handler.getAdapter().getViewObject(position);
        
        if (post.getType().equals("link")) {
            postIntent.putExtra("post_type", PostActivity.POST_LINK);
        }
        else if (post.getType().equals("snap")) {
            postIntent.putExtra("post_type", PostActivity.POST_SNAP);
        }
        else if (post.getType().equals("code")) {
            postIntent.putExtra("post_type", PostActivity.POST_CODE);
        }
        else {
            postIntent.putExtra("post_type", PostActivity.POST_QUESTION);
        }
        
        postIntent.putExtra("post", post);
        postIntent.putExtra("post_user_icon", ImageUtils.getByteArrayFromBitmap(post.getUserIcon()));
        
        startActivity(postIntent);
    }

}