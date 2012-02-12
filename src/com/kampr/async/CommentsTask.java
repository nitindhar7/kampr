package com.kampr.async;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.forrst.api.model.Comment;
import com.kampr.KamprActivity;
import com.kampr.R;
import com.kampr.adapters.CommentsAdapter;
import com.kampr.models.CommentDecorator;
import com.kampr.posts.CommentsActivity;
import com.kampr.util.ImageUtils;
import com.kampr.util.LayoutUtils;

public class CommentsTask extends AsyncTask<Integer, Integer, List<CommentDecorator>> {
    
    protected static final ForrstAPI _forrst = new ForrstAPIClient();
    
    private Context _context;
    private ListView _comments;
    
    public CommentsTask(Context context, ListView comments) {
        _context = context;
        _comments = comments;
    }
    
    protected List<CommentDecorator> doInBackground(Integer... params) {
        List<CommentDecorator> listOfComments = new ArrayList<CommentDecorator>();
        
        String loginToken = _context.getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0).getString("login_token", null);
        
        for(Comment comment : _forrst.postComments(loginToken, params[0])) {
            CommentDecorator cd = new CommentDecorator();
            cd.setComment(comment);
            cd.setUserIcon(ImageUtils.fetchUserIcon(_context, comment.getUserIconUrl(), R.drawable.forrst_default_25));
            listOfComments.add(cd);
        }
        
        return listOfComments;
    }
    
    protected void onPostExecute(List<CommentDecorator> listOfComments) {
        CommentsAdapter commentsAdapter = new CommentsAdapter(_context, listOfComments);
        _comments.setAdapter(commentsAdapter);
        LayoutUtils.layoutOverride(CommentsActivity.getSpinner(), View.GONE);
    }

}
