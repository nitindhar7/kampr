package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Post;

public class PostsAdapter<T> extends AbstractListAdapter<T> {
    
    public PostsAdapter(Context context, List<T> posts) {
        super(context, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.post_item);

        Post post = (Post) _objects.get(position);

        ImageView postUserIcon = (ImageView) getViewHandle(convertView, R.id.user_icon_thumbnail);
        postUserIcon.setImageBitmap(post.getUserIcon());

        TextView postUsername = (TextView) getViewHandle(convertView, R.id.post_item_username);
        postUsername.setText(post.getUserName());

        TextView postDate = (TextView) getViewHandle(convertView, R.id.post_item_date);
        postDate.setText(post.getCreatedAt());
        
        TextView postLikes = (TextView) getViewHandle(convertView, R.id.post_likes_count);
        postLikes.setText(Integer.toString(post.getLikeCount()));
        
        TextView postViews = (TextView) getViewHandle(convertView, R.id.post_views_count);
        postViews.setText(Integer.toString(post.getViewCount()));
        
        TextView postComments = (TextView) getViewHandle(convertView, R.id.post_comments_count);
        postComments.setText(Integer.toString(post.getCommentCount()));

        TextView postTitle = (TextView) getViewHandle(convertView, R.id.post_item_content);
        postTitle.setText(post.getTitle());

        convertView.setId(post.getId());

        return convertView;
    }
    
    public Post getViewObject(int position) {
        return (Post) _objects.get(position);
    }

}