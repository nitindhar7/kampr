package com.kampr.handlers;

import java.util.List;

import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kampr.adapters.CommentsAdapter;
import com.kampr.models.Comment;

public class CommentsHandler extends AbstractHandler {

    private List<Comment> _listOfItems;
    private CommentsAdapter _commentsAdapter;
    
    public CommentsHandler(Context context, ProgressBar spinner, ListView comments, List<Comment> listOfComments) {
        _context = context;
        _spinner = spinner;
        _list = comments;
        _listOfItems = listOfComments;
    }
    
    @Override
    public void handleMessage(Message msg) {
        switch(msg.getData().getInt(FETCH_STATUS)) {
            case FETCH_COMPLETE:
                _commentsAdapter = new CommentsAdapter(_context, _listOfItems);
                _list.setAdapter(_commentsAdapter);
                _spinner.setVisibility(View.GONE);
                break;
        }
    }

}
