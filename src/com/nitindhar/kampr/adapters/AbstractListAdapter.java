package com.nitindhar.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class AbstractListAdapter<T> extends BaseAdapter {
    
    protected Context _context;
    protected LayoutInflater _inflater;
    protected List<T> _objects;
    
    public AbstractListAdapter(Context context, List<T> objects) {
        this._context = context;
        this._objects = objects;
        _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
        return _objects.size();
    }

    @Override
    public Object getItem(int position) {
        return _objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    protected Object getViewHandle(View parent, int viewId) {
        Object view = parent.getTag(viewId);
        if(view == null) {
            view = parent.findViewById(viewId);
            parent.setTag(viewId, view);
        }
        return view;
    }
    
    protected View getConvertView(View view, int viewId) {
        if(view == null) {
            return _inflater.inflate(viewId, null);
        }
        else {
            return view;
        }
    }

}
