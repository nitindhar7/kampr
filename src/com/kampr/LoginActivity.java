package com.kampr;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;
import com.forrst.api.util.ForrstAuthenticationException;

public class LoginActivity extends Activity {
    
    private final String ACTIVITY_TAG = "LoginActivity";

    protected static final int RESULT_SUCCESS = 1;
    protected static final int RESULT_FAILURE = -1;
    
    private String _loginUsername;
    private String _loginPassword;
    
    private ForrstAPI _forrst;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");

        _forrst = new ForrstAPIClient();
        
        _loginUsername = getIntent().getStringExtra("login_username");
        _loginPassword = getIntent().getStringExtra("login_password");
        
        boolean areCredentialFormatsValid = validateCredentialsFormat(_loginUsername, _loginPassword);
        boolean areCredentialsValid = validateCredentials(_loginUsername, _loginPassword);
        
        if(areCredentialFormatsValid && areCredentialsValid) {
            setResult(RESULT_SUCCESS);
            finish();
        }
        else {
            setResult(RESULT_FAILURE);
            finish();
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_TAG, "onStart");
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_TAG, "onPause");
    }
    
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_TAG, "onStop");
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_TAG, "onDestroy");
    }
    
    private boolean validateCredentialsFormat(String username, String password) {
        if(username.length() > 0 && password.length() > 0)
            return true;
        else
            return false;
    }
    
    private boolean validateCredentials(String username, String password) {
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
        } catch (ForrstAuthenticationException e) {
            return false;
        } catch (JSONException e) {
            throw new RuntimeException("Error retrieving user data from Forrst authentication", e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error validating user", e);
        }
        
    }

}