package com.kampr.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Post;

public abstract class PostsAdapter<T> extends BaseAdapter {

    protected Context _context;
    protected List<T> _posts;
    
    private LayoutInflater _inflater;
    
    public PostsAdapter(Context context, List<T> posts) {
        this._context = context;
        this._posts = posts;
        _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
        return _posts.size();
    }

    @Override
    public Object getItem(int position) {
        return _posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        if(convertView == null) {
            convertView = _inflater.inflate(R.layout.post_item, null);
        }
        
        Post post = (Post) _posts.get(position);
        
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
    
    protected Object getViewHandle(View parent, int viewId) {
        Object view = parent.getTag(viewId);
        if(view == null) {
            view = parent.findViewById(viewId);
            parent.setTag(viewId, view);
        }
        return view;
    }

}
