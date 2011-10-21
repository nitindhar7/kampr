package com.kampr.tabs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.R;
import com.kampr.adapters.SnapsAdapter;
import com.kampr.models.Snap;
import com.kampr.posts.SnapActivity;

public class SnapsActivity extends PostsListActivity<Snap> implements OnItemClickListener {
    
    private final String ACTIVITY_TAG = "SnapsActivity";

    private SnapsAdapter _postsAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trustAllHosts();
        
        _listOfPosts = new ArrayList<Snap>();
        
        _posts = getListView();
        _posts.setVerticalScrollBarEnabled(false);
        _posts.setVerticalFadingEdgeEnabled(false);
        _posts.setDivider(getResources().getDrawable(R.color.post_item_divider));
        _posts.setDividerHeight(1);
        _posts.setOnItemClickListener(this);

        _dialog = ProgressDialog.show(SnapsActivity.this, "", "Loading snaps...", true);

        _fetchPostsThread.start();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent snap = new Intent(SnapsActivity.this, SnapActivity.class);
        snap.putExtra("id", view.getId());
        startActivity(snap);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _postsAdapter = new SnapsAdapter(SnapsActivity.this, _listOfPosts);
                    _posts.setAdapter(_postsAdapter);
                    _dialog.cancel();
                    break;
            }
        }
    };
    
    private Thread _fetchPostsThread = new Thread(new Runnable() {
        
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            try {
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
                    snapProperties.put("large_small_url", json.getJSONObject("snaps").getString("large_url"));
                    
                    Snap snap = new Snap(snapProperties);
                    _listOfPosts.add(snap);
                }

                Bundle handlerData = new Bundle();
                handlerData.putInt(FETCH_STATUS, FETCH_COMPLETE);
                
                Message fetchingCompleteMessage = new Message();
                fetchingCompleteMessage.setData(handlerData);
                
                _handler.sendMessage(fetchingCompleteMessage);
            } catch (JSONException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error fetching snap from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error parsing snap date", e);
            }
        }
        
    });

}
