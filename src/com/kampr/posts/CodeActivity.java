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
import com.kampr.models.Code;

public class CodeActivity extends Activity {
    
private final String ACTIVITY_TAG = "CodeActivity";
    
    protected static final int DEFAULT_CODE_ID = -1;
    
    private ForrstAPI _forrst;
    private JSONObject _codeJSON;
    
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
        
        _forrst = new ForrstAPIClient();
        
        Code code = fetchCode(getIntent().getIntExtra("id", DEFAULT_CODE_ID));
        
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
        codeUserIcon.setImageBitmap(fetchCodeUserIcon(code.getProperty("user_photos_thumb_url")));
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
    
    protected Code fetchCode(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> codeProperties = new HashMap<String, String>();

        try {
            _codeJSON = _forrst.postsShow(id);
            
            codeProperties.put("id", _codeJSON.getString("id"));
            codeProperties.put("post_type", _codeJSON.getString("post_type"));
            codeProperties.put("post_url", _codeJSON.getString("post_url"));

            long codeDateInMillis = sdf.parse(_codeJSON.getString("created_at")).getTime();
            String codeDate = DateUtils.formatDateTime(null, codeDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
            codeProperties.put("created_at", codeDate);
            
            codeProperties.put("name", _codeJSON.getJSONObject("user").getString("name"));
            codeProperties.put("title", _codeJSON.getString("title"));
            codeProperties.put("url", _codeJSON.getString("url"));
            codeProperties.put("content", _codeJSON.getString("content"));
            codeProperties.put("description", _codeJSON.getString("description"));
            codeProperties.put("formatted_content", _codeJSON.getString("formatted_content"));
            codeProperties.put("formatted_description", _codeJSON.getString("formatted_description"));
            codeProperties.put("view_count", Integer.toString(_codeJSON.getInt("view_count")));
            codeProperties.put("like_count", _codeJSON.getString("like_count"));
            codeProperties.put("comment_count", _codeJSON.getString("comment_count"));
            codeProperties.put("user_photos_thumb_url", _codeJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
            codeProperties.put("tag_string", _codeJSON.getString("tag_string"));

            return new Code(codeProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching code from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing code date", e);
        }
    }
    
    protected Bitmap fetchCodeUserIcon(String uri) {
        Bitmap codeUserIconData = null;
        try {
            InputStream is = (InputStream) new URL(uri).getContent();
            codeUserIconData = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error processing code url", e);
        } catch (IOException e) {
            throw new RuntimeException("Error fetch data from stream", e);
        }
        return codeUserIconData;
    }

}
