package com.kampr.posts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Question;
import com.markupartist.android.widget.ActionBar;

public class QuestionActivity extends PostActivity {
    
    private final String ACTIVITY_TAG = "QuestionActivity";
    
    private TextView _questionTitle;
    private TextView _questionUsername;
    private TextView _questionDate;
    private TextView _questionContent;
    private ImageView _questionUserIcon;
    private ScrollView _questionContentScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        setContentView(R.layout.question);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        _dialog = ProgressDialog.show(QuestionActivity.this, "", "Loading question...", true);

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
                    _questionUserIcon.setImageBitmap(fetchImageBitmap(_post.getProperty("user_photos_thumb_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };
    
    private Thread _fetchPostThread = new Thread(new Runnable() {
        
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> questionProperties = new HashMap<String, String>();

            try {
                _postJSON = _forrst.postsShow(getIntent().getIntExtra("id", DEFAULT_POST_ID));
                
                questionProperties.put("id", _postJSON.getString("id"));
                questionProperties.put("post_type", _postJSON.getString("post_type"));
                questionProperties.put("post_url", _postJSON.getString("post_url"));

                long questionDateInMillis = sdf.parse(_postJSON.getString("created_at")).getTime();
                String questionDate = DateUtils.formatDateTime(null, questionDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                questionProperties.put("created_at", questionDate);
                
                questionProperties.put("name", _postJSON.getJSONObject("user").getString("name"));
                questionProperties.put("title", _postJSON.getString("title"));
                questionProperties.put("url", _postJSON.getString("url"));
                questionProperties.put("content", _postJSON.getString("content"));
                questionProperties.put("description", _postJSON.getString("description"));
                questionProperties.put("formatted_content", _postJSON.getString("formatted_content"));
                questionProperties.put("formatted_description", _postJSON.getString("formatted_description"));
                questionProperties.put("view_count", Integer.toString(_postJSON.getInt("view_count")));
                questionProperties.put("like_count", _postJSON.getString("like_count"));
                questionProperties.put("comment_count", _postJSON.getString("comment_count"));
                questionProperties.put("user_photos_thumb_url", _postJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                questionProperties.put("tag_string", _postJSON.getString("tag_string"));
                
                _post = new Question(questionProperties);
                
                Bundle handlerData = new Bundle();
                handlerData.putInt(FETCH_STATUS, FETCH_COMPLETE);
                
                Message fetchingCompleteMessage = new Message();
                fetchingCompleteMessage.setData(handlerData);
                
                _handler.sendMessage(fetchingCompleteMessage);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching question from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing question date", e);
            }
        }
        
    });

}
