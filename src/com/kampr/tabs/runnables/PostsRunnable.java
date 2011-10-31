package com.kampr.tabs.runnables;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.R;
import com.kampr.models.Post;
import com.kampr.tabs.handlers.PostsHandler;

public abstract class PostsRunnable<T> implements Runnable {
    
    protected static ForrstAPI _forrst = new ForrstAPIClient();
    protected static SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    protected Context _context;
    protected Handler _handler;
    protected List<T> _listOfPosts;
    protected Map<String,Bitmap> _userIcons;
    
    public PostsRunnable(Context context, Handler handler, List<T> listOfPosts, Map<String,Bitmap> userIcons) {
        _context = context;
        _handler = handler;
        _listOfPosts = listOfPosts;
        _userIcons = userIcons;
    }
    
    public abstract void run();
    
    protected void notifyHandler() {
        Bundle handlerData = new Bundle();
        handlerData.putInt(PostsHandler.FETCH_STATUS, PostsHandler.FETCH_COMPLETE);
        
        Message fetchingCompleteMessage = new Message();
        fetchingCompleteMessage.setData(handlerData);
        
        _handler.sendMessage(fetchingCompleteMessage);
    }
    
    protected void fetchUserIcon(T t) {
        Post post = (Post) t;
        try {
            InputStream is = (InputStream) new URL(post.getProperty("user_photos_thumb_url")).getContent();
            _userIcons.put(post.getProperty("id"), getBitmapFromStream(is));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: malformed URI", e);
        } catch (IOException e) {
            throw new RuntimeException("Error: could not read from stream", e);
        }
    }
    
    protected String getPostDate(JSONObject json) {
        try {
            long inMillis = _dateFormat.parse(json.getString("created_at")).getTime();
            return DateUtils.formatDateTime(null, inMillis, DateUtils.FORMAT_ABBREV_ALL);
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing post date", e);
        } catch (JSONException e) {
            throw new RuntimeException("Error retrieving date from json", e);
        }
    }
    
    private Bitmap getBitmapFromStream(InputStream is) {
        Bitmap bmp = BitmapFactory.decodeStream(is);
        if(bmp == null) {
            return BitmapFactory.decodeResource(_context.getResources(), R.drawable.forrst_default_25);
        }
        else {
            return bmp;
        }
    }

}