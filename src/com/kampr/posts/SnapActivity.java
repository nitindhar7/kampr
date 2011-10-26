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
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Snap;
import com.markupartist.android.widget.ActionBar;

public class SnapActivity extends PostActivity {
    
    private final String ACTIVITY_TAG = "SnapActivity";
    
    private TextView _snapTitle;
    private TextView _snapUrl;
    private TextView _snapUsername;
    private TextView _snapDate;
    private TextView _snapDescription;
    private ImageView _snapUserIcon;
    private ImageView _snapLargeUrl;
    private ScrollView _snapDesciptionScrollView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        setContentView(R.layout.snap);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        _dialog = ProgressDialog.show(SnapActivity.this, "", "Loading snap...", true);

        _fetchPostThread.start();
        
        _snapTitle = (TextView) findViewById(R.id.snap_title);
        _snapUrl = (TextView) findViewById(R.id.snap_url);
        _snapUsername = (TextView) findViewById(R.id.snap_user_name);
        _snapDate = (TextView) findViewById(R.id.snap_date);
        _snapDescription = (TextView) findViewById(R.id.snap_description);
        _snapUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
        _snapLargeUrl = (ImageView) findViewById(R.id.snap_large_url);
        _snapDesciptionScrollView = (ScrollView) findViewById(R.id.snap_description_scroll);
        _snapDesciptionScrollView.setVerticalScrollBarEnabled(false);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _snapTitle.setText(_post.getProperty("title"));
                    
                    String localSnapUrl = _post.getProperty("url");
                    if(localSnapUrl == null || localSnapUrl.length() == 0) {
                        _snapUrl.setVisibility(View.GONE);
                    }
                    else {
                        _snapUrl.setText(getTruncatedText(localSnapUrl, TRUNCATED_URL_LENGTH) + "...");
                    }
                    
                    _snapUsername.setText(_post.getProperty("name"));
                    _snapDate.setText(_post.getProperty("created_at"));
                    _snapDescription.setText(_post.getProperty("description"));
                    _snapUserIcon.setImageBitmap(fetchImageBitmap(_post.getProperty("user_photos_thumb_url")));
                    _snapLargeUrl.setImageBitmap(fetchImageBitmap(_post.getProperty("snaps_large_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };
    
    private Thread _fetchPostThread = new Thread(new Runnable() {
        
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> snapProperties = new HashMap<String, String>();

            try {
                _postJSON = _forrst.postsShow(getIntent().getIntExtra("id", DEFAULT_POST_ID));
                
                snapProperties.put("id", _postJSON.getString("id"));
                snapProperties.put("post_type", _postJSON.getString("post_type"));
                snapProperties.put("post_url", _postJSON.getString("post_url"));

                long snapDateInMillis = sdf.parse(_postJSON.getString("created_at")).getTime();
                String snapDate = DateUtils.formatDateTime(null, snapDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                snapProperties.put("created_at", snapDate);
                
                snapProperties.put("name", _postJSON.getJSONObject("user").getString("name"));
                snapProperties.put("title", _postJSON.getString("title"));
                snapProperties.put("url", _postJSON.getString("url"));
                snapProperties.put("content", _postJSON.getString("content"));
                snapProperties.put("description", _postJSON.getString("description"));
                snapProperties.put("formatted_content", _postJSON.getString("formatted_content"));
                snapProperties.put("formatted_description", _postJSON.getString("formatted_description"));
                snapProperties.put("view_count", Integer.toString(_postJSON.getInt("view_count")));
                snapProperties.put("like_count", _postJSON.getString("like_count"));
                snapProperties.put("comment_count", _postJSON.getString("comment_count"));
                snapProperties.put("user_photos_thumb_url", _postJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                snapProperties.put("snaps_large_url", _postJSON.getJSONObject("snaps").getString("large_url"));
                snapProperties.put("tag_string", _postJSON.getString("tag_string"));
                
                _post = new Snap(snapProperties);
                
                Bundle handlerData = new Bundle();
                handlerData.putInt(FETCH_STATUS, FETCH_COMPLETE);
                
                Message fetchingCompleteMessage = new Message();
                fetchingCompleteMessage.setData(handlerData);
                
                _handler.sendMessage(fetchingCompleteMessage);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching snap from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing snap date", e);
            }
        }
        
    });

}
