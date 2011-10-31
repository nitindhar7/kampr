package com.kampr.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.Snap;
import com.kampr.posts.SnapActivity;
import com.kampr.runnables.tabs.SnapsRunnable;

public class SnapsActivity extends PostsListActivity<Snap> {
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Snap>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new SnapsRunnable(this, _handler, _listOfPosts, _userIcons));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent snap = new Intent(SnapsActivity.this, SnapActivity.class);
        snap.putExtra("id", view.getId());
        startActivity(snap);
    }

}
