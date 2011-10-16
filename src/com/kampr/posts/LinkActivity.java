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
import com.kampr.models.Link;

public class LinkActivity extends Activity {
    
    private final String ACTIVITY_TAG = "LinkActivity";
    
    protected static final int DEFAULT_LINK_ID = -1;
    
    private ForrstAPI _forrst;
    private JSONObject _linkJSON;
    
    private TextView linkTitle;
    private TextView linkUrl;
    private TextView linkUsername;
    private TextView linkDate;
    private TextView linkDescription;
    private ImageView linkUserIcon;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.link);
        
        _forrst = new ForrstAPIClient();
        
        Link link = fetchLink(getIntent().getIntExtra("id", DEFAULT_LINK_ID));
        
        linkTitle = (TextView) findViewById(R.id.link_title);
        linkUrl = (TextView) findViewById(R.id.link_url);
        linkUsername = (TextView) findViewById(R.id.link_user_name);
        linkDate = (TextView) findViewById(R.id.link_date);
        linkDescription = (TextView) findViewById(R.id.link_description);
        linkUserIcon = (ImageView) findViewById(R.id.link_user_icon);
        
        linkTitle.setText(link.getProperty("title"));
        linkUrl.setText(link.getProperty("url"));
        linkUsername.setText(link.getProperty("name"));
        linkDate.setText(link.getProperty("created_at"));
        linkDescription.setText(link.getProperty("description"));
        linkUserIcon.setImageBitmap(fetchLinkUserIcon(link.getProperty("user_photos_thumb_url")));
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
    
    protected Link fetchLink(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> linkProperties = new HashMap<String, String>();

        try {
            _linkJSON = _forrst.postsShow(id);
            
            linkProperties.put("id", _linkJSON.getString("id"));
            linkProperties.put("post_type", _linkJSON.getString("post_type"));
            linkProperties.put("post_url", _linkJSON.getString("post_url"));

            long linkDateInMillis = sdf.parse(_linkJSON.getString("created_at")).getTime();
            String linkDate = DateUtils.formatDateTime(null, linkDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
            linkProperties.put("created_at", linkDate);
            
            linkProperties.put("name", _linkJSON.getJSONObject("user").getString("name"));
            linkProperties.put("title", _linkJSON.getString("title"));
            linkProperties.put("url", _linkJSON.getString("url"));
            linkProperties.put("content", _linkJSON.getString("content"));
            linkProperties.put("description", _linkJSON.getString("description"));
            linkProperties.put("formatted_content", _linkJSON.getString("formatted_content"));
            linkProperties.put("formatted_description", _linkJSON.getString("formatted_description"));
            linkProperties.put("view_count", Integer.toString(_linkJSON.getInt("view_count")));
            linkProperties.put("like_count", _linkJSON.getString("like_count"));
            linkProperties.put("comment_count", _linkJSON.getString("comment_count"));
            linkProperties.put("user_photos_thumb_url", _linkJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
            linkProperties.put("tag_string", _linkJSON.getString("tag_string"));

            return new Link(linkProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching link from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing link date", e);
        }
    }
    
    protected Bitmap fetchLinkUserIcon(String uri) {
        Bitmap linkUserIconData = null;
        try {
            InputStream is = (InputStream) new URL(uri).getContent();
            linkUserIconData = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error processing link url", e);
        } catch (IOException e) {
            throw new RuntimeException("Error fetch data from stream", e);
        }
        return linkUserIconData;
    }

}
