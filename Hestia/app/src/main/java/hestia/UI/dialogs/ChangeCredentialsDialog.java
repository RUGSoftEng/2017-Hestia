package hestia.UI.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import com.rugged.application.hestia.R;

import hestia.UI.activities.login.LoginActivity;

import static hestia.UI.activities.login.LoginActivity.hashString;

/**
 * This class opens the dialog to change the username and password to login.
 * It uses some of the static variables and the static hashing function form LoginActivity.
 * @see LoginActivity
 */

public class ChangeCredentialsDialog extends HestiaDialog {
    private EditText oldPassField, newPassField, newPassCheckField, newUserField;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private final String pass_old_wrong = "Old password is incorrect ";
    private final String pass_check_wrong = "New password not set ";
    private final String pass_set = "Password set to : ";
    private final String user_set = "Username set to : ";
    private final String user_not_set = "Username not changed (length<5)";

    public ChangeCredentialsDialog(Context context) {
        super(context, R.layout.change_credentials_dialog, "Change username/password");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newUserField = (EditText) findViewById(R.id.newUser);
        newUserField.requestFocus();
        newPassField = (EditText) findViewById(R.id.newPass);
        newPassCheckField = (EditText) findViewById(R.id.newPassCheck);
        oldPassField = (EditText) findViewById(R.id.oldPass);
    }

    @Override
    void pressConfirm() {
        String newUser = newUserField.getText().toString();
        String newPass = newPassField.getText().toString();
        String newPassCheck = newPassCheckField.getText().toString();
        String oldPass = oldPassField.getText().toString();
        loginPreferences = context.getSharedPreferences(LoginActivity.LOGIN_PREFERENCES
                , Context.MODE_PRIVATE);

        String feedback = "";
        if(checkOldPass(oldPass)){
            if(newPass.equals(newPassCheck) && !newPass.equals("")){
                setSharedPrefs(LoginActivity.prefsPass, hashString(newPass));
                feedback = pass_set + newPass + "\n";
            } else{
                feedback = pass_check_wrong + "\n";
            }
            if(newUser.length()>4){
                setSharedPrefs(LoginActivity.prefsUser,hashString(newUser));
                feedback = feedback + user_set + newUser;
            } else {
                feedback = feedback + user_not_set;
            }
            showToast(feedback);
        } else {
            showToast(pass_old_wrong);
        }
        dismiss();
    }

    private boolean checkOldPass(String oldPass){
        String corrpass = loginPreferences.getString(LoginActivity.prefsPass, "");
        return corrpass.equals(hashString(oldPass));
    }

    private void setSharedPrefs(String name, String value){
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putString(name, value);
        loginPrefsEditor.apply();
    }

    private void showToast(String text){
        Toast.makeText(context, text , Toast.LENGTH_LONG).show();
    }
}