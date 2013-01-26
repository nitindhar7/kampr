package com.nitindhar.kampr;

import android.app.Activity;
import android.os.Bundle;

import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;

public class LogoutActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SessionDao sessionDao = SessionSharedPreferencesDao.instance();

        if (sessionDao.removeSession()) {
            setResult(getResources().getInteger(R.integer.logout_success));
            finish();
        } else {
            setResult(getResources().getInteger(R.integer.logout_failure));
            finish();
        }
    }

}