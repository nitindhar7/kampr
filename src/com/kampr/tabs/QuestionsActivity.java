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
import com.kampr.adapters.QuestionsAdapter;
import com.kampr.models.Question;

public class QuestionsActivity extends PostsListActivity {

    private final String ACTIVITY_TAG = "QuestionsActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        trustAllHosts();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            List<Question> listOfQuestions = new ArrayList<Question>();
            ForrstAPI forrst = new ForrstAPIClient();
            
            JSONObject postsJSON = forrst.postsList("question", null);
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
                listOfQuestions.add(question);
            }
            
            ListView questions = getListView();
            questions.setVerticalFadingEdgeEnabled(false);
            QuestionsAdapter questionsAdapter = new QuestionsAdapter(QuestionsActivity.this, listOfQuestions);
            questions.setAdapter(questionsAdapter);
        } catch (JSONException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error fetching question from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException(ACTIVITY_TAG + ": Error parsing question date", e);
        }
    }

}