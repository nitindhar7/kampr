package com.kampr.tabs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.PropertyContainer;
import com.kampr.models.Question;
import com.kampr.posts.QuestionActivity;
import com.kampr.runnables.tabs.QuestionsRunnable;
import com.kampr.util.KamprImageUtils;

public class QuestionsActivity extends PostsListActivity<Question> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Question>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new QuestionsRunnable(this, _handler, _listOfPosts, _userIcons, null));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent question = new Intent(QuestionsActivity.this, QuestionActivity.class);
        PropertyContainer post = _handler.getAdapter().getViewObject(position);
        question.putExtra("post", post);
        
        Bitmap bmp = _userIcons.get(post.getProperty("id"));
        question.putExtra("post_user_icon", KamprImageUtils.getByteArrayFromBitmap(bmp));

        startActivity(question);
    }

}