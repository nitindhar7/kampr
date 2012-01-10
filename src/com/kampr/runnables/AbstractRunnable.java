package com.kampr.runnables;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.handlers.PostsHandler;

public abstract class AbstractRunnable implements Runnable {

    protected static final ForrstAPI _forrst = new ForrstAPIClient(); 
    
    protected Handler _handler;
    protected Context _context;
    
    public AbstractRunnable(Context context, Handler handler) {
        _context = context;
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
    
    

}