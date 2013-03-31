package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ListView;

import com.nitindhar.forrst.model.Notification;
import com.nitindhar.kampr.adapters.NotificationsAdapter;
import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;
import com.nitindhar.kampr.util.ForrstUtil;
import com.nitindhar.kampr.util.ImageUtils;

public class NotificationsTask extends
        AsyncTask<Integer, Integer, List<Notification>> {

    private final Context context;
    private final ListView notificationsList;
    private final List<Bitmap> userIcons;

    public NotificationsTask(Context context, ListView notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
        userIcons = new ArrayList<Bitmap>();
    }

    @Override
    protected List<Notification> doInBackground(Integer... params) {
        SessionDao sessionDao = SessionSharedPreferencesDao.instance();

        Map<String, String> opts = new HashMap<String, String>();
        opts.put("grouped", "true");

        List<Notification> listOfNotifications = ForrstUtil.client()
                .notifications(sessionDao.getSessionToken(), opts);
        for (Notification notification : listOfNotifications) {
            userIcons.add(ImageUtils.fetchImageBitmap(notification.getData()
                    .getPhoto()));
        }

        return listOfNotifications;
    }

    @Override
    protected void onPostExecute(List<Notification> listOfNotifications) {
        if (listOfNotifications.size() > 0) {
            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(
                    context, listOfNotifications, userIcons);
            notificationsList.setAdapter(notificationsAdapter);
        }
    }

}