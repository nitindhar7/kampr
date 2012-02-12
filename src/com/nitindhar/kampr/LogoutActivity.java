package com.nitindhar.kampr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LogoutActivity extends Activity {

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_FAILURE = -1;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(KamprActivity.KAMPR_APP_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("login_username");
        editor.remove("login_password");
        editor.remove("login_token");
        editor.remove("login_user_id");
        
        if(editor.commit()) {
            setResult(RESULT_SUCCESS);
            finish();
        }
        else {
            setResult(RESULT_FAILURE);
            finish();
        }
    }

}
