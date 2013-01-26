package com.nitindhar.kampr.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.http.HttpProvider;
import com.nitindhar.forrst.model.Notification;
import com.nitindhar.kampr.PostsActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.NotificationsAdapter;
import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class NotificationsTask extends
        AsyncTask<Integer, Integer, List<Notification>> {

    protected static final ForrstAPI forrst = new ForrstAPIClient(
            HttpProvider.JAVA_NET);

    private final Context context;
    private final ListView notificationsList;
    private final List<Bitmap> userIcons;

    public NotificationsTask(Context context, ListView notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
        this.userIcons = new ArrayList<Bitmap>();
    }

    @Override
    protected List<Notification> doInBackground(Integer... params) {
        SessionDao sessionDao = SessionSharedPreferencesDao.instance();

        Map<String, String> opts = new HashMap<String, String>();
        opts.put("grouped", "true");

        List<Notification> listOfNotifications = forrst.notifications(
                sessionDao.getSessionToken(), opts);
        for (Notification notification : listOfNotifications) {
            userIcons.add(ImageUtils.fetchImageBitmap(notification.getData()
                    .getPhoto()));
        }

        return listOfNotifications;
    }

    @Override
    protected void onPostExecute(List<Notification> listOfNotifications) {
        if (listOfNotifications.size() > 0) {
            LayoutUtils.layoutOverride(PostsActivity.getNotificationbar(),
                    View.VISIBLE);
            RelativeLayout handle = (RelativeLayout) PostsActivity
                    .getNotificationbar().findViewById(R.id.handle);
            TextView notificationIcon = (TextView) handle
                    .findViewById(R.id.notification_icon);
            notificationIcon.setText(Integer.toString(listOfNotifications
                    .size()));

            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(
                    context, listOfNotifications, userIcons);
            notificationsList.setAdapter(notificationsAdapter);
        }
    }

}