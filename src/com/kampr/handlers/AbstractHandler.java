package com.kampr.handlers;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

public abstract class AbstractHandler<T> extends Handler {
    
    public static final String FETCH_STATUS = "fetch_status";
    public static final int FETCH_COMPLETE = 1;
    
    protected Context _context;
    protected ProgressDialog _dialog;
    protected Map<String,Bitmap> _userIcons;
    protected ListView _list;
    protected List<T> _listOfItems;
    
    public abstract void handleMessage(Message msg);

}
