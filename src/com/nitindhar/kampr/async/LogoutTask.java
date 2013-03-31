package com.nitindhar.kampr.async;

import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;

public class LogoutTask implements Runnable {

    private static final SessionDao sessionDao = SessionSharedPreferencesDao.instance();

    @Override
    public void run() {
        sessionDao.removeSession();
    }

}