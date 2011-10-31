package com.kampr.posts;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.SettingsActivity;
import com.kampr.models.Code;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class CodeActivity extends PostActivity {

    private TextView _codeTitle;
    private TextView _codeUsername;
    private TextView _codeDate;
    private TextView _codeContent;
    private TextView _codeDescription;
    private ImageView _codeUserIcon;
    private ScrollView _codeScroll;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        Action settingsAction = new IntentAction(this, new Intent(this, SettingsActivity.class), R.drawable.ic_actionbar_settings);
        _actionBar.addAction(settingsAction);

        _fetchPostThread.start();
        
        _codeTitle = (TextView) findViewById(R.id.code_title);
        _codeUsername = (TextView) findViewById(R.id.code_user_name);
        _codeDate = (TextView) findViewById(R.id.code_date);
        _codeContent = (TextView) findViewById(R.id.code_content);
        _codeDescription = (TextView) findViewById(R.id.code_description);
        _codeUserIcon = (ImageView) findViewById(R.id.code_user_icon);
        _codeScroll = (ScrollView) findViewById(R.id.code_scroll);
        _codeScroll.setVerticalScrollBarEnabled(false);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _codeTitle.setText(_post.getProperty("title"));
                    _codeUsername.setText(_post.getProperty("name"));
                    _codeDate.setText(_post.getProperty("created_at"));
                    _codeContent.setText(_post.getProperty("content"));
                    _codeDescription.setText(_post.getProperty("description"));
                    _codeUserIcon.setImageBitmap(fetchUserIcon(_post.getProperty("user_photos_thumb_url")));
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
                properties.put("tag_string", _postJSON.getString("tag_string"));
                
                _post = new Code(properties);
                
                notifyHandler(_handler);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching code from Forrst", e);
            }
        }
        
    });

}