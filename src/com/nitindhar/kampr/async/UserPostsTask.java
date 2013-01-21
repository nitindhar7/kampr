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
import com.nitindhar.forrst.http.HttpProvider;
import com.nitindhar.forrst.model.Post;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.UserActivity;
import com.nitindhar.kampr.adapters.UserPostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class UserPostsTask extends AsyncTask<Integer, Integer, List<PostDecorator>> {

    protected static final ForrstAPI forrst = new ForrstAPIClient(HttpProvider.JAVA_NET);

    private final Context context;
    private final ListView userPosts;
    private final List<PostDecorator> listOfPosts;
    private UserPostsAdapter<PostDecorator> userPostsAdapter;

    public UserPostsTask(Context context, ListView userPosts) {
        this.listOfPosts = new ArrayList<PostDecorator>();
        this.context = context;
        this.userPosts = userPosts;
    }

    @Override
    protected List<PostDecorator> doInBackground(Integer... ints) {
        Map<String,String> userInfo = new HashMap<String,String>();
        userInfo.put("id", Integer.toString(ints[0]));

        for(Post post : forrst.userPosts(userInfo, null)) {
            PostDecorator pd = new PostDecorator();
            pd.setPost(post);
            pd.setUserIcon(ImageUtils.fetchUserIcon(context, post.getUser().getPhoto().getThumbUrl(), R.drawable.forrst_default_25));
            listOfPosts.add(pd);
        }

        return listOfPosts;
    }

    @Override
    protected void onPostExecute(List<PostDecorator> listOfPosts) {
        userPostsAdapter = new UserPostsAdapter<PostDecorator>(context, listOfPosts);
        userPosts.setAdapter(userPostsAdapter);
        LayoutUtils.layoutOverride(UserActivity.getSpinner(), View.GONE);
    }

}