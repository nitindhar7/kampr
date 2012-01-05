package com.kampr.handlers;

import java.util.List;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kampr.adapters.PostsAdapter;

public class PostsHandler<T> extends AbstractHandler<T> {

    private PostsAdapter<T> _postsAdapter;
    
    public PostsHandler(Context context, ProgressBar spinner, ListView posts, List<T> listOfPosts) {
        _context = context;
        _spinner = spinner;
        _list = posts; 
        _listOfItems = listOfPosts;
    }
    
    @Override
    public void handleMessage(Message msg) {
        switch(msg.getData().getInt(FETCH_STATUS)) {
            case FETCH_COMPLETE:
                _postsAdapter = new PostsAdapter<T>(_context, _listOfItems);
                _list.setAdapter(_postsAdapter);
                _spinner.setVisibility(View.GONE);
                break;
        }
    }
    
    public PostsAdapter<T> getAdapter() {
        return _postsAdapter;
    }

}