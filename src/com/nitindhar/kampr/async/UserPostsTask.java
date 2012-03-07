package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.UserActivity;
import com.nitindhar.kampr.adapters.UserPostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class UserPostsTask extends AsyncTask<Integer, Integer, List<PostDecorator>> {
    
    protected static final ForrstAPI _forrst = new ForrstAPIClient();
    
    private Context _context;
    private ListView _userPosts;
    private List<PostDecorator> _listOfPosts;
    private UserPostsAdapter<PostDecorator> _userPostsAdapter;
    
    public UserPostsTask(Context context, ListView userPosts) {
        _listOfPosts = new ArrayList<PostDecorator>();
        _context = context;
        _userPosts = userPosts;
    }

    protected List<PostDecorator> doInBackground(Integer... ints) {
        Map<String,String> userInfo = new HashMap<String,String>();
        userInfo.put("id", Integer.toString(ints[0]));
        
        for(Post post : _forrst.userPosts(userInfo, null)) {
            PostDecorator pd = new PostDecorator();
            pd.setPost(post);
            pd.setUserIcon(ImageUtils.fetchUserIcon(_context, post.getUser().getPhoto().getThumbUrl(), R.drawable.forrst_default_25));
            _listOfPosts.add(pd);
        }

        return _listOfPosts;
    }
    
    protected void onPostExecute(List<PostDecorator> listOfPosts) {
        _userPostsAdapter = new UserPostsAdapter<PostDecorator>(_context, _listOfPosts);
        _userPosts.setAdapter(_userPostsAdapter);
        LayoutUtils.layoutOverride(UserActivity.getSpinner(), View.GONE);
    }

}
