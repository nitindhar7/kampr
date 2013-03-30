package com.nitindhar.kampr;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nitindhar.kampr.async.LoginTask;
import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;

public class KamprActivity extends Activity implements OnClickListener {

    private static final int POST_QUIT_CODE = 2;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static SessionDao sessionDao;

    private TextView signUpLink;
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences(getResources()
                .getString(R.string.app_prefs), MODE_PRIVATE);

        SessionSharedPreferencesDao.setSharedPreferences(preferences);
        sessionDao = SessionSharedPreferencesDao.instance();

        if (!sessionDao.sessionWasEulaAccepted()) {
            AlertDialog alert = getEulaAlertBox();
            alert.show();
        }

        if (sessionDao.sessionExists()) {
            startPostsActivity();
        } else {
            setContentView(R.layout.login);

            signUpLink = (TextView) findViewById(R.id.sign_up_link);
            loginUsername = (EditText) findViewById(R.id.login_username);
            loginPassword = (EditText) findViewById(R.id.login_password);
            loginSubmit = (Button) findViewById(R.id.login_submit);

            loginSubmit.setOnClickListener(this);

            signUpLink.setAutoLinkMask(Linkify.WEB_URLS);
            signUpLink.setText(getResources().getString(
                    R.string.login_forrst_signup));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case POST_QUIT_CODE:
            finish();
            break;
        }
    }

    @Override
    public void onClick(View src) {
        switch (src.getId()) {
        case R.id.login_submit:
            attemptLogin();
            break;
        }
    }

    protected void attemptLogin() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginPassword.getWindowToken(), 0);

        ProgressDialog.show(KamprActivity.this, "",
                getResources().getString(R.string.login_dialog_message), true)
                .show();

        try {
            Future<Boolean> future = executor.submit(new LoginTask(loginUsername.getText().toString(), loginPassword.getText().toString()));
            if(future.get()) {
                startPostsActivity();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(
                                R.string.login_invalid_credentials),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Throwable t) {
            Toast.makeText(
                    getApplicationContext(),
                    getResources().getString(
                            R.string.login_unexpected_error),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private AlertDialog getEulaAlertBox() {
        LayoutInflater inflater = getLayoutInflater();
        View eula = inflater.inflate(R.layout.eula_dialog_box, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setNegativeButton("Disagree",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getResources().getString(
                                                R.string.eula_disagree),
                                        Toast.LENGTH_LONG).show();
                                finish();
                            }
                        })
                .setPositiveButton("Agree",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                sessionDao.sessionEulaAccepted();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.setView(eula, 0, 0, 0, 0);
        return alert;
    }

    protected void startPostsActivity() {
        Intent posts = new Intent(KamprActivity.this, PostsActivity.class);
        startActivityForResult(posts, 2);
    }

}