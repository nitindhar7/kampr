package com.nitindhar.kampr.adapters;

import java.sql.Timestamp;
import java.text.ParseException;
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

        CommentDecorator cd = objects.get(position);
        Comment comment = cd.getComment();

        ImageView commentUserIcon = (ImageView) getViewHandle(convertView,
                R.id.user_icon_thumbnail);
        commentUserIcon.setImageBitmap(cd.getUserIcon());

        TextView commentUsername = (TextView) getViewHandle(convertView,
                R.id.comment_item_username);
        commentUsername.setText(comment.getUser().getName());

        TextView commentDate = (TextView) getViewHandle(convertView,
                R.id.comment_item_date);
        try {
            commentDate.setText(TimeUtils.getPostDate(comment.getCreatedAt()
                    .toString()));
        } catch (ParseException e) {
            commentDate.setText(TimeUtils.getPostDate((new Timestamp(System
                    .currentTimeMillis())).toString()));
        }

        TextView commentTitle = (TextView) getViewHandle(convertView,
                R.id.comment_item_content);
        commentTitle.setText(TextUtils.convertHtmlToText(comment.getBody()));

        convertView.setId(comment.getId());

        SpanUtils.setFont(context, commentUsername, SpanUtils.FONT_BOLD);
        SpanUtils.setFont(context, commentDate);
        SpanUtils.setFont(context, commentTitle);

        return convertView;
    }

}