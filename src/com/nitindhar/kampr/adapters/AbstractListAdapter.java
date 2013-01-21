package com.nitindhar.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class AbstractListAdapter<T> extends BaseAdapter {

    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> objects;

    public AbstractListAdapter(Context context, List<T> objects) {
        this.context = context;
        this.objects = objects;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
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
            return inflater.inflate(viewId, null);
        }
        else {
            return view;
        }
    }

}
