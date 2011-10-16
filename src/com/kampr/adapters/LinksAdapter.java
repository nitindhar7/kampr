package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Link;

public class LinksAdapter extends PostsAdapter<Link> {
    
    public LinksAdapter(Context context, List<Link> links) {
        super(context, links);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        Link link = _posts.get(position);
        
        if(convertView == null) {
            inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_item, null);
        }
        
        TextView postUsername = (TextView) convertView.findViewById(R.id.post_username);
        postUsername.setText(link.getProperty("name"));
        
        TextView postDate = (TextView) convertView.findViewById(R.id.post_date);
        postDate.setText(link.getProperty("created_at"));
        
        TextView postTitle = (TextView) convertView.findViewById(R.id.post_title);
        postTitle.setText(link.getProperty("title"));

        return convertView;
    }

}
