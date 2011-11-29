package com.kampr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.forrst.api.util.ForrstAuthenticationException;
import com.kampr.util.KamprUtils;

public class LoginActivity extends Activity {

    protected static final int RESULT_SUCCESS = 1;
    protected static final int RESULT_FAILURE = -1;
    
    private String _loginUsername;
    private String _loginPassword;
    
    private static ForrstAPI _forrst = new ForrstAPIClient();
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _loginUsername = getIntent().getStringExtra("login_username");
        _loginPassword = getIntent().getStringExtra("login_password");

        if(validateCredentialsFormat(_loginUsername, _loginPassword) && KamprUtils.isOnline(getApplicationContext())) {
            try {
                validateCredentials(_loginUsername, _loginPassword);
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
        if(username.length() > 0 && password.length() > 0)
            return true;
        else
            return false;
    }
    
    private boolean validateCredentials(String username, String password) throws ForrstAuthenticationException {
        JSONObject json = null;
        try {
            json = _forrst.usersAuth(username, password);
            SharedPreferences settings = getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0);
            SharedPreferences.Editor editor = settings.edit();

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashedPassword = md.digest(_loginPassword.getBytes());
            
            editor.putString("login_username", _loginUsername);
            editor.putString("login_password", hashedPassword.toString());
            editor.putString("login_token", json.getString("token"));
            editor.putString("login_user_id", json.getString("user_id"));
            editor.commit();
            return true;
        } catch (JSONException e) {
            throw new RuntimeException("Error retrieving user data from Forrst authentication", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error validating user", e);
        }
        
    }

}