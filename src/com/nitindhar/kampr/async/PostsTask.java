package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.adapters.PostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ForrstUtil;

public class PostsTask extends AsyncTask<String, Integer, List<PostDecorator>> {

    private static final ExecutorService executor = Executors
            .newFixedThreadPool(10);

    private static PostsAdapter<PostDecorator> postsAdapter;

    private final Context context;
    private final ListView postsView;

    public PostsTask(Context context, ListView posts) {
        this.context = context;
        postsView = posts;
    }

    @Override
    protected List<PostDecorator> doInBackground(String... params) {
        List<Post> posts = new ArrayList<Post>();
        List<PostDecorator> listOfPosts = new ArrayList<PostDecorator>();

        if (params[0].equals("all")) {
            posts = ForrstUtil.client().postsAll(null);
        } else {
            posts = ForrstUtil.client().postsList(params[0], null);
        }

        for (Post post : posts) {
            Log.i(getClass().getSimpleName(), post.toString());
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
        postsAdapter = new PostsAdapter<PostDecorator>(context, listOfPosts);
        postsView.setAdapter(postsAdapter);
    }

    public PostsAdapter<PostDecorator> getAdapter() {
        return postsAdapter;
    }

}