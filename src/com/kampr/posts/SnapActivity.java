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
import com.kampr.models.Snap;

public class SnapActivity extends Activity {
    
    private final String ACTIVITY_TAG = "SnapActivity";
    
    protected static final int DEFAULT_SNAP_ID = -1;
    
    private ForrstAPI _forrst;
    private JSONObject _snapJSON;
    
    private TextView snapTitle;
    private TextView snapUrl;
    private TextView snapUsername;
    private TextView snapDate;
    private TextView snapDescription;
    private ImageView snapUserIcon;
    private ImageView snapLargeUrl;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.snap);
        
        _forrst = new ForrstAPIClient();
        
        Snap snap = fetchSnap(getIntent().getIntExtra("id", DEFAULT_SNAP_ID));
        
        snapTitle = (TextView) findViewById(R.id.snap_title);
        snapUrl = (TextView) findViewById(R.id.snap_url);
        snapUsername = (TextView) findViewById(R.id.snap_user_name);
        snapDate = (TextView) findViewById(R.id.snap_date);
        snapDescription = (TextView) findViewById(R.id.snap_description);
        snapUserIcon = (ImageView) findViewById(R.id.snap_user_icon);
        snapLargeUrl = (ImageView) findViewById(R.id.snap_large_url);
        
        snapTitle.setText(snap.getProperty("title"));
        snapUrl.setText(snap.getProperty("url"));
        snapUsername.setText(snap.getProperty("name"));
        snapDate.setText(snap.getProperty("created_at"));
        snapDescription.setText(snap.getProperty("description"));
        snapUserIcon.setImageBitmap(fetchSnapImage(snap.getProperty("user_photos_thumb_url")));
        snapLargeUrl.setImageBitmap(fetchSnapImage(snap.getProperty("snaps_large_url")));
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
    
    protected Snap fetchSnap(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> snapProperties = new HashMap<String, String>();

        try {
            _snapJSON = _forrst.postsShow(id);
            
            snapProperties.put("id", _snapJSON.getString("id"));
            snapProperties.put("post_type", _snapJSON.getString("post_type"));
            snapProperties.put("post_url", _snapJSON.getString("post_url"));

            long snapDateInMillis = sdf.parse(_snapJSON.getString("created_at")).getTime();
            String snapDate = DateUtils.formatDateTime(null, snapDateInMillis, DateUtils.FORMAT_ABBREV_ALL);
            snapProperties.put("created_at", snapDate);
            
            snapProperties.put("name", _snapJSON.getJSONObject("user").getString("name"));
            snapProperties.put("title", _snapJSON.getString("title"));
            snapProperties.put("url", _snapJSON.getString("url"));
            snapProperties.put("content", _snapJSON.getString("content"));
            snapProperties.put("description", _snapJSON.getString("description"));
            snapProperties.put("formatted_content", _snapJSON.getString("formatted_content"));
            snapProperties.put("formatted_description", _snapJSON.getString("formatted_description"));
            snapProperties.put("view_count", Integer.toString(_snapJSON.getInt("view_count")));
            snapProperties.put("like_count", _snapJSON.getString("like_count"));
            snapProperties.put("comment_count", _snapJSON.getString("comment_count"));
            snapProperties.put("user_photos_thumb_url", _snapJSON.getJSONObject("user").getJSONObject("photos").getString("thumb_url"));
            snapProperties.put("snaps_large_url", _snapJSON.getJSONObject("snaps").getString("large_url"));
            snapProperties.put("tag_string", _snapJSON.getString("tag_string"));

            return new Snap(snapProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching snap from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing snap date", e);
        }
    }
    
    protected Bitmap fetchSnapImage(String uri) {
        Bitmap snapImageData = null;
        try {
            InputStream is = (InputStream) new URL(uri).getContent();
            snapImageData = BitmapFactory.decodeStream(is);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error processing snap url", e);
        } catch (IOException e) {
            throw new RuntimeException("Error fetch data from stream", e);
        }
        return snapImageData;
    }

}
