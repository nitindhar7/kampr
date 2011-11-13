package com.kampr.posts.comments;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.handlers.CommentsHandler;
import com.kampr.models.Comment;
import com.kampr.runnables.CommentsRunnable;

public class CommentsActivity extends ListActivity {
    
    private final int DEFAULT_POST_ID = -1;
    
    protected static SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private TextView _postTitleView;
    private TextView _postUsernameView;
    private TextView _postDateView;
    private ImageView _postUserIconView;
    
    private int _postId;
    private String _postTitle;
    private String _postName;
    private String _postCreatedAt;
    private Bitmap _postUserIcon;

    private ListView _comments;
    private ProgressDialog _dialog;
    private List<Comment> _listOfComments;
    private Map<String,Bitmap> _userIcons;
    private CommentsHandler _handler;
    private Thread _fetchCommentsThread;
    
    public CommentsActivity() {
        _listOfComments = new ArrayList<Comment>();
        _userIcons = new HashMap<String,Bitmap>();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments);
        trustAllHosts();

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

        _dialog = ProgressDialog.show(CommentsActivity.this, "", "Loading...", true);
        
        _handler = new CommentsHandler(this, _dialog, _comments, _listOfComments, _userIcons);
        _fetchCommentsThread = new Thread(new CommentsRunnable(this, _handler, _listOfComments, _userIcons, _postId));
        _fetchCommentsThread.start();
    }
    
    protected Bitmap getBitmapFromByteArray() {
        byte[] bmpBytes = getIntent().getByteArrayExtra("post_user_icon");
        return BitmapFactory.decodeByteArray(bmpBytes, 0, bmpBytes.length);
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
