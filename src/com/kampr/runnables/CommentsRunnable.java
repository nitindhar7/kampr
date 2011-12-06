package com.kampr.runnables;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;

import com.kampr.KamprActivity;
import com.kampr.R;
import com.kampr.models.Comment;
import com.kampr.util.KamprUtils;

public class CommentsRunnable extends AbstractRunnable {

    protected Context _context;
    protected List<Comment> _listOfComments;
    protected Map<String,Bitmap> _userIcons;
    protected SharedPreferences _settings;
    protected int _postId;
    
    public CommentsRunnable(Context context, Handler handler, List<Comment> listOfComments, Map<String, Bitmap> userIcons, int postId) {
        super(handler);
        _context = context;
        _listOfComments = listOfComments;
        _userIcons = userIcons;
        _settings = _context.getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0);
        _postId = postId;
    }

    @Override
    public void run() {
        try {
            JSONObject commentsJSON = _forrst.postComments(_settings.getString("login_token", null), _postId);
            
            JSONArray commentsJSONArray = (JSONArray) commentsJSON.get("comments");
            for(int commentCount = 0; commentCount < commentsJSONArray.length(); commentCount++) {
                JSONObject commentJSON = commentsJSONArray.getJSONObject(commentCount);
                
                Map<String, String> commentProperties = new HashMap<String, String>();
                commentProperties.put("id", commentJSON.getString("id"));
                commentProperties.put("name", commentJSON.getJSONObject("user").getString("name"));
                commentProperties.put("body", commentJSON.getString("body"));
                commentProperties.put("created_at", getPostDate(commentJSON));
                commentProperties.put("user_photos_thumb_url", commentJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));

                Comment comment = new Comment(commentProperties);
                _listOfComments.add(comment);

                fetchUserIcon(comment);
                
                if (commentJSON.has("replies")) {
                    JSONArray repliesJSONArray = (JSONArray) commentJSON.get("replies");
                    for(int replyCount = 0; replyCount < repliesJSONArray.length(); replyCount++) {
                        JSONObject replyJSON = repliesJSONArray.getJSONObject(replyCount);
                        
                        Map<String, String> replyProperties = new HashMap<String, String>();
                        replyProperties.put("id", replyJSON.getString("id"));
                        replyProperties.put("name", replyJSON.getJSONObject("user").getString("name"));
                        String body = KamprUtils.cleanseText(replyJSON.getString("body"));
                        body = KamprUtils.stripHtmlTag(body, "<p>");
                        body = KamprUtils.stripHtmlTag(body, "</p>");
                        replyProperties.put("body", body);
                        replyProperties.put("created_at", getPostDate(replyJSON));
                        replyProperties.put("user_photos_thumb_url", replyJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
    
                        Comment replyComment = new Comment(replyProperties);
                        _listOfComments.add(replyComment);
    
                        fetchUserIcon(replyComment);
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching comment from Forrst", e);
        }
        
        notifyHandler();
    }
    
    protected void fetchUserIcon(Comment comment) {
        try {
            InputStream is = (InputStream) new URL(comment.getProperty("user_photos_thumb_url")).getContent();
            _userIcons.put(comment.getProperty("id"), getBitmapFromStream(is, _context, R.drawable.forrst_default_25));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: malformed URI", e);
        } catch (IOException e) {
            throw new RuntimeException("Error: could not read from stream", e);
        }
    }
    
}
