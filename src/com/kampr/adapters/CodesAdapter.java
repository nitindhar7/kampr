package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Code;

public class CodesAdapter extends PostsAdapter<Code> {
    
    public CodesAdapter(Context context, List<Code> codes) {
        super(context, codes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        Code code = _posts.get(position);
        
        if(convertView == null) {
            inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_item, null);
        }
        
        TextView postUsername = (TextView) convertView.findViewById(R.id.post_username);
        postUsername.setText(code.getProperty("name"));
        
        TextView postDate = (TextView) convertView.findViewById(R.id.post_date);
        postDate.setText(code.getProperty("created_at"));
        
        TextView postTitle = (TextView) convertView.findViewById(R.id.post_title);
        postTitle.setText(code.getProperty("title"));

        return convertView;
    }

}
