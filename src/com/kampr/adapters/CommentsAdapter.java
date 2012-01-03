package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Comment;

public class CommentsAdapter extends AbstractListAdapter<Comment> {

    public CommentsAdapter(Context context, List<Comment> comments) {
        super(context, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.comment_item);
        
        Comment comment = (Comment) _objects.get(position);
        
        ImageView commentUserIcon = (ImageView) getViewHandle(convertView, R.id.user_icon_thumbnail);
        commentUserIcon.setImageBitmap(comment.getUserIcon());

        TextView commentUsername = (TextView) getViewHandle(convertView, R.id.comment_item_username);
        commentUsername.setText(comment.getUserName());

        TextView commentDate = (TextView) getViewHandle(convertView, R.id.comment_item_date);
        commentDate.setText(comment.getCreatedAt());

        TextView commentTitle = (TextView) getViewHandle(convertView, R.id.comment_item_content);
        commentTitle.setText(comment.getBody());

        convertView.setId(comment.getId());

        return convertView;
    }
    
}
