package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.kampr.R;
import com.kampr.models.Comment;

public class CommentsAdapter extends AbstractListAdapter<Comment> {

    public CommentsAdapter(Context context, List<Comment> comments) {
        super(context, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.comment_item);

        return convertView;
    }
    
}
