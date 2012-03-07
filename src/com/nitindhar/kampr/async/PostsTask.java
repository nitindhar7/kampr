package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.PostsActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.PostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class PostsTask extends AsyncTask<String, Integer, List<PostDecorator>> {
    
    protected static final ForrstAPI _forrst = new ForrstAPIClient();
    
    private static PostsAdapter<PostDecorator> _postsAdapter;
    
    private Context _context;
    private ListView _posts;

    public PostsTask(Context context, ListView posts) {
        _context = context;
        _posts = posts;
    }

    protected List<PostDecorator> doInBackground(String... params) {
        List<Post> posts = null;
        List<PostDecorator> listOfPosts = new ArrayList<PostDecorator>();

        if(params[0].equals("all"))
            posts = _forrst.postsAll(null);
        else
            posts = _forrst.postsList(params[0], null);
        
        for(Post post : posts) {
            Bitmap userIcon = ImageUtils.fetchUserIcon(_context, post.getUser().getPhoto().getMediumUrl(), R.drawable.forrst_default_25);
            PostDecorator pd = new PostDecorator();
            pd.setPost(post);
            pd.setUserIcon(userIcon);
            listOfPosts.add(pd);
        }
        
        return listOfPosts;
    }
    
    protected void onPostExecute(List<PostDecorator> listOfPosts) {
        _postsAdapter = new PostsAdapter<PostDecorator>(_context, listOfPosts);
        _posts.setAdapter(_postsAdapter);
        LayoutUtils.layoutOverride(PostsActivity.getSpinner(), View.GONE);
    }
    
    public PostsAdapter<PostDecorator> getAdapter() {
        return _postsAdapter;
    }

}
