package com.kampr.runnables.posts;

import org.json.JSONObject;

import android.os.Handler;

import com.kampr.models.PropertyContainer;
import com.kampr.runnables.AbstractRunnable;

public abstract class PostRunnable extends AbstractRunnable {
    
    protected static final String FETCH_STATUS = "fetch_status";
    protected static final int FETCH_COMPLETE = 1;
    
    protected JSONObject _postJSON;
    protected int _postId;
    protected PropertyContainer _post;
    
    public PostRunnable(int postId, Handler handler, PropertyContainer post) {
        super(handler);
        _postId = postId;
        _post = post;
    }

}