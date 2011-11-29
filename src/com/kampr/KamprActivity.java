package com.kampr;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class KamprActivity extends Activity implements OnClickListener, OnKeyListener {

    public static final String KAMPR_APP_PREFS = "KamprAppPrefs";

    private final int LOGIN_RESULT_CODE = 1;
    private final int ENTER_KEY_CODE = 66;
    
    private EditText _loginUsername;
    private EditText _loginPassword;
    private Button _loginSubmit;
    private ProgressDialog _dialog;
    private SharedPreferences _settings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trustAllHosts();
        
        _settings = getSharedPreferences(KAMPR_APP_PREFS, 0);

        if(sessionExists()) {
            startPostsActivity();
        }
        else {
            setContentView(R.layout.main);
            
            _loginUsername = (EditText)findViewById(R.id.login_username);
            _loginPassword = (EditText)findViewById(R.id.login_password);
            _loginSubmit = (Button)findViewById(R.id.login_submit);
            
            _loginSubmit.setOnClickListener(this);
            _loginUsername.setOnKeyListener(this);
            _loginPassword.setOnKeyListener(this);
        }
    }

    public void onClick(View src) {
        switch(src.getId()) {
            case R.id.login_submit:
                attemptLogin();
                break;
        }
    }
    
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(keyCode == ENTER_KEY_CODE) {
            attemptLogin();
        }
        return false;
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case LOGIN_RESULT_CODE:
                if(resultCode == LoginActivity.RESULT_SUCCESS) {
                    startPostsActivity();
                }
                else if(resultCode == LoginActivity.RESULT_FAILURE)
                    Toast.makeText(getApplicationContext() , "Invalid username or password", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                _dialog.cancel();
                break;
        }
    }
    
    protected boolean sessionExists() {
        if(_settings.getString("login_username", null) != null &&
                _settings.getString("login_password", null) != null &&
                _settings.getString("login_token", null) != null &&
                _settings.getString("login_user_id", null) != null) {
            return true;
        }
        else {
            return false;
        }
    }
    
    protected void startPostsActivity() {
        Intent posts = new Intent(KamprActivity.this, PostsActivity.class);
        startActivity(posts);
    }
    
    protected void attemptLogin() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_loginPassword.getWindowToken(), 0);

        _dialog = ProgressDialog.show(KamprActivity.this, "", "Logging in. Please wait...", true);
        Intent validate = new Intent(KamprActivity.this, LoginActivity.class);
        validate.putExtra("login_username", _loginUsername.getText().toString());
        validate.putExtra("login_password", _loginPassword.getText().toString());
        startActivityForResult(validate, LOGIN_RESULT_CODE);
    }
    
    /**
     * Trust every server - dont check for any certificate
     * 1. Create a trust manager that does not validate certificate chains
     * 2. Install the all-trusting trust manager
     */
    protected static void trustAllHosts() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
        }};

        SSLContext sc;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error installing all-trusting trust manager: algorithm not found", e);
        } catch (KeyManagementException e) {
            throw new RuntimeException("Error installing all-trusting trust manager: problems managing key", e);
        }
    }

}