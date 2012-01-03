package com.kampr.runnables;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.kampr.R;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.Post;
import com.kampr.util.ImageUtils;
import com.kampr.util.TimeUtils;

public class AllRunnable extends PostsRunnable {

    public AllRunnable(Context context, PostsHandler<Post> handler, List<Post> posts, Map<String,String> forrstParams) {
        super(context, handler, posts, forrstParams, null);
    }
    
    @Override
    public void run() {
        try {
            JSONObject postsJSON = _forrst.postsAll();
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                JSONObject json = postsJSONArray.getJSONObject(count);

                Post post = new Post();
                post.setId(json.getInt("id"));
                post.setType(json.getString("post_type"));
                post.setCreatedAt(TimeUtils.getPostDate(json.getString("created_at")));
                post.setUserName(json.getJSONObject("user").getString("name"));
                post.setTitle(json.getString("title"));
                post.setUrl(json.getString("url"));
                post.setContent(json.getString("content"));
                post.setDescription(json.getString("description"));
                post.setViewCount(json.getInt("view_count"));
                post.setLikeCount(json.getInt("like_count"));
                post.setCommentCount(json.getInt("comment_count"));
                post.setUserIcon(ImageUtils.fetchUserIcon(_context, json.getJSONObject("user").getJSONObject("photos").getString("medium_url"), R.drawable.forrst_default_25));
                if (json.getString("post_type").equals("snap")) {
                    post.setSnap(json.getJSONObject("snaps").getString("original_url"));
                }

                _posts.add(post);
            }

            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching post from Forrst", e);
        }
    }

}