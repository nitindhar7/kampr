package com.kampr.adapters;

import java.util.List;

import android.content.Context;

import com.kampr.models.Question;

public class QuestionsAdapter extends PostsAdapter<Question> {
    
    public QuestionsAdapter(Context context, List<Question> questions) {
        super(context, questions);
    }

}
