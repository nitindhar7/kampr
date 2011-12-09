package com.kampr.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.PropertyContainer;
import com.kampr.posts.CodeActivity;
import com.kampr.posts.LinkActivity;
import com.kampr.posts.QuestionActivity;
import com.kampr.posts.SnapActivity;
import com.kampr.runnables.tabs.AllRunnable;
import com.kampr.util.KamprImageUtils;

public class AllActivity extends PostsListActivity<PropertyContainer> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<PropertyContainer>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new AllRunnable(this, _handler, _listOfPosts, _userIcons, null));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent postIntent = null;
        PropertyContainer post = _handler.getAdapter().getViewObject(position);
        
        if (post.getProperty("post_type").equals("link")) {
            postIntent = new Intent(AllActivity.this, LinkActivity.class);
        }
        else if (post.getProperty("post_type").equals("snap")) {
            postIntent = new Intent(AllActivity.this, SnapActivity.class);
        }
        else if (post.getProperty("post_type").equals("code")) {
            postIntent = new Intent(AllActivity.this, CodeActivity.class);
        }
        else {
            postIntent = new Intent(AllActivity.this, QuestionActivity.class);
        }
        
        postIntent.putExtra("post", post);
        
        Bitmap bmp = _userIcons.get(post.getProperty("id"));
        postIntent.putExtra("post_user_icon", KamprImageUtils.getByteArrayFromBitmap(bmp));
        
        startActivity(postIntent);
    }

}