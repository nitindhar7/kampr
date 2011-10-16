package com.kampr.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kampr.R;
import com.kampr.models.Question;

public class QuestionsAdapter extends PostsAdapter<Question> {
    
    public QuestionsAdapter(Context context, List<Question> questions) {
        super(context, questions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = null;
        Question question = _posts.get(position);
        
        if(convertView == null) {
            inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.question_item, null);
        }
        
        TextView questionUsername = (TextView) convertView.findViewById(R.id.question_username);
        questionUsername.setText(question.getProperty("name"));
        
        TextView questionDate = (TextView) convertView.findViewById(R.id.question_date);
        questionDate.setText(question.getProperty("created_at"));
        
        TextView questionTitle = (TextView) convertView.findViewById(R.id.question_title);
        questionTitle.setText(question.getProperty("title"));

        return convertView;
    }

}
