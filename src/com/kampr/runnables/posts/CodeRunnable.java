package com.kampr.runnables.posts;

import android.os.Handler;

import com.kampr.models.PropertyContainer;

public class CodeRunnable extends PostRunnable {
    
    public CodeRunnable(Handler handler, PropertyContainer post) {
        super(handler, post);
    }
    
    public void run() {
        notifyHandler();
    }

}