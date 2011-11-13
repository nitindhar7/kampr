package com.kampr.posts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.R;
import com.kampr.models.PropertyContainer;
import com.kampr.posts.comments.CommentsActivity;

public class PostActivity extends Activity implements OnClickListener {

    protected static final String FETCH_STATUS = "fetch_status";
    protected static final int FETCH_COMPLETE = 1;
    protected static final int DEFAULT_POST_ID = -1;

    protected final int TRUNCATED_URL_LENGTH = 35;
    
    protected final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected ForrstAPI _forrst;
    protected JSONObject _postJSON;
    protected ProgressDialog _dialog;
    protected PropertyContainer _post;
    protected Thread _fetchPostThread;
    protected int _postId;
    protected Bitmap _userIconBitmap;
    
    protected TextView _postLikes;
    protected TextView _postViews;
    protected TextView _postComments;
    
    public PostActivity() {
        _forrst = new ForrstAPIClient();
        _post = new PropertyContainer();
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _dialog = ProgressDialog.show(PostActivity.this, "", "Loading...", true);
        
        _postLikes = (TextView) findViewById(R.id.post_likes);
        _postViews = (TextView) findViewById(R.id.post_views);
        _postComments = (TextView) findViewById(R.id.post_comments);
        _postComments.setOnClickListener(this);
        
        _postId = getIntent().getIntExtra("id", DEFAULT_POST_ID);
    }
    
    @Override
    public void onClick(View v) {
        Intent comments = new Intent(PostActivity.this, CommentsActivity.class);
        comments.putExtra("post_id", _postId);
        comments.putExtra("post_title", _post.getProperty("title"));
        comments.putExtra("post_name", _post.getProperty("name"));
        comments.putExtra("post_created_at", _post.getProperty("created_at"));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        _userIconBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        comments.putExtra("post_user_icon", stream.toByteArray());
        startActivity(comments);
    }
    
    protected Bitmap fetchImageBitmap(String uri) {
        Bitmap imageData = null;
        try {
            InputStream is = (InputStream) new URL(uri).getContent();
            imageData = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error processing url", e);
        } catch (IOException e) {
            throw new RuntimeException("Error fetching data from stream", e);
        }
        return imageData;
    }
    
    protected String getTruncatedText(String text, int maxLength) {
        if(text == null) {
            return null;
        }
        else if(text.length() >= maxLength) {
            return text.substring(0, maxLength) + "...";
        }
        else {
            return text;
        }
    }
    
    protected String getPostDate() {
        try {
            long inMillis = _dateFormat.parse(_postJSON.getString("created_at")).getTime();
            return DateUtils.formatDateTime(null, inMillis, DateUtils.FORMAT_ABBREV_ALL);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing post date", e);
        } catch (JSONException e) {
            throw new RuntimeException("Error retrieving date from json", e);
        }
    }
    
    protected Bitmap fetchUserIcon(String uri) {
        try {
            InputStream is = (InputStream) new URL(uri).getContent();
            return getBitmapFromStream(is);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: malformed URI", e);
        } catch (IOException e) {
            throw new RuntimeException("Error: could not read from stream", e);
        }
    }
    
    private Bitmap getBitmapFromStream(InputStream is) {
        Bitmap bmp = BitmapFactory.decodeStream(is);
        if(bmp == null) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.forrst_default_25);
        }
        else {
            return bmp;
        }
    }

}