package com.kampr.tabs;

import java.io.ByteArrayOutputStream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.Link;
import com.kampr.models.PropertyContainer;
import com.kampr.posts.LinkActivity;
import com.kampr.runnables.tabs.LinksRunnable;

public class LinksActivity extends PostsListActivity<Link> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Link>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new LinksRunnable(this, _handler, _listOfPosts, _userIcons, null));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent link = new Intent(LinksActivity.this, LinkActivity.class);
        PropertyContainer post = _handler.getAdapter().getViewObject(position);
        link.putExtra("post", post);
        
        Bitmap bmp = _userIcons.get(post.getProperty("id"));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        link.putExtra("post_user_icon", stream.toByteArray());
        
        startActivity(link);
    }
    
}