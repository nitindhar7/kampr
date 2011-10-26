package com.kampr.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
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
        
        try {
            InputStream is = (InputStream) new URL(post.getProperty("user_photos_thumb_url")).getContent();
            
            ImageView postUserIcon = (ImageView) getViewHandle(convertView, R.id.post_item_user_icon);
            postUserIcon.setImageBitmap(BitmapFactory.decodeStream(is));
            
            TextView postUsername = (TextView) getViewHandle(convertView, R.id.post_item_username);
            postUsername.setText(post.getProperty("name"));
            
            TextView postDate = (TextView) getViewHandle(convertView, R.id.post_item_date);
            postDate.setText(post.getProperty("created_at"));
            
            TextView postTitle = (TextView) getViewHandle(convertView, R.id.post_item_content);
            postTitle.setText(post.getProperty("title"));
            
            convertView.setId(Integer.parseInt(post.getProperty("id")));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: malformed URI", e);
        } catch (IOException e) {
            throw new RuntimeException("Error: could not read from stream", e);
        }

        return convertView;
    }

}