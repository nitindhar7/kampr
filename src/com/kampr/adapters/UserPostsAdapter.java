package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forrst.api.model.Post;
import com.kampr.R;
import com.kampr.util.TimeUtils;

public class UserPostsAdapter<T> extends AbstractListAdapter<T> {
    
    public UserPostsAdapter(Context context, List<T> posts) {
        super(context, posts);
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = getConvertView(convertView, R.layout.user_post_item);
        
        Post post = (Post) _objects.get(position);
        
        ImageView postTypeIcon = (ImageView) getViewHandle(convertView, R.id.user_post_item_type_icon);
        if (post.getPostType().equals("link")) {
            postTypeIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.list_link));
        }
        else if (post.getPostType().equals("snap")) {
            postTypeIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.list_snap));
        }
        else if (post.getPostType().equals("code")) {
            postTypeIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.list_code));
        }
        else {
            postTypeIcon.setImageDrawable(_context.getResources().getDrawable(R.drawable.list_question));
        }
        
        TextView postDate = (TextView) getViewHandle(convertView, R.id.user_post_item_date);
        postDate.setText(TimeUtils.getPostDate(post.getCreatedAt()));
        
        TextView postLikes = (TextView) getViewHandle(convertView, R.id.post_likes_count);
        postLikes.setText(Integer.toString(post.getLikeCount()));
        
        TextView postViews = (TextView) getViewHandle(convertView, R.id.post_views_count);
        postViews.setText(Integer.toString(post.getViewCount()));
        
        TextView postComments = (TextView) getViewHandle(convertView, R.id.post_comments_count);
        postComments.setText(Integer.toString(post.getCommentCount()));

        TextView postTitle = (TextView) getViewHandle(convertView, R.id.user_post_item_content);
        postTitle.setText(post.getTitle());

        convertView.setId(post.getId());
        
        return convertView;
    }
    
    public Post getViewObject(int position) {
        return (Post) _objects.get(position);
    }
    
}
