package com.nitindhar.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nitindhar.kampr.R;
import com.nitindhar.kampr.activity.UserActivity;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.TimeUtils;

public class PostsAdapter<T> extends AbstractListAdapter<T> {

    public PostsAdapter(Context context, List<T> posts) {
        super(context, posts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.post_item);

        PostDecorator pd = (PostDecorator) objects.get(position);

        OnClickListener onUsernameClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(context, UserActivity.class);
                PostDecorator pd = getViewObject(position);
                postIntent.putExtra("user", pd.getPost().getUser());
                postIntent.putExtra("user_icon", ImageUtils.getByteArrayFromBitmap(pd.getUserIcon()));
                context.startActivity(postIntent);
            }
        };

        ImageView postUserIcon = (ImageView) getViewHandle(convertView,
                R.id.user_icon_thumbnail);
        postUserIcon.setImageBitmap(pd.getUserIcon());
        postUserIcon.setOnClickListener(onUsernameClickListener);

        TextView postUsername = (TextView) getViewHandle(convertView,
                R.id.post_item_username);
        postUsername.setText(pd.getPost().getUser().getName());
        postUsername.setOnClickListener(onUsernameClickListener);

        TextView postDate = (TextView) getViewHandle(convertView,
                R.id.post_item_date);
        postDate.setText(TimeUtils.getPostDate(pd.getPost().getCreatedAt()));

        TextView postLikes = (TextView) getViewHandle(convertView,
                R.id.post_likes_count);
        postLikes.setText(Integer.toString(pd.getPost().getLikeCount()));

        TextView postViews = (TextView) getViewHandle(convertView,
                R.id.post_views_count);
        postViews.setText(Integer.toString(pd.getPost().getViewCount()));

        TextView postComments = (TextView) getViewHandle(convertView,
                R.id.post_comments_count);
        postComments.setText(Integer.toString(pd.getPost().getCommentCount()));

        TextView postTitle = (TextView) getViewHandle(convertView,
                R.id.post_item_content);
        postTitle.setText(pd.getPost().getTitle());

        TextView postDescription = (TextView) getViewHandle(convertView,
                R.id.post_item_description);
        postDescription.setText(pd.getPost().getDescription().trim());

        convertView.setId(pd.getPost().getId());

        return convertView;
    }

    public PostDecorator getViewObject(int position) {
        return (PostDecorator) objects.get(position);
    }

}