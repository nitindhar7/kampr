package com.kampr.runnables;

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
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;
import com.kampr.util.TimeUtils;

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
                commentProperties.put("body", TextUtils.convertHtmlToText(commentJSON.getString("body")));
                commentProperties.put("created_at", TimeUtils.getPostDate(commentJSON.getString("created_at")));
                commentProperties.put("user_photos_thumb_url", commentJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));

                Comment comment = new Comment(commentProperties);
                _listOfComments.add(comment);

                _userIcons.put(comment.getProperty("id"), ImageUtils.fetchUserIcon(_context, comment.getProperty("user_photos_thumb_url"), R.drawable.forrst_default_25));
                
                if (commentJSON.has("replies")) {
                    JSONArray repliesJSONArray = (JSONArray) commentJSON.get("replies");
                    for(int replyCount = 0; replyCount < repliesJSONArray.length(); replyCount++) {
                        JSONObject replyJSON = repliesJSONArray.getJSONObject(replyCount);
                        
                        Map<String, String> replyProperties = new HashMap<String, String>();
                        replyProperties.put("id", replyJSON.getString("id"));
                        replyProperties.put("name", replyJSON.getJSONObject("user").getString("name"));
                        replyProperties.put("body", TextUtils.convertHtmlToText(replyJSON.getString("body")));
                        replyProperties.put("created_at", TimeUtils.getPostDate(replyJSON.getString("created_at")));
                        replyProperties.put("user_photos_thumb_url", replyJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
    
                        Comment replyComment = new Comment(replyProperties);
                        _listOfComments.add(replyComment);

                        _userIcons.put(replyComment.getProperty("id"), ImageUtils.fetchUserIcon(_context, replyComment.getProperty("user_photos_thumb_url"), R.drawable.forrst_default_25));
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching comment from Forrst", e);
        }
        
        notifyHandler();
    }
    
}
