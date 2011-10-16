package com.kampr.posts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Link;

public class LinkActivity extends PostActivity {
    
    private final String ACTIVITY_TAG = "LinkActivity";
    
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
        
        Link link = fetchLink(getIntent().getIntExtra("id", DEFAULT_POST_ID));
        
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
        linkUserIcon.setImageBitmap(fetchImageBitmap(link.getProperty("user_photos_thumb_url")));
    }
    
    protected Link fetchLink(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> linkProperties = new HashMap<String, String>();

        try {
            _postJSON = _forrst.postsShow(id);
            
            linkProperties.put("id", _postJSON.getString("id"));
            linkProperties.put("post_type", _postJSON.getString("post_type"));
            linkProperties.put("post_url", _postJSON.getString("post_url"));

            long linkDateInMillis = sdf.parse(_postJSON.getString("created_at")).getTime();
            String linkDate = DateUtils.formatDateTime(null, linkDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
            linkProperties.put("created_at", linkDate);
            
            linkProperties.put("name", _postJSON.getJSONObject("user").getString("name"));
            linkProperties.put("title", _postJSON.getString("title"));
            linkProperties.put("url", _postJSON.getString("url"));
            linkProperties.put("content", _postJSON.getString("content"));
            linkProperties.put("description", _postJSON.getString("description"));
            linkProperties.put("formatted_content", _postJSON.getString("formatted_content"));
            linkProperties.put("formatted_description", _postJSON.getString("formatted_description"));
            linkProperties.put("view_count", Integer.toString(_postJSON.getInt("view_count")));
            linkProperties.put("like_count", _postJSON.getString("like_count"));
            linkProperties.put("comment_count", _postJSON.getString("comment_count"));
            linkProperties.put("user_photos_thumb_url", _postJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
            linkProperties.put("tag_string", _postJSON.getString("tag_string"));

            return new Link(linkProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching link from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing link date", e);
        }
    }

}
