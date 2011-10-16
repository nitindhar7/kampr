package com.kampr.tabs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.adapters.LinksAdapter;
import com.kampr.models.Link;

public class LinksActivity extends PostsListActivity {

    private final String ACTIVITY_TAG = "LinksActivity";
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        trustAllHosts();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            List<Link> listOfLinks = new ArrayList<Link>();
            ForrstAPI forrst = new ForrstAPIClient();
            
            JSONObject postsJSON = forrst.postsList("link", null);
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                Map<String, String> linkProperties = new HashMap<String, String>();
                JSONObject json = postsJSONArray.getJSONObject(count);
                
                linkProperties.put("id", json.getString("id"));
                linkProperties.put("post_type", json.getString("post_type"));
                linkProperties.put("post_url", json.getString("post_url"));

                long linkDateInMillis = sdf.parse(json.getString("created_at")).getTime();
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
                listOfLinks.add(link);
            }
            
            ListView links = getListView();
            links.setVerticalFadingEdgeEnabled(false);
            LinksAdapter linksAdapter = new LinksAdapter(LinksActivity.this, listOfLinks);
            links.setAdapter(linksAdapter);
        } catch (JSONException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error fetching link from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error parsing link date", e);
        }
    }
    
}
