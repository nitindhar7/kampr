package com.kampr.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kampr.handlers.PostsHandler;
import com.kampr.models.Question;
import com.kampr.posts.QuestionActivity;
import com.kampr.runnables.tabs.QuestionsRunnable;

public class QuestionsActivity extends PostsListActivity<Question> {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _handler = new PostsHandler<Question>(this, _dialog, _posts, _listOfPosts, _userIcons);
        _fetchPostsThread = new Thread(new QuestionsRunnable(this, _handler, _listOfPosts, _userIcons));
        _fetchPostsThread.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent question = new Intent(QuestionsActivity.this, QuestionActivity.class);
        question.putExtra("id", view.getId());
        startActivity(question);
    }

}