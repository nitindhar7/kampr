package com.nitindhar.kampr;

import android.app.Activity;
import android.os.Bundle;

import com.google.common.base.Optional;
import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.http.HttpProvider;
import com.nitindhar.forrst.model.Auth;
import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;
import com.nitindhar.kampr.models.Session;
import com.nitindhar.kampr.util.NetworkUtils;

public class LoginActivity extends Activity {

    protected static final int LOGIN_SUCCESS = 1;
    protected static final int LOGIN_FAILURE = -1;

    private static SessionDao sessionDao;

    private static ForrstAPI forrst = new ForrstAPIClient(HttpProvider.JAVA_NET);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionDao = SessionSharedPreferencesDao.instance();

        if (!NetworkUtils.isOnline(getApplicationContext())) {
            setResult(LOGIN_FAILURE);
            finish();
        } else {
            try {
                boolean validCredentials = validateCredentials(getIntent()
                        .getStringExtra("login_username"), getIntent()
                        .getStringExtra("login_password"));
                if (validCredentials) {
                    setResult(LOGIN_SUCCESS);
                } else {
                    setResult(LOGIN_FAILURE);
                }
            } finally {
                finish();
            }
        }
    }

    private boolean validateCredentials(String username, String password) {
        boolean validCredentials = false;
        try {
            Optional<Auth> auth = forrst.usersAuth(username, password);
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