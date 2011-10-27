package com.kampr.posts;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Link;
import com.markupartist.android.widget.ActionBar;

public class LinkActivity extends PostActivity {

    private TextView _linkTitle;
    private TextView _linkUrl;
    private TextView _linkUsername;
    private TextView _linkDate;
    private TextView _linkDescription;
    private ImageView _linkUserIcon;
    private ScrollView _linkDesciptionScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");

        _fetchPostThread.start();
        
        _linkTitle = (TextView) findViewById(R.id.link_title);
        _linkUrl = (TextView) findViewById(R.id.link_url);
        _linkUsername = (TextView) findViewById(R.id.link_user_name);
        _linkDate = (TextView) findViewById(R.id.link_date);
        _linkDescription = (TextView) findViewById(R.id.link_description);
        _linkUserIcon = (ImageView) findViewById(R.id.link_user_icon);
        _linkDesciptionScrollView = (ScrollView) findViewById(R.id.link_description_scroll);
        _linkDesciptionScrollView.setVerticalScrollBarEnabled(false);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _linkTitle.setText(_post.getProperty("title"));
                    _linkUrl.setText(getTruncatedText(_post.getProperty("url"), TRUNCATED_URL_LENGTH));
                    _linkUsername.setText(_post.getProperty("name"));
                    _linkDate.setText(_post.getProperty("created_at"));
                    _linkDescription.setText(_post.getProperty("description"));
                    _linkDescription.setVerticalScrollBarEnabled(false);
                    _linkUserIcon.setImageBitmap(fetchImageBitmap(_post.getProperty("user_photos_thumb_url")));
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
                
                _post = new Link(properties);
                
                notifyHandler(_handler);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching link from Forrst", e);
            }
        }
        
    });

}