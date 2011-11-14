package com.kampr.tabs;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;

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
    
    //http://benjii.me/2010/08/endless-scrolling-listview-in-android/
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int visibleThreshold = 10;
        int currentPage = 1;
        int previousTotal = 0;
        boolean loading = true;

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            Map<String,String> forrstParams = new HashMap<String,String>();
            forrstParams.put("page", Integer.toString(currentPage + 1));
            
//            _fetchPostsThread = new Thread(new LinksRunnable(this, _handler, _listOfPosts, _userIcons, forrstParams));
//            _fetchPostsThread.start();
            
            loading = true;
        }
    }
 
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent link = new Intent(LinksActivity.this, LinkActivity.class);
        link.putExtra("id", view.getId());
        startActivity(link);
    }
    
}