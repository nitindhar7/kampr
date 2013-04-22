package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;

import com.nitindhar.forrst.model.Comment;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.CommentsAdapter;
import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;
import com.nitindhar.kampr.models.CommentsDecorator;
import com.nitindhar.kampr.util.ForrstUtil;
import com.nitindhar.kampr.util.ImageUtils;

public class CommentsTask extends
        AsyncTask<Integer, Integer, List<CommentsDecorator>> {

    private final Context context;
    private final ListView comments;

    public CommentsTask(Context context, ListView comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    protected List<CommentsDecorator> doInBackground(Integer... params) {
        List<CommentsDecorator> listOfComments = new ArrayList<CommentsDecorator>();

        SessionDao sessionDao = SessionSharedPreferencesDao.instance();

        for (Comment comment : ForrstUtil.client().postComments(
                sessionDao.getSessionToken(), params[0])) {
            CommentsDecorator cd = new CommentsDecorator();
            cd.setComment(comment);
            cd.setUserIcon(ImageUtils.fetchUserIcon(context, comment.getUser()
                    .getPhoto().getMediumUrl(), R.drawable.forrst_default_25));
            listOfComments.add(cd);
        }

        return listOfComments;
    }

    @Override
    protected void onPostExecute(List<CommentsDecorator> listOfComments) {
        CommentsAdapter commentsAdapter = new CommentsAdapter(context,
                listOfComments);
        comments.setAdapter(commentsAdapter);
    }

}