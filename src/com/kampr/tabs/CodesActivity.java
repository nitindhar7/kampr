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

import com.kampr.models.Code;
import com.kampr.posts.CodeActivity;

public class CodesActivity extends PostsListActivity<Code> {
    
    private final String ACTIVITY_TAG = "CodesActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _fetchPostsThread.start();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent code = new Intent(CodesActivity.this, CodeActivity.class);
        code.putExtra("id", view.getId());
        startActivity(code);
    }
    
    private Thread _fetchPostsThread = new Thread(new Runnable() {
        
        public void run() {
            try {
                JSONObject postsJSON = _forrst.postsList("code", null);
                JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
                
                for(int count = 0; count < postsJSONArray.length(); count++) {
                    Map<String, String> codeProperties = new HashMap<String, String>();
                    JSONObject json = postsJSONArray.getJSONObject(count);
                    
                    codeProperties.put("id", json.getString("id"));
                    codeProperties.put("post_type", json.getString("post_type"));
                    codeProperties.put("post_url", json.getString("post_url"));

                    long codeDateInMillis = _dateFormat.parse(json.getString("created_at")).getTime();
                    String codeDate = DateUtils.formatDateTime(null, codeDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                    codeProperties.put("created_at", codeDate);
                    
                    codeProperties.put("name", json.getJSONObject("user").getString("name"));
                    codeProperties.put("title", json.getString("title"));
                    codeProperties.put("url", json.getString("url"));
                    codeProperties.put("content", json.getString("content"));
                    codeProperties.put("description", json.getString("description"));
                    codeProperties.put("formatted_content", json.getString("formatted_content"));
                    codeProperties.put("formatted_description", json.getString("formatted_description"));
                    codeProperties.put("view_count", Integer.toString(json.getInt("view_count")));
                    codeProperties.put("like_count", json.getString("like_count"));
                    codeProperties.put("comment_count", json.getString("comment_count"));
                    codeProperties.put("user_photos_thumb_url", json.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                    codeProperties.put("tag_string", json.getString("tag_string"));
                    
                    Code code = new Code(codeProperties);
                    _listOfPosts.add(code);
                }
                
                notifyHandler();
            } catch (JSONException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error fetching code from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error parsing code date", e);
            }
        }
        
    });

}