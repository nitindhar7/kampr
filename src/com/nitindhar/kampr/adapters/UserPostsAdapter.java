package com.nitindhar.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nitindhar.kampr.R;
import com.nitindhar.kampr.models.PostDecorator;
import com.nitindhar.kampr.util.TimeUtils;

public class UserPostsAdapter<T> extends AbstractListAdapter<T> {

    public UserPostsAdapter(Context context, List<T> posts) {
        super(context, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.user_post_item);

        PostDecorator pd = (PostDecorator) objects.get(position);

        TextView postDate = (TextView) getViewHandle(convertView,
                R.id.user_post_item_date);
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
                R.id.user_post_item_content);
        postTitle.setText(pd.getPost().getTitle());

        TextView postDescription = (TextView) getViewHandle(convertView,
                R.id.user_post_item_description);
        postDescription.setText(pd.getPost().getDescription().trim());

        convertView.setId(pd.getPost().getId());

        return convertView;
    }

    public PostDecorator getViewObject(int position) {
        return (PostDecorator) objects.get(position);
    }

}