package com.nitindhar.kampr.async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.forrst.api.model.Notification;
import com.nitindhar.kampr.KamprActivity;
import com.nitindhar.kampr.PostsActivity;
import com.nitindhar.kampr.util.LayoutUtils;

public class NotificationsTask extends AsyncTask<Integer, Integer, List<Notification>> {
    
    protected static final ForrstAPI _forrst = new ForrstAPIClient();
    
    private Context _context;
    private ListView
    
    public NotificationsTask(Context context) {
        _context = context;
    }
    
    protected List<Notification> doInBackground(Integer... params) {
        String loginToken = _context.getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0).getString("login_token", null);
        Map<String,String> opts = new HashMap<String,String>();
        opts.put("grouped", "true");

        return _forrst.notifications(loginToken, opts);
    }
    
    protected void onPostExecute(List<Notification> listOfNotifications) {
        LayoutUtils.layoutOverride(PostsActivity.getSpinner(), View.GONE);
    }

}
