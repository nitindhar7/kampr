package com.kampr;

import android.app.Activity;
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
    
    private EditText _loginUsername;
    private EditText _loginPassword;
    private Button _loginSubmit;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_TAG, "onCreate");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        _loginUsername = (EditText)findViewById(R.id.login_username);
        _loginPassword = (EditText)findViewById(R.id.login_password);
        _loginSubmit = (Button)findViewById(R.id.login_submit);
        
        _loginSubmit.setOnClickListener(this);
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
                String loginUsername = _loginUsername.getText().toString();
                String loginPassword = _loginPassword.getText().toString();
                break;
        }
    }

}