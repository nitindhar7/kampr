package com.kampr.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.Code;
import com.kampr.posts.CodeActivity;
import com.kampr.runnables.tabs.CodesRunnable;

public class CodesActivity extends PostsListActivity<Code> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Code>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new CodesRunnable(this, _handler, _listOfPosts, _userIcons, null));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent code = new Intent(CodesActivity.this, CodeActivity.class);
        code.putExtra("post", _handler.getAdapter().getViewObject(position));
        startActivity(code);
    }
    
}