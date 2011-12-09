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
import com.kampr.util.KamprImageUtils;

public class PostsRunnable extends AbstractRunnable {

    protected String _postType;
    protected Map<String,String> _forrstParams;
    protected Context _context;
    protected List<PropertyContainer> _listOfPosts;
    protected Map<String,Bitmap> _userIcons;
    
    public PostsRunnable(Context context, PostsHandler<PropertyContainer> handler, List<PropertyContainer> listOfPosts, Map<String,Bitmap> userIcons, Map<String,String> forrstParams, String postType) {
        super(handler);
        _context = context;
        _listOfPosts = listOfPosts;
        _userIcons = userIcons;
        _forrstParams = forrstParams;
        _postType = postType;
    }
    
    public void run() {
        try {
            JSONObject postsJSON = _forrst.postsList(_postType, _forrstParams);
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
                properties.put("user_photos_thumb_url", json.getJSONObject("user").getJSONObject("photos").getString("medium_url"));
                if (json.has("snaps")) {
                    properties.put("snaps_original_url", json.getJSONObject("snaps").getString("original_url"));
                }
                
                PropertyContainer post = new PropertyContainer(properties);
                _listOfPosts.add(post);

                _userIcons.put(post.getProperty("id"), KamprImageUtils.fetchUserIcon(_context, post.getProperty("user_photos_thumb_url"), R.drawable.forrst_default_25));
            }

            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching post from Forrst", e);
        }
    }

}