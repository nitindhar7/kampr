package com.kampr.runnables;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.kampr.KamprActivity;
import com.kampr.R;
import com.kampr.models.Comment;
import com.kampr.util.ImageUtils;
import com.kampr.util.TextUtils;
import com.kampr.util.TimeUtils;

public class CommentsRunnable extends AbstractRunnable {

    protected List<Comment> _listOfComments;
    protected SharedPreferences _settings;
    protected int _postId;
    
    public CommentsRunnable(Context context, Handler handler, List<Comment> listOfComments, int postId) {
        super(context, handler);
        _listOfComments = listOfComments;
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
                
                Comment comment = new Comment();
                comment.setId(commentJSON.getInt("id"));
                comment.setUserName(commentJSON.getJSONObject("user").getString("name"));
                comment.setBody(TextUtils.convertHtmlToText(commentJSON.getString("body")));
                comment.setCreatedAt(TimeUtils.getPostDate(commentJSON.getString("created_at")));
                comment.setUserIcon(ImageUtils.fetchUserIcon(_context, commentJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"), R.drawable.forrst_default_25));

                _listOfComments.add(comment);
                
                if (commentJSON.has("replies")) {
                    JSONArray repliesJSONArray = (JSONArray) commentJSON.get("replies");
                    for(int replyCount = 0; replyCount < repliesJSONArray.length(); replyCount++) {
                        JSONObject replyJSON = repliesJSONArray.getJSONObject(replyCount);

                        Comment replyComment = new Comment();
                        replyComment.setId(replyJSON.getInt("id"));
                        replyComment.setUserName(replyJSON.getJSONObject("user").getString("name"));
                        replyComment.setBody(TextUtils.convertHtmlToText(replyJSON.getString("body")));
                        replyComment.setCreatedAt(TimeUtils.getPostDate(replyJSON.getString("created_at")));
                        replyComment.setUserIcon(ImageUtils.fetchUserIcon(_context, replyJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"), R.drawable.forrst_default_25));

                        _listOfComments.add(replyComment);
                    }
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching comment from Forrst", e);
        }
        
        notifyHandler();
    }
    
}
