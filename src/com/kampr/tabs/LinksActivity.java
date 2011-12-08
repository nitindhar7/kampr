package com.kampr.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;

import com.kampr.adapters.PostsAdapter;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.Link;
import com.kampr.posts.LinkActivity;
import com.kampr.runnables.tabs.LinksRunnable;

public class LinksActivity extends PostsListActivity<Link> implements OnScrollListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _posts.setOnScrollListener(this);
        _handler = new PostsHandler<Link>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new LinksRunnable(this, _handler, _listOfPosts, _userIcons, null));
        _fetchPostsThread.start();
    }
    
    // http://stackoverflow.com/questions/1080811/android-endless-list
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
        
        if(loadMore) {
//            adapter.count += visibleCount; // or any other amount
//            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent link = new Intent(LinksActivity.this, LinkActivity.class);
        link.putExtra("id", view.getId());
        link.putExtra("link", _handler.getAdapter().getViewObject(position));
        startActivity(link);
    }
    
    public PostsAdapter<Link> getListAdapter() {
        return _handler.getAdapter();
    }
    
}