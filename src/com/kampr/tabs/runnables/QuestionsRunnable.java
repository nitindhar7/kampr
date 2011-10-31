package com.kampr.tabs.runnables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;

import com.kampr.models.Question;
import com.kampr.tabs.handlers.PostsHandler;

public class QuestionsRunnable extends PostsRunnable<Question> {

    public QuestionsRunnable(Context context, PostsHandler<Question> handler, List<Question> listOfPosts, Map<String,Bitmap> userIcons) {
        super(context, handler, listOfPosts, userIcons);
    }
    
    public void run() {
        try {
            JSONObject postsJSON = _forrst.postsList("question", null);
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
                
                Question question = new Question(properties);
                _listOfPosts.add(question);
                
                fetchUserIcon(question);
            }

            notifyHandler();
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching question from Forrst", e);
        }
    }
    
}