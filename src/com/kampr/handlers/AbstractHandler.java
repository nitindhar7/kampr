package com.kampr.handlers;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.ProgressBar;

public abstract class AbstractHandler<T> extends Handler {
    
    public static final String FETCH_STATUS = "fetch_status";
    public static final int FETCH_COMPLETE = 1;
    
    protected Context _context;
    protected ProgressBar _spinner;
    protected ListView _list;
    protected List<T> _listOfItems;
    
    public abstract void handleMessage(Message msg);

}
