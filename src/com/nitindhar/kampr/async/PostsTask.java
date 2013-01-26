package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.PostsActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.PostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ForrstUtil;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class PostsTask extends AsyncTask<String, Integer, List<PostDecorator>> {

    private static PostsAdapter<PostDecorator> postsAdapter;

    private final Context context;
    private final ListView posts;

    public PostsTask(Context context, ListView posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    protected List<PostDecorator> doInBackground(String... params) {
        List<Post> posts = null;
        List<PostDecorator> listOfPosts = new ArrayList<PostDecorator>();

        if (params[0].equals("all")) {
            posts = ForrstUtil.client().postsAll(null);
        } else {
            posts = ForrstUtil.client().postsList(params[0], null);
        }

        for (Post post : posts) {
            Bitmap userIcon = ImageUtils.fetchUserIcon(context, post.getUser()
                    .getPhoto().getMediumUrl(), R.drawable.forrst_default_25);
            PostDecorator pd = new PostDecorator();
            pd.setPost(post);
            pd.setUserIcon(userIcon);
            listOfPosts.add(pd);
        }

        return listOfPosts;
    }

    @Override
    protected void onPostExecute(List<PostDecorator> listOfPosts) {
        postsAdapter = new PostsAdapter<PostDecorator>(context, listOfPosts);
        posts.setAdapter(postsAdapter);
        LayoutUtils.layoutOverride(PostsActivity.getSpinner(), View.GONE);
    }

    public PostsAdapter<PostDecorator> getAdapter() {
        return postsAdapter;
    }

}