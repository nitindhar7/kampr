package com.kampr.posts.comments;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.KamprActivity;
import com.kampr.R;
import com.kampr.adapters.CommentsAdapter;
import com.kampr.models.Comment;
import com.kampr.models.PropertyContainer;

public class CommentsActivity extends ListActivity {
    
    private final int DEFAULT_POST_ID = -1;
    
    private TextView _postTitleView;
    private TextView _postUsernameView;
    private TextView _postDateView;
    private ImageView _postUserIconView;
    
    private int _postId;
    private String _postTitle;
    private String _postName;
    private String _postCreatedAt;
    private Bitmap _postUserIcon;

    private ForrstAPI _forrst;
    private ListView _comments;
    private ProgressDialog _dialog;
    private List<Comment> _listOfComments;
    private Map<String,Bitmap> _userIcons;
    private CommentsAdapter _commentsAdapter;
    
    public CommentsActivity() {
        _forrst = new ForrstAPIClient();
        _listOfComments = new ArrayList<Comment>();
        _userIcons = new HashMap<String,Bitmap>();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        trustAllHosts();
        
        SharedPreferences settings = getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0);

        _postId = getIntent().getIntExtra("post_id", DEFAULT_POST_ID);
        _postTitle = getIntent().getStringExtra("post_title");
        _postName = getIntent().getStringExtra("post_name");
        _postCreatedAt = getIntent().getStringExtra("post_created_at");
        _postUserIcon = getBitmapFromByteArray();
        
        _postTitleView = (TextView) findViewById(R.id.post_title);
        _postUsernameView = (TextView) findViewById(R.id.post_user_name);
        _postDateView = (TextView) findViewById(R.id.post_date);
        _postUserIconView = (ImageView) findViewById(R.id.post_user_icon);
        
        _postTitleView.setText(_postTitle);
        _postUsernameView.setText(_postName);
        _postDateView.setText(_postCreatedAt);
        _postUserIconView.setImageBitmap(_postUserIcon);
        
        _comments = getListView();
        _comments.setVerticalScrollBarEnabled(false);
        _comments.setVerticalFadingEdgeEnabled(false);
        _comments.setDivider(getResources().getDrawable(R.color.comment_item_divider));
        _comments.setDividerHeight(1);

//        _commentsAdapter = new CommentsAdapter(this, _listOfComments);
        
        _dialog = ProgressDialog.show(CommentsActivity.this, "", "Loading...", true);

        try {
            JSONObject commentsJSON = _forrst.postComments(settings.getString("login_token", null), _postId);
            
            JSONArray commentsJSONArray = (JSONArray) commentsJSON.get("comments");
            for(int commentCount = 0; commentCount < commentsJSONArray.length(); commentCount++) {
                JSONObject commentJSON = commentsJSONArray.getJSONObject(commentCount);
                
                Map<String, String> properties = new HashMap<String, String>();
                properties.put("id", commentJSON.getString("id"));
                properties.put("name", commentJSON.getJSONObject("user").getString("name"));
                properties.put("body", commentJSON.getString("body"));
                properties.put("created_at", commentJSON.getString("created_id"));

                Comment comment = new Comment(properties);
                _listOfComments.add(comment);

                fetchUserIcon(comment);
                
                JSONArray repliesJSONArray = (JSONArray) commentJSON.get("replies");
                for(int replyCount = 0; replyCount < commentsJSONArray.length(); replyCount++) {
                    JSONObject replyJSON = repliesJSONArray.getJSONObject(replyCount);
                    
                    Map<String, String> replyProperties = new HashMap<String, String>();
                    replyProperties.put("id", replyJSON.getString("id"));
                    replyProperties.put("name", replyJSON.getJSONObject("user").getString("name"));
                    replyProperties.put("body", replyJSON.getString("body"));
                    replyProperties.put("created_at", replyJSON.getString("created_id"));

                    Comment replyComment = new Comment(properties);
                    _listOfComments.add(replyComment);

                    fetchUserIcon(replyComment);
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching comment from Forrst", e);
        }

//        _postsAdapter = new PostsAdapter<T>(_context, _listOfPosts, _userIcons);
//        _posts.setAdapter(_postsAdapter);
        _dialog.cancel();
    }
    
    protected Bitmap getBitmapFromByteArray() {
        byte[] bmpBytes = getIntent().getByteArrayExtra("post_user_icon");
        return BitmapFactory.decodeByteArray(bmpBytes, 0, bmpBytes.length);
    }
    
    protected void fetchUserIcon(Comment comment) {
        try {
            InputStream is = (InputStream) new URL(comment.getProperty("user_photos_thumb_url")).getContent();
            _userIcons.put(comment.getProperty("id"), getBitmapFromStream(is, this, R.drawable.forrst_default_25));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: malformed URI", e);
        } catch (IOException e) {
            throw new RuntimeException("Error: could not read from stream", e);
        }
    }
    
    protected Bitmap getBitmapFromStream(InputStream is, Context context, int drawableId) {
        Bitmap bmp = BitmapFactory.decodeStream(is);
        if(bmp == null) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.forrst_default_25);
        }
        else {
            return bmp;
        }
    }
    
    /**
     * Trust every server - dont check for any certificate
     * 1. Create a trust manager that does not validate certificate chains
     * 2. Install the all-trusting trust manager
     */
    protected static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
        }};

        SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error installing all-trusting trust manager: algorithm not found", e);
        } catch (KeyManagementException e) {
            throw new RuntimeException("Error installing all-trusting trust manager: problems managing key", e);
        }
    }

}
