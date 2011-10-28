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
import com.kampr.models.Post;

public class PostsAdapter<T> extends AbstractListAdapter<T> {

    private Map<String,Bitmap> _userIcons;
    
    public PostsAdapter(Context context, List<T> posts, Map<String,Bitmap> userIcons) {
        super(context, posts);
        _userIcons = userIcons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {        
        convertView = getConvertView(convertView, R.layout.post_item);

        Post post = (Post) _objects.get(position);

        ImageView postUserIcon = (ImageView) getViewHandle(convertView, R.id.post_item_user_icon);
        postUserIcon.setImageBitmap(_userIcons.get(post.getProperty("id")));

        TextView postUsername = (TextView) getViewHandle(convertView, R.id.post_item_username);
        postUsername.setText(post.getProperty("name"));

        TextView postDate = (TextView) getViewHandle(convertView, R.id.post_item_date);
        postDate.setText(post.getProperty("created_at"));

        TextView postTitle = (TextView) getViewHandle(convertView, R.id.post_item_content);
        postTitle.setText(post.getProperty("title"));

        convertView.setId(Integer.parseInt(post.getProperty("id")));

        return convertView;
    }

}