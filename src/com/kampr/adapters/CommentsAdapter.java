package com.kampr.adapters;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Comment;
import com.kampr.util.LayoutUtils;

public class CommentsAdapter extends AbstractListAdapter<Comment> {
    
    private Map<String,Bitmap> _userIcons;

    public CommentsAdapter(Context context, List<Comment> comments, Map<String,Bitmap> userIcons) {
        super(context, comments);
        _userIcons = userIcons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.comment_item);
        
        Comment comment = (Comment) _objects.get(position);
        
        ImageView commentUserIcon = (ImageView) getViewHandle(convertView, R.id.comment_item_user_icon);
        commentUserIcon.setImageBitmap(_userIcons.get(comment.getProperty("id")));

        TextView commentUsername = (TextView) getViewHandle(convertView, R.id.comment_item_username);
        commentUsername.setText(comment.getProperty("name"));

        TextView commentDate = (TextView) getViewHandle(convertView, R.id.comment_item_date);
        commentDate.setText(comment.getProperty("created_at"));

        TextView commentTitle = (TextView) getViewHandle(convertView, R.id.comment_item_content);
        commentTitle.setText(comment.getProperty("body"));
        
        LayoutUtils.setFont(this._context, commentUsername);
        LayoutUtils.setFont(this._context, commentDate);
        LayoutUtils.setFont(this._context, commentTitle);

        convertView.setId(Integer.parseInt(comment.getProperty("id")));

        return convertView;
    }
    
}
