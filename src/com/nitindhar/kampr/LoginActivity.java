package com.nitindhar.kampr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.nitindhar.forrst.ForrstAPI;
import com.nitindhar.forrst.ForrstAPIClient;
import com.nitindhar.forrst.http.HttpProvider;
import com.nitindhar.forrst.model.Auth;
import com.nitindhar.forrst.util.ForrstAuthenticationException;
import com.nitindhar.kampr.util.NetworkUtils;

public class LoginActivity extends Activity {

    protected static final int RESULT_SUCCESS = 1;
    protected static final int RESULT_FAILURE = -1;

    private String loginUsername;
    private String loginPassword;

    private static ForrstAPI _forrst = new ForrstAPIClient(HttpProvider.JAVA_NET);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginUsername = getIntent().getStringExtra("login_username");
        loginPassword = getIntent().getStringExtra("login_password");

        if(validateCredentialsFormat(loginUsername, loginPassword) && NetworkUtils.isOnline(getApplicationContext())) {
            try {
                validateCredentials(loginUsername, loginPassword);
                setResult(RESULT_SUCCESS);
            } catch (ForrstAuthenticationException e) {
                setResult(RESULT_FAILURE);
            } finally {
                finish();
            }
        }
        else {
            setResult(RESULT_FAILURE);
            finish();
        }
    }

    private boolean validateCredentialsFormat(String username, String password) {
        if(username.length() > 0 && password.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateCredentials(String username, String password) throws ForrstAuthenticationException {
        try {
            Auth auth = _forrst.usersAuth(username, password);
            SharedPreferences settings = getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0);
            SharedPreferences.Editor editor = settings.edit();

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedPassword = md.digest(loginPassword.getBytes());

            editor.putString("login_username", loginUsername);
            editor.putString("login_password", hashedPassword.toString());
            editor.putString("login_token", auth.getAccessToken());
            editor.putString("login_user_id", Integer.toString(auth.getUserId()));
            editor.commit();
            return true;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error validating user", e);
        }

    }

}