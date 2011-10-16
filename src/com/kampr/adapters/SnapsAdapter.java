package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Snap;

public class SnapsAdapter extends PostsAdapter<Snap> {
    
    public SnapsAdapter(Context context, List<Snap> snaps) {
        super(context, snaps);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        Snap snap = _posts.get(position);
        
        if(convertView == null) {
            inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_item, null);
        }
        
        TextView postUsername = (TextView) convertView.findViewById(R.id.post_username);
        postUsername.setText(snap.getProperty("name"));
        
        TextView postDate = (TextView) convertView.findViewById(R.id.post_date);
        postDate.setText(snap.getProperty("created_at"));
        
        TextView postTitle = (TextView) convertView.findViewById(R.id.post_title);
        postTitle.setText(snap.getProperty("title"));

        return convertView;
    }

}
