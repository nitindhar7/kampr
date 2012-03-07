package com.nitindhar.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitindhar.forrst.model.Comment;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.models.CommentDecorator;
import com.nitindhar.kampr.util.SpanUtils;
import com.nitindhar.kampr.util.TextUtils;
import com.nitindhar.kampr.util.TimeUtils;

public class CommentsAdapter extends AbstractListAdapter<CommentDecorator> {

    public CommentsAdapter(Context context, List<CommentDecorator> comments) {
        super(context, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.comment_item);
        
        CommentDecorator cd = (CommentDecorator) _objects.get(position);
        Comment comment = cd.getComment();
        
        ImageView commentUserIcon = (ImageView) getViewHandle(convertView, R.id.user_icon_thumbnail);
        commentUserIcon.setImageBitmap(cd.getUserIcon());

        TextView commentUsername = (TextView) getViewHandle(convertView, R.id.comment_item_username);
        commentUsername.setText(comment.getUserName());

        TextView commentDate = (TextView) getViewHandle(convertView, R.id.comment_item_date);
        commentDate.setText(TimeUtils.getPostDate(comment.getCreatedAt().toString()));

        TextView commentTitle = (TextView) getViewHandle(convertView, R.id.comment_item_content);
        commentTitle.setText(TextUtils.convertHtmlToText(comment.getBody()));

        convertView.setId(comment.getId());
        
        SpanUtils.setFont(_context, commentUsername, SpanUtils.FONT_BOLD);
        SpanUtils.setFont(_context, commentDate);
        SpanUtils.setFont(_context, commentTitle);

        return convertView;
    }
    
}
