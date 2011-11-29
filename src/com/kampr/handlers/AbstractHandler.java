package com.kampr.handlers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public abstract class AbstractHandler extends Handler {
    
    public static final String FETCH_STATUS = "fetch_status";
    public static final int FETCH_COMPLETE = 1;
    
    protected Context _context;
    protected ProgressDialog _dialog;
    
    public abstract void handleMessage(Message msg);

}
