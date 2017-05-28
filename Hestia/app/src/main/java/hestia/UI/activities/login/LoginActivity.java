package hestia.UI.activities.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import hestia.UI.activities.home.HomeActivity;

/**
 *  This class handles the login activity.
 *  It takes the fields from the layout, gets the values the user inputs and validates it.
 *  Furthermore it first checks the shared preferences of the phone if the user is remembered.
 */

public class LoginActivity extends Activity  {
    private Button loginButton;
    private EditText userField,passField;
    private CheckBox rememberButton;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private TextView attemptsText;
    private int counter = 10;
    private String username,password;
    public static final String LOGIN_PREFERENCES = "LoginPreferences";
    public static final String prefsUser = "username";
    public static final String prefsPass = "password";
    private static final String SALT = "RuGg3Ds0ftWarE";
    private final String intentExtra = "login";
    private final String correctLoginToast = "Correct, redirecting.";
    private final String incorrectLoginToast = "Invalid credentials.";
    private final String saveLoginString = "saveLogin";
    private final String standardUser = "admin";
    private final String standardPass = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent fromIntent = getIntent();
        String extra = fromIntent.getStringExtra(intentExtra);

        loginPreferences = getSharedPreferences(LOGIN_PREFERENCES, MODE_PRIVATE);
        if(loginPreferences.getString(prefsUser,"").equals("")){
            setSharedPreferences(standardUser, standardPass,false);
        }
        Boolean saveLogin = loginPreferences.getBoolean(saveLoginString, false);
        if (saveLogin) {
            if(extra==null) {
                gotoMainActivity();
            }
            else{
                clearSaveLogin();
            }
        }

        loginButton = (Button)findViewById(R.id.loginButton);
        userField = (EditText)findViewById(R.id.username);
        passField = (EditText)findViewById(R.id.password);
        rememberButton = (CheckBox) findViewById(R.id.rememberButton);
        attemptsText = (TextView)findViewById(R.id.textView4);
        attemptsText.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            /* Button has been clicked, get and check credentials */
            @Override
            public void onClick(View v) {
                username = userField.getText().toString();
                password = passField.getText().toString();
                if(checkCredentials(username,password)){
                    if (rememberButton.isChecked()) {
                        setSharedPreferences(username,password,true);
                    } else {
                        clearSaveLogin();
                    }
                    showLoginToast(correctLoginToast);
                    gotoMainActivity();
                }else{
                    showLoginToast(incorrectLoginToast);
                    editLoginAttempts();
                }
            }
        });
    }

    private boolean checkCredentials(String username,String password){
        String corrPass = loginPreferences.getString(prefsPass, "");
        String corrUser = loginPreferences.getString(prefsUser, "");
        String hashedUser = hashString(username);
        String hashedPass = hashString(password);
        return(hashedUser.equals(corrUser)&&hashedPass.equals(corrPass));
    }

    private void gotoMainActivity(){
        Intent toIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(toIntent);
        finish();
    }

    private void setSharedPreferences(String username, String password, Boolean saveLogin){
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putBoolean(saveLoginString, saveLogin);
        loginPrefsEditor.putString(prefsUser, hashString(username));
        loginPrefsEditor.putString(prefsPass, hashString(password));
        loginPrefsEditor.apply();
    }

    private void clearSaveLogin(){
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putBoolean(saveLoginString, false);
        loginPrefsEditor.apply();
    }

    private void editLoginAttempts(){
        attemptsText.setVisibility(View.VISIBLE);
        counter--;
        attemptsText.setText(String.format(Locale.getDefault(), "%d",counter));
        if (counter == 0) {
            loginButton.setEnabled(false);
        }
    }

    private void showLoginToast(String info){
        Toast.makeText(getApplicationContext(),info,Toast.LENGTH_SHORT).show();
    }

    public static String hashString(String string){
        String hashedString = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(SALT.getBytes("UTF-8"));
            byte[] bytes = md.digest(string.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedString = sb.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return hashedString;
    }
}