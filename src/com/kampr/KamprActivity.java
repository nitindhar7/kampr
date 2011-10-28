package com.kampr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;

public class KamprActivity extends Activity implements OnClickListener {

    private final String ACTIVITY_TAG = "KamprActivity";
    private final int LOGIN_RESULT_CODE = 1;
    protected static final String KAMPR_APP_PREFS = "KamprAppPrefs";
    
    private EditText _loginUsername;
    private EditText _loginPassword;
    private Button _loginSubmit;
    private ProgressDialog _dialog;
    private ActionBar _actionBar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");

        if(sessionExists())
            startPostsActivity();
        else {
            setContentView(R.layout.main);
            
            _actionBar = (ActionBar) findViewById(R.id.actionbar);
            _actionBar.setTitle("kampr");
            
            _loginUsername = (EditText)findViewById(R.id.login_username);
            _loginPassword = (EditText)findViewById(R.id.login_password);
            _loginSubmit = (Button)findViewById(R.id.login_submit);
            
            _loginSubmit.setOnClickListener(this);
        }
    }

    public void onClick(View src) {
        switch(src.getId()) {
            case R.id.login_submit:
                _dialog = ProgressDialog.show(KamprActivity.this, "", "Logging in. Please wait...", true);
                Intent validate = new Intent(KamprActivity.this, LoginActivity.class);
                validate.putExtra("login_username", _loginUsername.getText().toString());
                validate.putExtra("login_password", _loginPassword.getText().toString());
                startActivityForResult(validate, LOGIN_RESULT_CODE);
                break;
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case LOGIN_RESULT_CODE:
                if(resultCode == LoginActivity.RESULT_SUCCESS) {
                    _dialog.cancel();
                    startPostsActivity();
                }
                else if(resultCode == LoginActivity.RESULT_FAILURE)
                    Toast.makeText(getApplicationContext() , "Invalid username or password", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    
    protected boolean sessionExists() {
        SharedPreferences settings = getSharedPreferences(KAMPR_APP_PREFS, 0);
        if(settings.getString("login_username", null) != null &&
                settings.getString("login_password", null) != null &&
                settings.getString("login_token", null) != null &&
                settings.getString("login_user_id", null) != null) {
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

}