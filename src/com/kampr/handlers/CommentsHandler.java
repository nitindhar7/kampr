package com.kampr.handlers;

import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.widget.ListView;

import com.kampr.adapters.CommentsAdapter;
import com.kampr.models.Comment;

public class CommentsHandler extends AbstractHandler<Comment> {

    private CommentsAdapter _commentsAdapter;
    
    public CommentsHandler(Context context, ProgressDialog dialog, ListView comments, List<Comment> listOfComments, Map<String,Bitmap> userIcons) {
        _context = context;
        _dialog = dialog;
        _list = comments;
        _userIcons = userIcons; 
        _listOfItems = listOfComments;
    }
    
    @Override
    public void handleMessage(Message msg) {
        switch(msg.getData().getInt(FETCH_STATUS)) {
            case FETCH_COMPLETE:
                _commentsAdapter = new CommentsAdapter(_context, _listOfItems, _userIcons);
                _list.setAdapter(_commentsAdapter);
                _dialog.cancel();
                break;
        }
    }

}
