package com.kampr.runnables.posts;

import org.json.JSONObject;

import android.os.Handler;

import com.kampr.models.PropertyContainer;
import com.kampr.runnables.AbstractRunnable;

public abstract class PostRunnable extends AbstractRunnable {
    
    protected static final String FETCH_STATUS = "fetch_status";
    protected static final int FETCH_COMPLETE = 1;
    
    protected JSONObject _postJSON;
    protected PropertyContainer _post;
    
    public PostRunnable(Handler handler, PropertyContainer post) {
        super(handler);
        _post = post;
    }

}