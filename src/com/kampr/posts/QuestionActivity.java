package com.kampr.posts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Question;
import com.markupartist.android.widget.ActionBar;

public class QuestionActivity extends PostActivity {
    
    private final String ACTIVITY_TAG = "QuestionActivity";
    
    private TextView questionTitle;
    private TextView questionUrl;
    private TextView questionUsername;
    private TextView questionDate;
    private TextView questionContent;
    private ImageView questionUserIcon;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        setContentView(R.layout.question);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        Question question = fetchQuestion(getIntent().getIntExtra("id", DEFAULT_POST_ID));
        
        questionTitle = (TextView) findViewById(R.id.question_title);
        questionUrl = (TextView) findViewById(R.id.question_url);
        questionUsername = (TextView) findViewById(R.id.question_user_name);
        questionDate = (TextView) findViewById(R.id.question_date);
        questionContent = (TextView) findViewById(R.id.question_content);
        questionUserIcon = (ImageView) findViewById(R.id.question_user_icon);
        
        questionTitle.setText(question.getProperty("title"));
        questionUrl.setText(question.getProperty("url"));
        questionUsername.setText(question.getProperty("name"));
        questionDate.setText(question.getProperty("created_at"));
        questionContent.setText(question.getProperty("content"));
        questionUserIcon.setImageBitmap(fetchImageBitmap(question.getProperty("user_photos_thumb_url")));
    }

    protected Question fetchQuestion(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> questionProperties = new HashMap<String, String>();

        try {
            _postJSON = _forrst.postsShow(id);
            
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

            return new Question(questionProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching question from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing question date", e);
        }
    }

}
