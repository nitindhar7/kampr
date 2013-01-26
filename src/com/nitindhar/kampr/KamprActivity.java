package com.nitindhar.kampr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nitindhar.kampr.data.SessionDao;
import com.nitindhar.kampr.data.SessionSharedPreferencesDao;
import com.nitindhar.kampr.util.NetworkUtils;
import com.nitindhar.kampr.util.SpanUtils;

public class KamprActivity extends Activity implements OnClickListener,
        OnKeyListener {

    private static final int LOGIN_RESULT_CODE = 1;
    private static final int POST_QUIT_CODE = 2;

    private static SessionDao sessionDao;

    private TextView signUpLink;
    private TextView loginUsernameLabel;
    private TextView loginPasswordLabel;
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginSubmit;
    private ProgressDialog dialog;

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

        if (sessionDao.sessionExists()
                && NetworkUtils.isOnline(getApplicationContext())) {
            startPostsActivity();
        } else {
            setContentView(R.layout.login);

            signUpLink = (TextView) findViewById(R.id.sign_up_link);
            loginUsernameLabel = (TextView) findViewById(R.id.login_label_username);
            loginPasswordLabel = (TextView) findViewById(R.id.login_label_password);
            loginUsername = (EditText) findViewById(R.id.login_username);
            loginPassword = (EditText) findViewById(R.id.login_password);
            loginSubmit = (Button) findViewById(R.id.login_submit);

            loginSubmit.setOnClickListener(this);
            loginUsername.setOnKeyListener(this);
            loginPassword.setOnKeyListener(this);

            signUpLink.setAutoLinkMask(Linkify.WEB_URLS);
            signUpLink.setText(getResources().getString(
                    R.string.login_forrst_signup));

            SpanUtils.setFont(this, signUpLink, SpanUtils.FONT_ITALIC);
            SpanUtils.setFont(this, loginUsernameLabel);
            SpanUtils.setFont(this, loginPasswordLabel);
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == getResources().getInteger(R.integer.enter_key_code)) {
            attemptLogin();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case LOGIN_RESULT_CODE:
            switch (resultCode) {
            case LoginActivity.LOGIN_SUCCESS:
                startPostsActivity();
                break;
            case LoginActivity.LOGIN_FAILURE:
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(
                                R.string.login_invalid_credentials),
                        Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(
                        getApplicationContext(),
                        getResources().getString(
                                R.string.login_unexpected_error),
                        Toast.LENGTH_SHORT).show();
            }
            dialog.cancel();
            break;
        case POST_QUIT_CODE:
            finish();
            break;
        }
    }

    protected void startPostsActivity() {
        Intent posts = new Intent(KamprActivity.this, PostsActivity.class);
        startActivityForResult(posts, 2);
    }

    protected void attemptLogin() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(loginPassword.getWindowToken(), 0);

        dialog = ProgressDialog.show(KamprActivity.this, "",
                "Logging in. Please wait...", true);
        Intent validate = new Intent(KamprActivity.this, LoginActivity.class);
        validate.putExtra("login_username", loginUsername.getText().toString());
        validate.putExtra("login_password", loginPassword.getText().toString());
        startActivityForResult(validate, LOGIN_RESULT_CODE);
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
                .setPositiveButton("Yes, I agree",
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

}