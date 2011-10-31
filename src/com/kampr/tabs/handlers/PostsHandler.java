package com.kampr.tabs.handlers;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.kampr.adapters.PostsAdapter;

public class PostsHandler<T> extends Handler {
    
    public static final String FETCH_STATUS = "fetch_status";
    public static final int FETCH_COMPLETE = 1;
    
    private Context _context;
    private ProgressDialog _dialog;
    private ListView _posts;
    private PostsAdapter<T> _postsAdapter;
    private List<T> _listOfPosts;
    private Map<String,Bitmap> _userIcons;
    
    public PostsHandler(Context context, ProgressDialog dialog, ListView posts, List<T> listOfPosts, Map<String,Bitmap> userIcons) {
        _context = context;
        _dialog = dialog;
        _posts = posts;
        _userIcons = userIcons; 
        _listOfPosts = listOfPosts;
    }
    
    @Override
    public void handleMessage(Message msg) {
        switch(msg.getData().getInt(FETCH_STATUS)) {
            case FETCH_COMPLETE:
                _postsAdapter = new PostsAdapter<T>(_context, _listOfPosts, _userIcons);
                _posts.setAdapter(_postsAdapter);
                _dialog.cancel();
                break;
        }
    }

}