package com.nitindhar.kampr.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.SharedPreferences;

import com.nitindhar.kampr.models.Session;

public class SessionSharedPreferencesDao implements SessionDao {

    private static final SessionDao INSTANCE = new SessionSharedPreferencesDao();
    private static final String SESSION_KEY_USERNAME = "login_username";
    private static final String SESSION_KEY_PASSWORD = "login_password";
    private static final String SESSION_KEY_TOKEN = "login_token";
    private static final String SESSION_KEY_USER_ID = "login_user_id";
    private static final String SESSION_KEY_EULA_ACCEPTED = "agreed_to_eula";
    private static final String SESSION_KEY_POST_TYPE = "post_type";
    private static final String SESSION_PASSWORD_HASH_ALGO = "MD5";

    private static SharedPreferences preferences;

    public static SessionDao instance() {
        return INSTANCE;
    }

    public static void setSharedPreferences(SharedPreferences preferences) {
        SessionSharedPreferencesDao.preferences = preferences;
    }

    @Override
    public boolean sessionExists() {
        if (preferences.getString(SESSION_KEY_TOKEN, null) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean storeSession(Session session)
            throws NoSuchAlgorithmException {
        SharedPreferences.Editor editor = preferences.edit();
        MessageDigest md = MessageDigest
                .getInstance(SESSION_PASSWORD_HASH_ALGO);
        byte[] passwordHash = md.digest(session.getPassword().getBytes());

        editor.putString(SESSION_KEY_USERNAME, session.getUsername());
        editor.putString(SESSION_KEY_PASSWORD, passwordHash.toString());
        editor.putString(SESSION_KEY_TOKEN, session.getToken());
        editor.putString(SESSION_KEY_USER_ID,
                Integer.toString(session.getUserId()));
        return editor.commit();
    }

    @Override
    public boolean sessionWasEulaAccepted() {
        return preferences.getBoolean(SESSION_KEY_EULA_ACCEPTED, false);
    }

    @Override
    public boolean sessionEulaAccepted() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(SESSION_KEY_EULA_ACCEPTED, true);
        return editor.commit();
    }

    @Override
    public boolean removeSession() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(SESSION_KEY_USERNAME);
        editor.remove(SESSION_KEY_PASSWORD);
        editor.remove(SESSION_KEY_TOKEN);
        editor.remove(SESSION_KEY_USER_ID);
        editor.remove(SESSION_KEY_POST_TYPE);
        return editor.commit();
    }

    @Override
    public String getSessionToken() {
        return preferences.getString(SESSION_KEY_TOKEN, null);
    }

    @Override
    public boolean storeSessionPostType(String postType) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SESSION_KEY_POST_TYPE, postType);
        return editor.commit();
    }

    @Override
    public String getSessionPostType() {
        return preferences.getString(SESSION_KEY_POST_TYPE, null);
    }

}