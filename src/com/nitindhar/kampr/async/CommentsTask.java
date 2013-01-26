package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.http.HttpProvider;
import com.nitindhar.forrst.model.Comment;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.CommentsAdapter;
import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;
import com.nitindhar.kampr.models.CommentDecorator;
import com.nitindhar.kampr.posts.CommentsActivity;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class CommentsTask extends
        AsyncTask<Integer, Integer, List<CommentDecorator>> {

    protected static final ForrstAPI forrst = new ForrstAPIClient(
            HttpProvider.JAVA_NET);

    private final Context context;
    private final ListView comments;

    public CommentsTask(Context context, ListView comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    protected List<CommentDecorator> doInBackground(Integer... params) {
        List<CommentDecorator> listOfComments = new ArrayList<CommentDecorator>();

        SessionDao sessionDao = SessionSharedPreferencesDao.instance();

        for (Comment comment : forrst.postComments(
                sessionDao.getSessionToken(), params[0])) {
            CommentDecorator cd = new CommentDecorator();
            cd.setComment(comment);
            cd.setUserIcon(ImageUtils.fetchUserIcon(context, comment.getUser()
                    .getPhoto().getMediumUrl(), R.drawable.forrst_default_25));
            listOfComments.add(cd);
        }

        return listOfComments;
    }

    @Override
    protected void onPostExecute(List<CommentDecorator> listOfComments) {
        CommentsAdapter commentsAdapter = new CommentsAdapter(context,
                listOfComments);
        comments.setAdapter(commentsAdapter);
        LayoutUtils.layoutOverride(CommentsActivity.getSpinner(), View.GONE);
    }

}