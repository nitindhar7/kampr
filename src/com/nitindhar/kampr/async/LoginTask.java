package com.nitindhar.kampr.async;

import java.util.concurrent.Callable;

import com.google.common.base.Optional;
import com.nitindhar.forrst.model.Auth;
import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;
import com.nitindhar.kampr.models.Session;
import com.nitindhar.kampr.util.ForrstUtil;

public class LoginTask implements Callable<Boolean> {

    private static SessionDao sessionDao = SessionSharedPreferencesDao.instance();

    private final String username;
    private final String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Boolean call() {
        boolean validCredentials = false;
        try {
            Optional<Auth> auth = ForrstUtil.client().usersAuth(username, password);
            if (auth.isPresent()) {
                Session session = new Session(username, password, auth.get()
                        .getAccessToken(), auth.get().getUserId());

                validCredentials = sessionDao.storeSession(session);
            }
        } catch (Exception e) {
        }
        return validCredentials;
    }

}