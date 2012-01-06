package com.kampr.handlers;

import java.util.List;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kampr.adapters.PostsAdapter;
import com.kampr.data.PostDao;
import com.kampr.models.Post;

public class PostsHandler<T> extends AbstractHandler<T> {

    private PostsAdapter<T> _postsAdapter;
    private static PostDao _postDao;
    
    public PostsHandler(Context context, ProgressBar spinner, ListView posts, List<T> listOfPosts, PostDao postDao) {
        _context = context;
        _spinner = spinner;
        _list = posts; 
        _listOfItems = listOfPosts;
        _postDao = postDao;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void handleMessage(Message msg) {
        switch(msg.getData().getInt(FETCH_STATUS)) {
            case FETCH_COMPLETE:
                _postsAdapter = new PostsAdapter<T>(_context, _listOfItems);
                _list.setAdapter(_postsAdapter);
                _spinner.setVisibility(View.GONE);
                _postDao.setPostsForPastWeek((List<Post>) _listOfItems);
                List<Post> posts = _postDao.getPostsFromLastWeek();
                Log.i("POSTSLISTACTIVITY", "# posts retreived from cached: " + posts.size());
                for(Post post : posts) {
                    Log.i("POSTSLISTACTIVITY", post.getUserName());
                }
                _postDao.close();
                break;
        }
    }
    
    public PostsAdapter<T> getAdapter() {
        return _postsAdapter;
    }

}