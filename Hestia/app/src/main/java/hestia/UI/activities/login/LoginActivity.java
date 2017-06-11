package hestia.UI.activities.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private static final String SALT = "RuGg3Ds0ftWarE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent fromIntent = getIntent();
        String extra = fromIntent.getStringExtra(getString(R.string.loginIntentExtra));

        loginPreferences = getSharedPreferences(getString(R.string.loginPrefs), MODE_PRIVATE);
        if(loginPreferences.getString(getString(R.string.loginPrefsUser),"").equals("")){
            setSharedPreferences(getString(R.string.standardUser), getString(R.string.standardPass),false);
        }
        Boolean saveLogin = loginPreferences.getBoolean(getString(R.string.saveLogin), false);
        if (saveLogin) {
            if(extra==null) {
                gotoMainActivity();
            }
            else{
                clearSaveLogin();
            }
        }
        addWidgets();
    }

    private void addWidgets() {
        loginButton = (Button)findViewById(R.id.loginButton);
        userField = (EditText)findViewById(R.id.username);
        passField = (EditText)findViewById(R.id.password);
        rememberButton = (CheckBox) findViewById(R.id.rememberButton);
        attemptsText = (TextView)findViewById(R.id.textView4);
        attemptsText.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            /* Button has been clicked, get and check credentials */
            @Override
            public void onClick(View view) {
                username = userField.getText().toString();
                password = passField.getText().toString();
                if(checkCredentials(username,password)){
                    if (rememberButton.isChecked()) {
                        setSharedPreferences(username,password,true);
                    } else {
                        clearSaveLogin();
                    }
                    showLoginToast(getString(R.string.correctLoginToast));
                    gotoMainActivity();
                }else{
                    showLoginToast(getString(R.string.incorrectLoginToast));
                    editLoginAttempts();
                }
            }
        });
    }

    private boolean checkCredentials(String username,String password){
        String corrPass = loginPreferences.getString(getString(R.string.loginPrefsPass), "");
        String corrUser = loginPreferences.getString(getString(R.string.loginPrefsUser), "");
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
        loginPrefsEditor.putBoolean(getString(R.string.saveLogin), saveLogin);
        loginPrefsEditor.putString(getString(R.string.loginPrefsUser), hashString(username));
        loginPrefsEditor.putString(getString(R.string.loginPrefsPass), hashString(password));
        loginPrefsEditor.apply();
    }

    private void clearSaveLogin(){
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putBoolean(getString(R.string.saveLogin), false);
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