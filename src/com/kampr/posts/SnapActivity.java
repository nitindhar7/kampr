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
import com.kampr.models.Snap;

public class SnapActivity extends PostActivity {
    
    private final String ACTIVITY_TAG = "SnapActivity";
    
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

        Snap snap = fetchSnap(getIntent().getIntExtra("id", DEFAULT_POST_ID));
        
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
        snapUserIcon.setImageBitmap(fetchImageBitmap(snap.getProperty("user_photos_thumb_url")));
        snapLargeUrl.setImageBitmap(fetchImageBitmap(snap.getProperty("snaps_large_url")));
    }

    protected Snap fetchSnap(int id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, String> snapProperties = new HashMap<String, String>();

        try {
            _postJSON = _forrst.postsShow(id);
            
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

            return new Snap(snapProperties);
        } catch (JSONException e) {
            throw new RuntimeException("Error fetching snap from Forrst", e);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing snap date", e);
        }
    }

}
