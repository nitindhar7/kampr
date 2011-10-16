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
import com.kampr.adapters.SnapsAdapter;
import com.kampr.models.Snap;

public class SnapsActivity extends PostsListActivity {
    
    private final String ACTIVITY_TAG = "SnapsActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trustAllHosts();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            List<Snap> listOfSnaps = new ArrayList<Snap>();
            ForrstAPI forrst = new ForrstAPIClient();
            
            JSONObject postsJSON = forrst.postsList("snap", null);
            JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
            
            for(int count = 0; count < postsJSONArray.length(); count++) {
                Map<String, String> snapProperties = new HashMap<String, String>();
                JSONObject json = postsJSONArray.getJSONObject(count);
                
                snapProperties.put("id", json.getString("id"));
                snapProperties.put("post_type", json.getString("post_type"));
                snapProperties.put("post_url", json.getString("post_url"));

                long snapDateInMillis = sdf.parse(json.getString("created_at")).getTime();
                String snapDate = DateUtils.formatDateTime(null, snapDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                snapProperties.put("created_at", snapDate);
                
                snapProperties.put("name", json.getJSONObject("user").getString("name"));
                snapProperties.put("title", json.getString("title"));
                snapProperties.put("url", json.getString("url"));
                snapProperties.put("content", json.getString("content"));
                snapProperties.put("description", json.getString("description"));
                snapProperties.put("formatted_content", json.getString("formatted_content"));
                snapProperties.put("formatted_description", json.getString("formatted_description"));
                snapProperties.put("view_count", Integer.toString(json.getInt("view_count")));
                snapProperties.put("like_count", json.getString("like_count"));
                snapProperties.put("comment_count", json.getString("comment_count"));
                snapProperties.put("tag_string", json.getString("tag_string"));
                snapProperties.put("user_photos_thumb_url", json.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                snapProperties.put("snaps_original_url", json.getJSONObject("snaps").getString("original_url"));
                
                Snap snap = new Snap(snapProperties);
                listOfSnaps.add(snap);
            }
            
            ListView snaps = getListView();
            snaps.setVerticalFadingEdgeEnabled(false);
            SnapsAdapter snapsAdapter = new SnapsAdapter(SnapsActivity.this, listOfSnaps);
            snaps.setAdapter(snapsAdapter);
        } catch (JSONException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error fetching snap from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error parsing snap date", e);
        }
    }

}
