package com.kampr.posts.runnables;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;

import com.forrst.api.ForrstAPI;
import com.kampr.models.Post;

public abstract class PostRunnable implements Runnable {
    
    protected static final String FETCH_STATUS = "fetch_status";
    protected static final int FETCH_COMPLETE = 1;
    
    protected final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    protected JSONObject _postJSON;
    protected ForrstAPI _forrst;
    protected int _postId;
    protected Post _post;
    protected Handler _handler;
    
    public PostRunnable(int postId, ForrstAPI forrst, Handler handler, Post post) {
        _postId = postId;
        _forrst = forrst;
        _handler = handler;
        _post = post;
    }
    
    public abstract void run();
    
    protected void notifyHandler(Handler handler) {
        Bundle handlerData = new Bundle();
        handlerData.putInt(FETCH_STATUS, FETCH_COMPLETE);
        
        Message fetchingCompleteMessage = new Message();
        fetchingCompleteMessage.setData(handlerData);
        
        handler.sendMessage(fetchingCompleteMessage);
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

}