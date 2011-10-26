package com.kampr.tabs;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.models.Link;
import com.kampr.posts.LinkActivity;

public class LinksActivity extends PostsListActivity<Link> {

    private final String ACTIVITY_TAG = "LinksActivity";
    
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
                    Map<String, String> linkProperties = new HashMap<String, String>();
                    JSONObject json = postsJSONArray.getJSONObject(count);
                    
                    linkProperties.put("id", json.getString("id"));
                    linkProperties.put("post_type", json.getString("post_type"));
                    linkProperties.put("post_url", json.getString("post_url"));

                    long linkDateInMillis = _dateFormat.parse(json.getString("created_at")).getTime();
                    String linkDate = DateUtils.formatDateTime(null, linkDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                    linkProperties.put("created_at", linkDate);
                    
                    linkProperties.put("name", json.getJSONObject("user").getString("name"));
                    linkProperties.put("title", json.getString("title"));
                    linkProperties.put("url", json.getString("url"));
                    linkProperties.put("content", json.getString("content"));
                    linkProperties.put("description", json.getString("description"));
                    linkProperties.put("formatted_content", json.getString("formatted_content"));
                    linkProperties.put("formatted_description", json.getString("formatted_description"));
                    linkProperties.put("view_count", Integer.toString(json.getInt("view_count")));
                    linkProperties.put("like_count", json.getString("like_count"));
                    linkProperties.put("comment_count", json.getString("comment_count"));
                    linkProperties.put("user_photos_thumb_url", json.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                    linkProperties.put("tag_string", json.getString("tag_string"));
                    
                    Link link = new Link(linkProperties);
                    _listOfPosts.add(link);
                }

                notifyHandler();
            } catch (JSONException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error fetching link from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error parsing link date", e);
            }
        }
        
    });
    
}