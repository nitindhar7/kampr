package com.kampr.runnables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.kampr.R;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.PropertyContainer;
import com.kampr.util.ImageUtils;
import com.kampr.util.TimeUtils;

public class AllRunnable extends PostsRunnable {

    public AllRunnable(Context context, PostsHandler<PropertyContainer> handler, List<PropertyContainer> listOfPosts, Map<String,Bitmap> userIcons, Map<String,String> forrstParams) {
        super(context, handler, listOfPosts, userIcons, forrstParams, null);
    }
    
    @Override
    public void run() {
        try {
            JSONObject postsJSON = _forrst.postsAll();
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                JSONObject json = postsJSONArray.getJSONObject(count);

                Map<String, String> properties = new HashMap<String, String>();
                properties.put("id", json.getString("id"));
                properties.put("post_type", json.getString("post_type"));
                properties.put("created_at", TimeUtils.getPostDate(json.getString("created_at")));
                properties.put("name", json.getJSONObject("user").getString("name"));
                properties.put("title", json.getString("title"));
                properties.put("url", json.getString("url"));
                properties.put("content", json.getString("content"));
                properties.put("description", json.getString("description"));
                properties.put("view_count", Integer.toString(json.getInt("view_count")));
                properties.put("like_count", json.getString("like_count"));
                properties.put("comment_count", json.getString("comment_count"));
                properties.put("user_photos_thumb_url", json.getJSONObject("user").getJSONObject("photos").getString("medium_url"));
                if (json.getString("post_type").equals("snap")) {
                    properties.put("snaps_original_url", json.getJSONObject("snaps").getString("original_url"));
                }
                
                PropertyContainer post = (PropertyContainer) new PropertyContainer(properties);
                _listOfPosts.add(post);

                _userIcons.put(post.getProperty("id"), ImageUtils.fetchUserIcon(_context, post.getProperty("user_photos_thumb_url"), R.drawable.forrst_default_25));
            }

            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching post from Forrst", e);
        }
    }

}