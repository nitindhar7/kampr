package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.nitindhar.forrst.model.Multipost;
import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.adapters.PostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ForrstUtil;

public class PostsTask extends AsyncTask<String, Integer, List<PostDecorator>> {

    private static final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.FILL_PARENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    );

    private static final ExecutorService executor = Executors
            .newFixedThreadPool(10);

    private static PostsAdapter<PostDecorator> postsAdapter;

    private MenuItem actionbarRefresh;

//    private final RelativeLayout postItem;
    private final Context context;
    private final ListView postsView;

    public PostsTask(Context context, ListView posts) {
        this.context = context;
        postsView = posts;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        postItem = (RelativeLayout) inflater.inflate(R.id.post_item, null);
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
            PostDecorator pd = new PostDecorator();
            pd.setPost(post);
            pd.setUserIconFuture(executor.submit(new UserIconFetchTask(context,
                    post.getUser().getPhoto().getMediumUrl())));
            if(post.getPostType().equals("multipost")) {
                List<Future<Bitmap>> postSnapFutures = new ArrayList<Future<Bitmap>>();
                for(Multipost multipost : post.getMultiposts()) {
                    if(multipost.getType().equals("image")) {
                        postSnapFutures.add(
                            executor.submit(new PostSnapFetchTask(multipost.getSnap().getLargeUrl()))
                        );
//                        ImageView image = new ImageView(context);
//                        image.setLayoutParams(layoutParams);
//                        image.setTag(post.getId());
//                        RelativeLayout postItemBottom = (RelativeLayout) postItem.findViewById(R.id.post_item_bottom);
//                        postItemBottom.addView(image);
                    }
                }
                pd.setPostSnapFutures(postSnapFutures);
            }
            listOfPosts.add(pd);
        }

        return listOfPosts;
    }

    @Override
    protected void onPostExecute(List<PostDecorator> listOfPosts) {
        postsAdapter = new PostsAdapter<PostDecorator>(context, listOfPosts);
        postsView.setAdapter(postsAdapter);
        if(actionbarRefresh != null) {
            actionbarRefresh.setActionView(null);
        }
    }

    public PostsAdapter<PostDecorator> getAdapter() {
        return postsAdapter;
    }

    public void setActionbarRefresh(MenuItem actionbarRefresh) {
        this.actionbarRefresh = actionbarRefresh;
    }

}