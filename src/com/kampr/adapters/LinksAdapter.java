package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kampr.models.Link;

public class LinksAdapter extends BaseAdapter {
    
    private Context _context;
    private List<Link> _links;
    
    public LinksAdapter(Context context, List<Link> links) {
        this._context = context;
        this._links = links;
    }

    @Override
    public int getCount() {
        return _links.size();
    }

    @Override
    public Object getItem(int position) {
        return _links.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        Link link = _links.get(position);
        
        if(convertView == null) {
            inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(com.kampr.R.layout.link, null);
        }

        return convertView;
    }

}
