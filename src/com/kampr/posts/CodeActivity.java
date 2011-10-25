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
import com.kampr.models.Code;
import com.markupartist.android.widget.ActionBar;

public class CodeActivity extends PostActivity {
    
    private final String ACTIVITY_TAG = "CodeActivity";
    
    private TextView _codeTitle;
    private TextView _codeUrl;
    private TextView _codeUsername;
    private TextView _codeDate;
    private TextView _codeContent;
    private TextView _codeDescription;
    private ImageView _codeUserIcon;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        setContentView(R.layout.code);
        
        _actionBar = (ActionBar) findViewById(R.id.actionbar);
        _actionBar.setTitle("kampr");
        
        _dialog = ProgressDialog.show(CodeActivity.this, "", "Loading code...", true);

        _fetchPostThread.start();
        
        _codeTitle = (TextView) findViewById(R.id.snap_title);
        _codeUrl = (TextView) findViewById(R.id.snap_url);
        _codeUsername = (TextView) findViewById(R.id.snap_user_name);
        _codeDate = (TextView) findViewById(R.id.snap_date);
        _codeContent = (TextView) findViewById(R.id.snap_content);
        _codeDescription = (TextView) findViewById(R.id.snap_description);
        _codeUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
    }
    
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.getData().getInt(FETCH_STATUS)) {
                case FETCH_COMPLETE:
                    _codeTitle.setText(_post.getProperty("title"));
                    _codeUrl.setText(_post.getProperty("url"));
                    _codeUsername.setText(_post.getProperty("name"));
                    _codeDate.setText(_post.getProperty("created_at"));
                    _codeContent.setText(_post.getProperty("content"));
                    _codeDescription.setText(_post.getProperty("description"));
                    _codeUserIcon.setImageBitmap(fetchImageBitmap(_post.getProperty("user_photos_thumb_url")));
                    _dialog.cancel();
                    break;
            }
        }
    };
    
    private Thread _fetchPostThread = new Thread(new Runnable() {
        
        public void run() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Map<String, String> codeProperties = new HashMap<String, String>();

            try {
                _postJSON = _forrst.postsShow(getIntent().getIntExtra("id", DEFAULT_POST_ID));
                
                codeProperties.put("id", _postJSON.getString("id"));
                codeProperties.put("post_type", _postJSON.getString("post_type"));
                codeProperties.put("post_url", _postJSON.getString("post_url"));

                long codeDateInMillis = sdf.parse(_postJSON.getString("created_at")).getTime();
                String codeDate = DateUtils.formatDateTime(null, codeDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
                codeProperties.put("created_at", codeDate);
                
                codeProperties.put("name", _postJSON.getJSONObject("user").getString("name"));
                codeProperties.put("title", _postJSON.getString("title"));
                codeProperties.put("url", _postJSON.getString("url"));
                codeProperties.put("content", _postJSON.getString("content"));
                codeProperties.put("description", _postJSON.getString("description"));
                codeProperties.put("formatted_content", _postJSON.getString("formatted_content"));
                codeProperties.put("formatted_description", _postJSON.getString("formatted_description"));
                codeProperties.put("view_count", Integer.toString(_postJSON.getInt("view_count")));
                codeProperties.put("like_count", _postJSON.getString("like_count"));
                codeProperties.put("comment_count", _postJSON.getString("comment_count"));
                codeProperties.put("user_photos_thumb_url", _postJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
                codeProperties.put("tag_string", _postJSON.getString("tag_string"));
                
                _post = new Code(codeProperties);
                
                Bundle handlerData = new Bundle();
                handlerData.putInt(FETCH_STATUS, FETCH_COMPLETE);
                
                Message fetchingCompleteMessage = new Message();
                fetchingCompleteMessage.setData(handlerData);
                
                _handler.sendMessage(fetchingCompleteMessage);
            } catch (JSONException e) {
                throw new RuntimeException("Error fetching code from Forrst", e);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing code date", e);
            }
        }
        
    });

}
