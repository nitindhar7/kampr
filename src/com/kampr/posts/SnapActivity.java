package com.kampr.posts;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.SettingsActivity;
import com.kampr.models.Snap;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class SnapActivity extends PostActivity {

    private TextView _snapTitle;
    private TextView _snapUrl;
    private TextView _snapUsername;
    private TextView _snapDate;
    private TextView _snapDescription;
    private ImageView _snapUserIcon;
    private ImageView _snapLargeUrl;
    private ScrollView _snapDesciptionScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.snap);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        Action settingsAction = new IntentAction(this, new Intent(this, SettingsActivity.class), R.drawable.ic_actionbar_settings);
        _actionBar.addAction(settingsAction);

        _fetchPostThread.start();
        
        _snapTitle = (TextView) findViewById(R.id.snap_title);
        _snapUrl = (TextView) findViewById(R.id.snap_url);
        _snapUsername = (TextView) findViewById(R.id.snap_user_name);
        _snapDate = (TextView) findViewById(R.id.snap_date);
        _snapDescription = (TextView) findViewById(R.id.snap_description);
        _snapUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
        _snapLargeUrl = (ImageView) findViewById(R.id.snap_large_url);
        _snapDesciptionScrollView = (ScrollView) findViewById(R.id.snap_description_scroll);
        _snapDesciptionScrollView.setVerticalScrollBarEnabled(false);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _snapTitle.setText(_post.getProperty("title"));
                    
                    String localSnapUrl = _post.getProperty("url");
                    if(localSnapUrl == null || localSnapUrl.length() == 0) {
                        _snapUrl.setVisibility(View.GONE);
                    }
                    else {
                        _snapUrl.setText(getTruncatedText(localSnapUrl, TRUNCATED_URL_LENGTH));
                    }
                    
                    _snapUsername.setText(_post.getProperty("name"));
                    _snapDate.setText(_post.getProperty("created_at"));
                    _snapDescription.setText(_post.getProperty("description"));
                    _snapUserIcon.setImageBitmap(fetchImageBitmap(_post.getProperty("user_photos_thumb_url")));
                    _snapLargeUrl.setImageBitmap(fetchImageBitmap(_post.getProperty("snaps_large_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };
    
    private Thread _fetchPostThread = new Thread(new Runnable() {
        
        public void run() {
            try {
                _postJSON = _forrst.postsShow(getIntent().getIntExtra("id", DEFAULT_POST_ID));

                Map<String, String> properties = new HashMap<String, String>();
                properties.put("id", _postJSON.getString("id"));
                properties.put("post_type", _postJSON.getString("post_type"));
                properties.put("post_url", _postJSON.getString("post_url"));
                properties.put("created_at", getPostDate());
                properties.put("name", _postJSON.getJSONObject("user").getString("name"));
                properties.put("title", _postJSON.getString("title"));
                properties.put("url", _postJSON.getString("url"));
                properties.put("content", _postJSON.getString("content"));
                properties.put("description", _postJSON.getString("description"));
                properties.put("formatted_content", _postJSON.getString("formatted_content"));
                properties.put("formatted_description", _postJSON.getString("formatted_description"));
                properties.put("view_count", Integer.toString(_postJSON.getInt("view_count")));
                properties.put("like_count", _postJSON.getString("like_count"));
                properties.put("comment_count", _postJSON.getString("comment_count"));
                properties.put("user_photos_thumb_url", _postJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                properties.put("snaps_large_url", _postJSON.getJSONObject("snaps").getString("large_url"));
                properties.put("tag_string", _postJSON.getString("tag_string"));
                
                _post = new Snap(properties);
                
                notifyHandler(_handler);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching snap from Forrst", e);
            }
        }
        
    });

}