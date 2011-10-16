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
            convertView = inflater.inflate(R.layout.code_item, null);
        }
        
        TextView codeUsername = (TextView) convertView.findViewById(R.id.code_username);
        codeUsername.setText(code.getProperty("name"));
        
        TextView codeDate = (TextView) convertView.findViewById(R.id.code_date);
        codeDate.setText(code.getProperty("created_at"));
        
        TextView codeTitle = (TextView) convertView.findViewById(R.id.code_title);
        codeTitle.setText(code.getProperty("title"));

        return convertView;
    }

}
