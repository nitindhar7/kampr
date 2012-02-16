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

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.forrst.api.model.Notification;
import com.nitindhar.kampr.KamprActivity;
import com.nitindhar.kampr.PostsActivity;
import com.nitindhar.kampr.R;
import com.nitindhar.kampr.adapters.NotificationsAdapter;
import com.nitindhar.kampr.util.ImageUtils;
import com.nitindhar.kampr.util.LayoutUtils;

public class NotificationsTask extends AsyncTask<Integer, Integer, List<Notification>> {
    
    protected static final ForrstAPI _forrst = new ForrstAPIClient();
    
    private Context _context;
    private ListView _notificationsList;
    
    public NotificationsTask(Context context, ListView notificationsList) {
        _context = context;
        _notificationsList = notificationsList;
    }
    
    protected List<Notification> doInBackground(Integer... params) {
        String loginToken = _context.getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0).getString("login_token", null);
        Map<String,String> opts = new HashMap<String,String>();
        opts.put("grouped", "true");

        return _forrst.notifications(loginToken, opts);
    }
    
    protected void onPostExecute(List<Notification> listOfNotifications) {
        if(listOfNotifications.size() > 0) {
            LayoutUtils.layoutOverride(PostsActivity.getNotificationbar(), View.VISIBLE);
            RelativeLayout handle = (RelativeLayout) PostsActivity.getNotificationbar().findViewById(R.id.handle);
            TextView notificationIcon = (TextView) handle.findViewById(R.id.notification_icon);
            notificationIcon.setText(Integer.toString(listOfNotifications.size()));
            
            List<Bitmap> userIcons = new ArrayList<Bitmap>();
            for(Notification notification : listOfNotifications) {
                userIcons.add(ImageUtils.fetchImageBitmap(notification.getData().getPhoto()));
            }
            
            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(_context, listOfNotifications, userIcons);
            _notificationsList.setAdapter(notificationsAdapter);
        }
    }

}
