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
import com.kampr.models.Question;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class QuestionActivity extends PostActivity {

    private TextView _questionTitle;
    private TextView _questionUsername;
    private TextView _questionDate;
    private TextView _questionContent;
    private ImageView _questionUserIcon;
    private ScrollView _questionContentScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        Action settingsAction = new IntentAction(this, new Intent(this, SettingsActivity.class), R.drawable.ic_actionbar_settings);
        _actionBar.addAction(settingsAction);

        _fetchPostThread.start();
        
        _questionTitle = (TextView) findViewById(R.id.question_title);
        _questionUsername = (TextView) findViewById(R.id.question_user_name);
        _questionDate = (TextView) findViewById(R.id.question_date);
        _questionUserIcon = (ImageView) findViewById(R.id.question_user_icon);
        _questionContent = (TextView) findViewById(R.id.question_content);
        _questionContentScrollView = (ScrollView) findViewById(R.id.question_content_scroll);
        _questionContentScrollView.setVerticalScrollBarEnabled(false);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _questionTitle.setText(_post.getProperty("title"));
                    _questionUsername.setText(_post.getProperty("name"));
                    _questionDate.setText(_post.getProperty("created_at"));
                    _questionContent.setText(_post.getProperty("content"));
                    _questionUserIcon.setImageBitmap(fetchUserIcon(_post.getProperty("user_photos_thumb_url")));
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
                
                _post = new Question(properties);
                
                notifyHandler(_handler);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching question from Forrst", e);
            }
        }
        
    });

}