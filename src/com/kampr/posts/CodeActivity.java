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
import com.kampr.models.Code;

public class CodeActivity extends PostActivity {
    
private final String ACTIVITY_TAG = "CodeActivity";
    
    private TextView codeTitle;
    private TextView codeUrl;
    private TextView codeUsername;
    private TextView codeDate;
    private TextView codeContent;
    private TextView codeDescription;
    private ImageView codeUserIcon;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.code);
        
        Code code = fetchCode(getIntent().getIntExtra("id", DEFAULT_POST_ID));
        
        codeTitle = (TextView) findViewById(R.id.snap_title);
        codeUrl = (TextView) findViewById(R.id.snap_url);
        codeUsername = (TextView) findViewById(R.id.snap_user_name);
        codeDate = (TextView) findViewById(R.id.snap_date);
        codeContent = (TextView) findViewById(R.id.snap_content);
        codeDescription = (TextView) findViewById(R.id.snap_description);
        codeUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
        
        codeTitle.setText(code.getProperty("title"));
        codeUrl.setText(code.getProperty("url"));
        codeUsername.setText(code.getProperty("name"));
        codeDate.setText(code.getProperty("created_at"));
        codeContent.setText(code.getProperty("content"));
        codeDescription.setText(code.getProperty("description"));
        codeUserIcon.setImageBitmap(fetchImageBitmap(code.getProperty("user_photos_thumb_url")));
    }
    
    protected Code fetchCode(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> codeProperties = new HashMap<String, String>();

        try {
            _postJSON = _forrst.postsShow(id);
            
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

            return new Code(codeProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching code from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing code date", e);
        }
    }

}
