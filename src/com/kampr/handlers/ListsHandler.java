package com.kampr.handlers;

import java.util.List;

import android.widget.ListView;

public abstract class ListsHandler<T> extends AbstractHandler {

    protected ListView _list;
    protected List<T> _listOfItems;

}
