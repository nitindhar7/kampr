package com.kampr.posts;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.models.Post;
import com.markupartist.android.widget.ActionBar;

public class PostActivity extends Activity {

    protected static final String FETCH_STATUS = "fetch_status";
    protected static final int FETCH_COMPLETE = 1;
    protected static final int DEFAULT_POST_ID = -1;

    protected final int TRUNCATED_URL_LENGTH = 35;
    
    protected final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected ForrstAPI _forrst;
    protected JSONObject _postJSON;
    protected ProgressDialog _dialog;
    protected Post _post;
    protected ActionBar _actionBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _forrst = new ForrstAPIClient();
        _dialog = ProgressDialog.show(PostActivity.this, "", "Loading...", true);
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
            return text.substring(0, maxLength) + "...";
        }
        else {
            return text;
        }
    }
    
    protected String getPostDate() {
        try {
            long inMillis = _dateFormat.parse(_postJSON.getString("created_at")).getTime();
            return DateUtils.formatDateTime(null, inMillis, DateUtils.FORMAT_ABBREV_ALL);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing post date", e);
        } catch (JSONException e) {
            throw new RuntimeException("Error retrieving date from json", e);
        }
    }
    
    protected void notifyHandler(Handler handler) {
        Bundle handlerData = new Bundle();
        handlerData.putInt(FETCH_STATUS, FETCH_COMPLETE);
        
        Message fetchingCompleteMessage = new Message();
        fetchingCompleteMessage.setData(handlerData);
        
        handler.sendMessage(fetchingCompleteMessage);
    }

}