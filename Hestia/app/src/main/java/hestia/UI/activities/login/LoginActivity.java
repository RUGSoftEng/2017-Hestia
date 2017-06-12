package hestia.UI.activities.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import hestia.UI.HestiaApplication;
import hestia.UI.activities.home.HomeActivity;
import hestia.UI.dialogs.DiscoverServerDialog;
import hestia.backend.NetworkHandler;

/**
 * This class handles the login activity.
 * It takes the fields from the layout, gets the values the user inputs and validates it.
 * Furthermore it first checks the shared preferences of the phone if the user is remembered.
 */

public class LoginActivity extends FragmentActivity {
    private Button loginButton;
    private Button setServerButton;
    private EditText userField, passField;
    private CheckBox rememberButton;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private TextView attemptsText;
    private int counter;
    private String username, password;
    private static final String SALT = "RuGg3Ds0ftWarE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        counter = Integer.valueOf(getString(R.string.initialCount));
        loginPreferences = getSharedPreferences(getString(R.string.loginPrefs), MODE_PRIVATE);
        if(rememberMeSelected()){
            gotoMainActivity();
            return;
        }
        clearSaveLogin();
        buildView();
    }

    private void buildView() {
        initLoginButton();
        initServerButton();
        addWidgets();
    }

    private void addWidgets() {
        loginButton = (Button) findViewById(R.id.loginButton);
        userField = (EditText) findViewById(R.id.username);
        passField = (EditText) findViewById(R.id.password);
        rememberButton = (CheckBox) findViewById(R.id.rememberButton);
        attemptsText = (TextView) findViewById(R.id.textView4);
        attemptsText.setVisibility(View.GONE);
    }

    private void initLoginButton() {
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userField.getText().toString();
                password = passField.getText().toString();
                if (checkCredentials(username, password)) {
                    if (rememberButton.isChecked()) {
                        setSharedPreferences(username, password, true);
                    } else {
                        clearSaveLogin();
                    }
                    showLoginToast(getString(R.string.correctLoginToast));
                    gotoMainActivity();
                } else {
                    showLoginToast(getString(R.string.incorrectLoginToast));
                    decreaseLoginAttempts();
                }
            }
        });
    }

    private void initServerButton() {
        setServerButton = (Button) findViewById(R.id.setServerButton);
        setServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetIpDialog();
            }
        });
    }

    private void showSetIpDialog() {
        DiscoverServerDialog fragment = DiscoverServerDialog.newInstance();
        fragment.setNetworkHandler(((HestiaApplication) getApplication()).getNetworkHandler());
        fragment.show(getSupportFragmentManager(), "tag");
    }

    private boolean rememberMeSelected() {
        if (loginPreferences.getString(getString(R.string.loginPrefsUser), "").equals("")) {
            setSharedPreferences(getString(R.string.standardUser), getString(R.string.standardPass), false);
        }
        Boolean saveLogin = loginPreferences.getBoolean(getString(R.string.saveLogin), false);
        return saveLogin;
    }

    private boolean checkCredentials(String username,String password){
        String corrUser = loginPreferences.getString(getString(R.string.loginPrefsUser), hashString("admin"));
        String corrPass = loginPreferences.getString(getString(R.string.loginPrefsPass), hashString("password"));
        String hashedUser = hashString(username);
        String hashedPass = hashString(password);
        return (hashedUser.equals(corrUser) && hashedPass.equals(corrPass));
    }

    private void gotoMainActivity() {
        Intent toIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(toIntent);
        finish();
    }

    private void setSharedPreferences(String username, String password, Boolean saveLogin) {
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putBoolean(getString(R.string.saveLogin), saveLogin);
        loginPrefsEditor.putString(getString(R.string.loginPrefsUser), hashString(username));
        loginPrefsEditor.putString(getString(R.string.loginPrefsPass), hashString(password));
        loginPrefsEditor.apply();
    }

    private void clearSaveLogin() {
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putBoolean(getString(R.string.saveLogin), false);
        loginPrefsEditor.apply();
    }

    private void decreaseLoginAttempts() {
        attemptsText.setVisibility(View.VISIBLE);
        counter--;
        attemptsText.setText(String.format(Locale.getDefault(), "%d", counter));
        if (counter == 0) {
            loginButton.setEnabled(false);
        }
    }

    private void showLoginToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }

    public static String hashString(String string) {
        String hashedString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(SALT.getBytes("UTF-8"));
            byte[] bytes = md.digest(string.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedString = sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return hashedString;
    }
}