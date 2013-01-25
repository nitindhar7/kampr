package com.nitindhar.kampr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;

public class LogoutActivity extends Activity {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILURE = -1;

    private static SessionDao sessionDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(
                KamprActivity.KAMPR_APP_PREFS, MODE_PRIVATE);
        sessionDao = new SessionSharedPreferencesDao(preferences);

        if (sessionDao.removeSession()) {
            setResult(RESULT_SUCCESS);
            finish();
        } else {
            setResult(RESULT_FAILURE);
            finish();
        }
    }

}
