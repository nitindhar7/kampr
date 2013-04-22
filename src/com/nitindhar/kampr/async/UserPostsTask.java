package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.widget.ListView;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.adapters.UserPostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ForrstUtil;

public class UserPostsTask extends
        AsyncTask<Integer, Integer, List<PostDecorator>> {

    private static final ExecutorService executor = Executors
            .newFixedThreadPool(10);

    private final Context context;
    private final ListView userPosts;
    private final List<PostDecorator> listOfPosts;

    private UserPostsAdapter<PostDecorator> userPostsAdapter;
    private MenuItem actionbarRefresh;

    public UserPostsTask(Context context, ListView userPosts) {
        listOfPosts = new ArrayList<PostDecorator>();
        this.context = context;
        this.userPosts = userPosts;
    }

    @Override
    protected List<PostDecorator> doInBackground(Integer... ints) {
        Map<String, String> userInfo = new HashMap<String, String>();
        userInfo.put("id", Integer.toString(ints[0]));

        for (Post post : ForrstUtil.client().userPosts(userInfo, null)) {
            PostDecorator pd = new PostDecorator();
            pd.setPost(post);
            pd.setUserIconFuture(executor.submit(new UserIconFetchTask(context,
                    post.getUser().getPhoto().getThumbUrl())));
            listOfPosts.add(pd);
        }

        return listOfPosts;
    }

    @Override
    protected void onPostExecute(List<PostDecorator> listOfPosts) {
        userPostsAdapter = new UserPostsAdapter<PostDecorator>(context,
                listOfPosts);
        userPosts.setAdapter(userPostsAdapter);
        if(actionbarRefresh != null) {
            actionbarRefresh.setActionView(null);
        }
    }

    public void setActionbarRefresh(MenuItem actionbarRefresh) {
        this.actionbarRefresh = actionbarRefresh;
    }

}