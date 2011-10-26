package com.kampr.tabs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.models.Question;
import com.kampr.posts.QuestionActivity;

public class QuestionsActivity extends PostsListActivity<Question> {

    private final String ACTIVITY_TAG = "QuestionsActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _dialog = ProgressDialog.show(QuestionsActivity.this, "", "Loading questions...", true);
        _fetchPostsThread.start();
    }
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent question = new Intent(QuestionsActivity.this, QuestionActivity.class);
        question.putExtra("id", view.getId());
        startActivity(question);
    }
    
    private Thread _fetchPostsThread = new Thread(new Runnable() {
        
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            try {
                JSONObject postsJSON = _forrst.postsList("question", null);
                JSONArray postsJSONArray = (JSONArray) postsJSON.get("posts");
                
                for(int count = 0; count < postsJSONArray.length(); count++) {
                    Map<String, String> questionProperties = new HashMap<String, String>();
                    JSONObject json = postsJSONArray.getJSONObject(count);
                    
                    questionProperties.put("id", json.getString("id"));
                    questionProperties.put("post_type", json.getString("post_type"));
                    questionProperties.put("post_url", json.getString("post_url"));

                    long questionDateInMillis = sdf.parse(json.getString("created_at")).getTime();
                    String questionDate = DateUtils.formatDateTime(null, questionDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                    questionProperties.put("created_at", questionDate);
                    
                    questionProperties.put("name", json.getJSONObject("user").getString("name"));
                    questionProperties.put("title", json.getString("title"));
                    questionProperties.put("url", json.getString("url"));
                    questionProperties.put("content", json.getString("content"));
                    questionProperties.put("description", json.getString("description"));
                    questionProperties.put("formatted_content", json.getString("formatted_content"));
                    questionProperties.put("formatted_description", json.getString("formatted_description"));
                    questionProperties.put("view_count", Integer.toString(json.getInt("view_count")));
                    questionProperties.put("like_count", json.getString("like_count"));
                    questionProperties.put("comment_count", json.getString("comment_count"));
                    questionProperties.put("user_photos_thumb_url", json.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                    questionProperties.put("tag_string", json.getString("tag_string"));
                    
                    Question question = new Question(questionProperties);
                    _listOfPosts.add(question);
                }

                notifyHandler();
            } catch (JSONException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error fetching question from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException(ACTIVITY_TAG + ": Error parsing question date", e);
            }
        }
        
    });

}