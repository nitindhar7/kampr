package com.kampr.posts;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.models.Post;
import com.markupartist.android.widget.ActionBar;

public class PostActivity extends Activity {
    
    private final String ACTIVITY_TAG = "PostActivity";
    
    protected static final String FETCH_STATUS = "fetch_status";
    protected static final int FETCH_COMPLETE = 1;
    protected static final int DEFAULT_POST_ID = -1;

    protected final int TRUNCATED_URL_LENGTH = 35;

    protected ForrstAPI _forrst;
    protected JSONObject _postJSON;
    protected ProgressDialog _dialog;
    protected Post _post;
    protected ActionBar _actionBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");

        _forrst = new ForrstAPIClient();
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
            return text.substring(0, maxLength);
        }
        else {
            return text;
        }
    }

}
