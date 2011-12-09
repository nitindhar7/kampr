package com.kampr.runnables.tabs;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;

import com.kampr.R;
import com.kampr.handlers.PostsHandler;
import com.kampr.models.PropertyContainer;
import com.kampr.runnables.AbstractRunnable;

public abstract class PostsRunnable<T> extends AbstractRunnable {

    protected Map<String,String> _forrstParams;
    protected Context _context;
    protected List<T> _listOfPosts;
    protected Map<String,Bitmap> _userIcons;
    
    public PostsRunnable(Context context, PostsHandler<T> handler, List<T> listOfPosts, Map<String,Bitmap> userIcons, Map<String,String> forrstParams) {
        super(handler);
        _context = context;
        _listOfPosts = listOfPosts;
        _userIcons = userIcons;
        _forrstParams = forrstParams;
    }
    
    protected void fetchUserIcon(T t) {
        PropertyContainer post = (PropertyContainer) t;
        try {
            InputStream is = (InputStream) new URL(post.getProperty("user_photos_thumb_url")).getContent();
            _userIcons.put(post.getProperty("id"), getBitmapFromStream(is, _context, R.drawable.forrst_default_25));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: malformed URI", e);
        } catch (IOException e) {
            throw new RuntimeException("Error: could not read from stream", e);
        }
    }

}