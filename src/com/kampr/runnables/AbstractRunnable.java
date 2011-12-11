package com.kampr.runnables;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.kampr.handlers.PostsHandler;
import com.petebevin.markdown.MarkdownProcessor;

public abstract class AbstractRunnable implements Runnable {

    protected static final ForrstAPI _forrst = new ForrstAPIClient();
    protected static final MarkdownProcessor _processor = new MarkdownProcessor(); 
    
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
    
    

}