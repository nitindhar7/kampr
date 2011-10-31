package com.kampr.runnables.tabs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.Code;

public class CodesRunnable extends PostsRunnable<Code> {
    
    public CodesRunnable(Context context, PostsHandler<Code> handler, List<Code> listOfPosts, Map<String,Bitmap> userIcons) {
        super(context, handler, listOfPosts, userIcons);
    }
    
    public void run() {
        try {
            JSONObject postsJSON = _forrst.postsList("code", null);
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                JSONObject json = postsJSONArray.getJSONObject(count);

                Map<String, String> properties = new HashMap<String, String>();
                properties.put("id", json.getString("id"));
                properties.put("created_at", getPostDate(json));
                properties.put("name", json.getJSONObject("user").getString("name"));
                properties.put("title", json.getString("title"));
                properties.put("url", json.getString("url"));
                properties.put("content", json.getString("content"));
                properties.put("description", json.getString("description"));
                properties.put("view_count", Integer.toString(json.getInt("view_count")));
                properties.put("like_count", json.getString("like_count"));
                properties.put("comment_count", json.getString("comment_count"));
                properties.put("user_photos_thumb_url", json.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                
                Code code = new Code(properties);
                _listOfPosts.add(code);
                
                fetchUserIcon(code);
            }
            
            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching code from Forrst", e);
        }
    }

}