package com.kampr.runnables;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
import com.kampr.handlers.PostsHandler;

public abstract class AbstractRunnable implements Runnable {

    protected static ForrstAPI _forrst = new ForrstAPIClient();
    protected static SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    protected Handler _handler;
    
    public AbstractRunnable(Handler handler) {
        _handler = handler;
    }
    
    public abstract void run();
    
    protected void notifyHandler() {
        Bundle handlerData = new Bundle();
        handlerData.putInt(PostsHandler.FETCH_STATUS, PostsHandler.FETCH_COMPLETE);
        
        Message fetchingCompleteMessage = new Message();
        fetchingCompleteMessage.setData(handlerData);
        
        _handler.sendMessage(fetchingCompleteMessage);
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
    
    protected Bitmap getBitmapFromStream(InputStream is, Context context, int drawableId) {
        Bitmap bmp = BitmapFactory.decodeStream(is);
        if(bmp == null) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.forrst_default_25);
        }
        else {
            return bmp;
        }
    }

}