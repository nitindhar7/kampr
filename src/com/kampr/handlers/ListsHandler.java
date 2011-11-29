package com.kampr.handlers;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.widget.ListView;

public abstract class ListsHandler<T> extends AbstractHandler {
    
    protected Map<String,Bitmap> _userIcons;
    protected ListView _list;
    protected List<T> _listOfItems;

}
