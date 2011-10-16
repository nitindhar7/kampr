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
import com.kampr.adapters.CodesAdapter;
import com.kampr.models.Code;

public class CodesActivity extends PostsListActivity {
    
    private final String ACTIVITY_TAG = "CodesActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trustAllHosts();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            List<Code> listOfCodes = new ArrayList<Code>();
            ForrstAPI forrst = new ForrstAPIClient();
            
            JSONObject postsJSON = forrst.postsList("code", null);
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                Map<String, String> codeProperties = new HashMap<String, String>();
                JSONObject json = postsJSONArray.getJSONObject(count);
                
                codeProperties.put("id", json.getString("id"));
                codeProperties.put("post_type", json.getString("post_type"));
                codeProperties.put("post_url", json.getString("post_url"));

                long codeDateInMillis = sdf.parse(json.getString("created_at")).getTime();
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
                codeProperties.put("tag_string", json.getString("tag_string"));
                
                Code code = new Code(codeProperties);
                listOfCodes.add(code);
            }
            
            ListView codes = getListView();
            codes.setVerticalFadingEdgeEnabled(false);
            CodesAdapter codesAdapter = new CodesAdapter(CodesActivity.this, listOfCodes);
            codes.setAdapter(codesAdapter);
        } catch (JSONException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error fetching code from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error parsing code date", e);
        }
    }

}
