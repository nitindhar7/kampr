package com.kampr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class KamprActivity extends Activity implements OnClickListener {

    private final String ACTIVITY_TAG = "KamprActivity";
    private final int VALIDATE_RESULT_CODE = 1;
    
    private EditText _loginUsername;
    private EditText _loginPassword;
    private Button _loginSubmit;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");

        if(sessionExists()) {
            Intent posts = new Intent(KamprActivity.this, PostsActivity.class);
            startActivity(posts);
        }
        else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.main);
            
            _loginUsername = (EditText)findViewById(R.id.login_username);
            _loginPassword = (EditText)findViewById(R.id.login_password);
            _loginSubmit = (Button)findViewById(R.id.login_submit);
            
            _loginSubmit.setOnClickListener(this);
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

    public void onClick(View src) {
        switch(src.getId()) {
            case R.id.login_submit:
                Intent validate = new Intent(KamprActivity.this, ValidateActivity.class);
                validate.putExtra("login_username", _loginUsername.getText().toString());
                validate.putExtra("login_password", _loginPassword.getText().toString());
                startActivityForResult(validate, VALIDATE_RESULT_CODE);
                break;
        }
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case VALIDATE_RESULT_CODE:
                if(resultCode == ValidateActivity.RESULT_SUCCESS)
                    Toast.makeText(getApplicationContext() , "Successfully Authenticated!", Toast.LENGTH_SHORT).show();
                else if(resultCode == ValidateActivity.RESULT_FAILURE)
                    Toast.makeText(getApplicationContext() , "Invalid username or password", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext() , "Unexpected error. Try again!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    
    protected boolean sessionExists() {
        SharedPreferences settings = getSharedPreferences(ValidateActivity.KAMPR_APP_PREFS, 0);
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

}