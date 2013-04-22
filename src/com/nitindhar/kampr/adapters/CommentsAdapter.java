package com.nitindhar.kampr.adapters;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitindhar.forrst.model.Comment;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.activity.UserActivity;
import com.nitindhar.kampr.models.CommentsDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.TimeUtils;

public class CommentsAdapter extends AbstractListAdapter<CommentsDecorator> {

    public CommentsAdapter(Context context, List<CommentsDecorator> comments) {
        super(context, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.comment_item);

        final CommentsDecorator cd = objects.get(position);
        final Comment comment = cd.getComment();

        OnClickListener onUserClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent userIntent = new Intent(context, UserActivity.class);
                userIntent.putExtra("user", comment.getUser());
                userIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(cd.getUserIcon()));
                context.startActivity(userIntent);
            }
        };

        ImageView commentUserIcon = (ImageView) getViewHandle(convertView,
                R.id.user_icon_thumbnail);
        commentUserIcon.setImageBitmap(cd.getUserIcon());
        commentUserIcon.setOnClickListener(onUserClickListener);

        TextView commentUsername = (TextView) getViewHandle(convertView,
                R.id.comment_item_username);
        commentUsername.setText(comment.getUser().getName());
        commentUsername.setOnClickListener(onUserClickListener);

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
        commentTitle.setText(comment.getBody());

        convertView.setId(comment.getId());

        return convertView;
    }

}