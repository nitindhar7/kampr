package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.forrst.api.model.Post;
import com.forrst.api.model.Snap;
import com.forrst.api.model.User;
import com.nitindhar.kampr.PostsActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.PostsAdapter;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;
import com.nitindhar.kampr.util.TextUtils;
import com.nitindhar.kampr.util.TimeUtils;

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
        List<PostDecorator> listOfPosts = new ArrayList<PostDecorator>();
        
        try {
            JSONObject postsJSON = null;
            if(params[0].equals("all"))
                postsJSON = _forrst.postsAll(null);
            else
                postsJSON = _forrst.postsList(params[0], null);
            
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                JSONObject json = postsJSONArray.getJSONObject(count);
                JSONObject userJson = json.getJSONObject("user");
                
                Bitmap userIcon = ImageUtils.fetchUserIcon(_context, userJson.getJSONObject("photos").getString("medium_url"), R.drawable.forrst_default_25);
                
                User user = new User();
                user.setId(userJson.getInt("id"));
                user.setUsername(userJson.getString("username"));
                user.setName(userJson.getString("name"));
                user.setUrl(userJson.getString("url"));
                user.setPosts(userJson.getInt("posts"));
                user.setComments(userJson.getInt("comments"));
                user.setLikes(userJson.getInt("likes"));
                user.setFollowers(userJson.getInt("followers"));
                user.setFollowing(userJson.getInt("following"));
                user.setBio(TextUtils.convertHtmlToText(userJson.getString("bio")));
                user.setIsA(userJson.getString("is_a"));
                user.setHomepageUrl(userJson.getString("homepage_url"));
                if (userJson.has("twitter")) {
                    user.setTwitter(userJson.getString("twitter"));
                }
                user.setTagString(userJson.getString("tag_string"));

                Post post = new Post();
                post.setId(json.getInt("id"));
                post.setUser(user);
                post.setPostType(json.getString("post_type"));
                post.setCreatedAt(TimeUtils.getPostDate(json.getString("created_at")));
                post.setTitle(json.getString("title"));
                post.setUrl(json.getString("url"));
                post.setContent(json.getString("content"));
                post.setDescription(json.getString("description"));
                post.setViewCount(json.getInt("view_count"));
                post.setLikeCount(json.getInt("like_count"));
                post.setCommentCount(json.getInt("comment_count"));
                if(json.has("snaps")) {
                    Snap snap = new Snap();
                    snap.setOriginalUrl(json.getJSONObject("snaps").getString("original_url"));
                    post.setSnap(snap);
                }

                PostDecorator pd = new PostDecorator();
                pd.setPost(post);
                pd.setUserIcon(userIcon);
                listOfPosts.add(pd);
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching posts from Forrst", e);
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
