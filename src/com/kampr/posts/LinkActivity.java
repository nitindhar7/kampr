package com.kampr.posts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Link;
import com.markupartist.android.widget.ActionBar;

public class LinkActivity extends PostActivity {
    
    private final String ACTIVITY_TAG = "LinkActivity";
    
    private TextView _linkTitle;
    private TextView _linkUrl;
    private TextView _linkUsername;
    private TextView _linkDate;
    private TextView _linkDescription;
    private ImageView _linkUserIcon;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        setContentView(R.layout.link);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        _dialog = ProgressDialog.show(LinkActivity.this, "", "Loading link...", true);

        _fetchPostThread.start();
        
        _linkTitle = (TextView) findViewById(R.id.link_title);
        _linkUrl = (TextView) findViewById(R.id.link_url);
        _linkUsername = (TextView) findViewById(R.id.link_user_name);
        _linkDate = (TextView) findViewById(R.id.link_date);
        _linkDescription = (TextView) findViewById(R.id.link_description);
        _linkUserIcon = (ImageView) findViewById(R.id.link_user_icon);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _linkTitle.setText(_post.getProperty("title"));
                    _linkUrl.setText(_post.getProperty("url"));
                    _linkUsername.setText(_post.getProperty("name"));
                    _linkDate.setText(_post.getProperty("created_at"));
                    _linkDescription.setText(_post.getProperty("description"));
                    _linkDescription.setVerticalScrollBarEnabled(false);
                    _linkUserIcon.setImageBitmap(fetchImageBitmap(_post.getProperty("user_photos_thumb_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };
    
    private Thread _fetchPostThread = new Thread(new Runnable() {
        
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> linkProperties = new HashMap<String, String>();

            try {
                _postJSON = _forrst.postsShow(getIntent().getIntExtra("id", DEFAULT_POST_ID));
                
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
                
                _post = new Link(linkProperties);
                
                Bundle handlerData = new Bundle();
                handlerData.putInt(FETCH_STATUS, FETCH_COMPLETE);
                
                Message fetchingCompleteMessage = new Message();
                fetchingCompleteMessage.setData(handlerData);
                
                _handler.sendMessage(fetchingCompleteMessage);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching link from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing link date", e);
            }
        }
        
    });

}
