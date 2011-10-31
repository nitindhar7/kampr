package com.kampr.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.models.Link;
import com.kampr.posts.LinkActivity;
import com.kampr.runnables.tabs.LinksRunnable;
import com.kampr.tabs.handlers.PostsHandler;

public class LinksActivity extends PostsListActivity<Link> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Link>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new LinksRunnable(this, _handler, _listOfPosts, _userIcons));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent link = new Intent(LinksActivity.this, LinkActivity.class);
        link.putExtra("id", view.getId());
        startActivity(link);
    }
    
}