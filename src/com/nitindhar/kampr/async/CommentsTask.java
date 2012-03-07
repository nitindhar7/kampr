package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.model.Comment;
import com.nitindhar.kampr.KamprActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.CommentsAdapter;
import com.nitindhar.kampr.models.CommentDecorator;
import com.nitindhar.kampr.posts.CommentsActivity;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

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
