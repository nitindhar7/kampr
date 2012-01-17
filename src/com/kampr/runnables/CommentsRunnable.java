package com.kampr.runnables;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.forrst.api.model.Comment;
import com.kampr.KamprActivity;
import com.kampr.R;
import com.kampr.models.CommentDecorator;
import com.kampr.util.ImageUtils;

public class CommentsRunnable extends AbstractRunnable {

    protected List<CommentDecorator> _listOfComments;
    protected SharedPreferences _settings;
    protected int _postId;
    
    public CommentsRunnable(Context context, Handler handler, List<CommentDecorator> listOfComments, int postId) {
        super(context, handler);
        _listOfComments = listOfComments;
        _settings = _context.getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0);
        _postId = postId;
    }

    @Override
    public void run() {
        for(Comment comment : _forrst.postComments(_settings.getString("login_token", null), _postId)) {
            CommentDecorator cd = new CommentDecorator();
            cd.setComment(comment);
            cd.setUserIcon(ImageUtils.fetchUserIcon(_context, comment.getUserIconUrl(), R.drawable.forrst_default_25));
            _listOfComments.add(cd);
        }

        notifyHandler();
    }
    
}
