package com.nitindhar.kampr;

import android.app.Activity;
import android.content.SharedPreferences;
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

    protected static final int RESULT_SUCCESS = 1;
    protected static final int RESULT_FAILURE = -1;

    private static SessionDao sessionDao;

    private String loginUsername;
    private String loginPassword;

    private static ForrstAPI forrst = new ForrstAPIClient(HttpProvider.JAVA_NET);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(
                KamprActivity.KAMPR_APP_PREFS, MODE_PRIVATE);

        sessionDao = new SessionSharedPreferencesDao(preferences);

        loginUsername = getIntent().getStringExtra("login_username");
        loginPassword = getIntent().getStringExtra("login_password");

        if (!NetworkUtils.isOnline(getApplicationContext())) {
            setResult(RESULT_FAILURE);
            finish();
        } else {
            try {
                boolean validCredentials = validateCredentials(loginUsername,
                        loginPassword);
                if (validCredentials) {
                    setResult(RESULT_SUCCESS);
                } else {
                    setResult(RESULT_FAILURE);
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