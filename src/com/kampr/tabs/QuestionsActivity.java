package com.kampr.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.PropertyContainer;
import com.kampr.posts.QuestionActivity;
import com.kampr.runnables.PostsRunnable;
import com.kampr.util.ImageUtils;

public class QuestionsActivity extends PostsListActivity<PropertyContainer> {
    
    private static final String POST_TYPE = "question";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<PropertyContainer>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new PostsRunnable(this, _handler, _listOfPosts, _userIcons, null, POST_TYPE));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent question = new Intent(QuestionsActivity.this, QuestionActivity.class);
        PropertyContainer post = _handler.getAdapter().getViewObject(position);
        question.putExtra("post", post);
        
        Bitmap bmp = _userIcons.get(post.getProperty("id"));
        question.putExtra("post_user_icon", ImageUtils.getByteArrayFromBitmap(bmp));

        startActivity(question);
    }

}