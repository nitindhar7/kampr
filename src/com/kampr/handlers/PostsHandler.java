package com.kampr.handlers;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Message;
import android.widget.ListView;

import com.kampr.adapters.PostsAdapter;

public class PostsHandler<T> extends ListsHandler<T> {

    private PostsAdapter<T> _postsAdapter;
    
    public PostsHandler(Context context, ProgressDialog dialog, ListView posts, List<T> listOfPosts) {
        _context = context;
        _dialog = dialog;
        _list = posts; 
        _listOfItems = listOfPosts;
    }
    
    @Override
    public void handleMessage(Message msg) {
        switch(msg.getData().getInt(FETCH_STATUS)) {
            case FETCH_COMPLETE:
                _postsAdapter = new PostsAdapter<T>(_context, _listOfItems);
                _list.setAdapter(_postsAdapter);
                _dialog.cancel();
                break;
        }
    }
    
    public PostsAdapter<T> getAdapter() {
        return _postsAdapter;
    }

}