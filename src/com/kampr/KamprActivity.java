package com.kampr;

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
import android.widget.TextView;
import android.widget.Toast;

import com.kampr.util.LayoutUtils;
import com.kampr.util.NetworkUtils;

public class KamprActivity extends Activity implements OnClickListener, OnKeyListener {

    public static final String KAMPR_APP_PREFS = "KamprAppPrefs";

    private final int LOGIN_RESULT_CODE = 1;
    private final int ENTER_KEY_CODE = 66;
    
    private TextView _actionbarLogo;
    private TextView _actionbarByline;
    private TextView _loginLabelUsername;
    private TextView _loginLabelPassword;
    private EditText _loginUsername;
    private EditText _loginPassword;
    private Button _loginSubmit;
    private ProgressDialog _dialog;
    private SharedPreferences _settings;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        _settings = getSharedPreferences(KAMPR_APP_PREFS, 0);

        if(sessionExists() && NetworkUtils.isOnline(getApplicationContext())) {
            startPostsActivity();
        }
        else {
            setContentView(R.layout.login);
            
            _actionbarLogo = (TextView)findViewById(R.id.actionbar_logo);
            _actionbarByline = (TextView)findViewById(R.id.actionbar_byline);
            _loginLabelUsername = (TextView)findViewById(R.id.login_label_username);
            _loginLabelPassword = (TextView)findViewById(R.id.login_label_password);
            _loginUsername = (EditText)findViewById(R.id.login_username);
            _loginPassword = (EditText)findViewById(R.id.login_password);
            _loginSubmit = (Button)findViewById(R.id.login_submit);
            
            _loginSubmit.setOnClickListener(this);
            _loginUsername.setOnKeyListener(this);
            _loginPassword.setOnKeyListener(this);
            
            LayoutUtils.setFont(this, _actionbarLogo, LayoutUtils.FONT_BOLD);
            LayoutUtils.setFont(this, _actionbarByline);
            LayoutUtils.setFont(this, _loginSubmit);
            LayoutUtils.setFont(this, _loginLabelUsername);
            LayoutUtils.setFont(this, _loginLabelPassword);
            LayoutUtils.setFont(this, _loginUsername);
            LayoutUtils.setFont(this, _loginPassword);
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

}