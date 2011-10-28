package com.kampr.tabs;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.models.Link;
import com.kampr.posts.LinkActivity;

public class LinksActivity extends PostsListActivity<Link> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent link = new Intent(LinksActivity.this, LinkActivity.class);
        link.putExtra("id", view.getId());
        startActivity(link);
    }
    
    private Thread _fetchPostsThread = new Thread(new Runnable() {
        
        public void run() {
            try {
                JSONObject postsJSON = _forrst.postsList("link", null);
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
                    
                    Link link = new Link(properties);
                    _listOfPosts.add(link);
                    
                    fetchUserIcon(link);
                }

                notifyHandler();
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching link from Forrst", e);
            }
        }
        
    });
    
}