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
            convertView = inflater.inflate(R.layout.snap_item, null);
        }
        
        TextView snapUsername = (TextView) convertView.findViewById(R.id.snap_username);
        snapUsername.setText(snap.getProperty("name"));
        
        TextView snapDate = (TextView) convertView.findViewById(R.id.snap_date);
        snapDate.setText(snap.getProperty("created_at"));
        
        TextView snapTitle = (TextView) convertView.findViewById(R.id.snap_title);
        snapTitle.setText(snap.getProperty("title"));

        return convertView;
    }

}
