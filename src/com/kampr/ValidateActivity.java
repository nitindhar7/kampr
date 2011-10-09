package com.kampr;

import org.json.JSONObject;

import com.forrst.api.ForrstAPI;
import com.forrst.api.ForrstAPIClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class ValidateActivity extends Activity {
    
    private final String ACTIVITY_TAG = "ValidateActivity";
    
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
        
        if(validateCredentialsFormat(_loginUsername, _loginPassword) &&
                validateCredentials(_loginUsername, _loginPassword)) {
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
        JSONObject json = _forrst.usersAuth(username, password);
        if(json.has("token") && json.has("user_id")) {
            // TODO: save token/user_id to session
            return true;
        }
        else
            return false;
    }

}