package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class PostsAdapter<T> extends BaseAdapter {

    protected Context _context;
    protected List<T> _posts;
    
    public PostsAdapter(Context context, List<T> posts) {
        this._context = context;
        this._posts = posts;
    }
    
    @Override
    public int getCount() {
        return _posts.size();
    }

    @Override
    public Object getItem(int position) {
        return _posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);

}
