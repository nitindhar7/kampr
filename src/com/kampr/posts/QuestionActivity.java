package com.kampr.posts;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.R;
import com.kampr.models.Question;

public class QuestionActivity extends Activity {
    
    private final String ACTIVITY_TAG = "QuestionActivity";
    
    protected static final int DEFAULT_QUESTION_ID = -1;
    
    private ForrstAPI _forrst;
    private JSONObject _questionJSON;
    
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.question);
        
        _forrst = new ForrstAPIClient();
        
        Question question = fetchQuestion(getIntent().getIntExtra("id", DEFAULT_QUESTION_ID));
        
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
        questionUserIcon.setImageBitmap(fetchQuestionUserIcon(question.getProperty("user_photos_thumb_url")));
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_TAG, "onStart");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_TAG, "onPause");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_TAG, "onStop");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_TAG, "onDestroy");
    }
    
    protected Question fetchQuestion(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> questionProperties = new HashMap<String, String>();

        try {
            _questionJSON = _forrst.postsShow(id);
            
            questionProperties.put("id", _questionJSON.getString("id"));
            questionProperties.put("post_type", _questionJSON.getString("post_type"));
            questionProperties.put("post_url", _questionJSON.getString("post_url"));

            long questionDateInMillis = sdf.parse(_questionJSON.getString("created_at")).getTime();
            String questionDate = DateUtils.formatDateTime(null, questionDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
            questionProperties.put("created_at", questionDate);
            
            questionProperties.put("name", _questionJSON.getJSONObject("user").getString("name"));
            questionProperties.put("title", _questionJSON.getString("title"));
            questionProperties.put("url", _questionJSON.getString("url"));
            questionProperties.put("content", _questionJSON.getString("content"));
            questionProperties.put("description", _questionJSON.getString("description"));
            questionProperties.put("formatted_content", _questionJSON.getString("formatted_content"));
            questionProperties.put("formatted_description", _questionJSON.getString("formatted_description"));
            questionProperties.put("view_count", Integer.toString(_questionJSON.getInt("view_count")));
            questionProperties.put("like_count", _questionJSON.getString("like_count"));
            questionProperties.put("comment_count", _questionJSON.getString("comment_count"));
            questionProperties.put("user_photos_thumb_url", _questionJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
            questionProperties.put("tag_string", _questionJSON.getString("tag_string"));

            return new Question(questionProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching question from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing question date", e);
        }
    }
    
    protected Bitmap fetchQuestionUserIcon(String uri) {
        Bitmap questionUserIconData = null;
        try {
            InputStream is = (InputStream) new URL(uri).getContent();
            questionUserIconData = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error processing question url", e);
        } catch (IOException e) {
            throw new RuntimeException("Error fetch data from stream", e);
        }
        return questionUserIconData;
    }

}
