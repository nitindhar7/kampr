package com.kampr.runnables.posts;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.os.Handler;

import com.kampr.models.Post;

public class SnapRunnable extends PostRunnable {
    
    public SnapRunnable(int postId, Handler handler, Post post) {
        super(postId, handler, post);
    }
    
    public void run() {
        try {
            _postJSON = _forrst.postsShow(_postId);

            Map<String, String> properties = new HashMap<String, String>();
            properties.put("id", _postJSON.getString("id"));
            properties.put("post_type", _postJSON.getString("post_type"));
            properties.put("post_url", _postJSON.getString("post_url"));
            properties.put("created_at", getPostDate(_postJSON));
            properties.put("name", _postJSON.getJSONObject("user").getString("name"));
            properties.put("title", _postJSON.getString("title"));
            properties.put("url", _postJSON.getString("url"));
            properties.put("content", _postJSON.getString("content"));
            properties.put("description", _postJSON.getString("description"));
            properties.put("formatted_content", _postJSON.getString("formatted_content"));
            properties.put("formatted_description", _postJSON.getString("formatted_description"));
            properties.put("view_count", Integer.toString(_postJSON.getInt("view_count")));
            properties.put("like_count", _postJSON.getString("like_count"));
            properties.put("comment_count", _postJSON.getString("comment_count"));
            properties.put("user_photos_thumb_url", _postJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
            properties.put("snaps_large_url", _postJSON.getJSONObject("snaps").getString("large_url"));
            properties.put("tag_string", _postJSON.getString("tag_string"));
            
            _post.setProperties(properties);
            
            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching snap from Forrst", e);
        }
    }

}