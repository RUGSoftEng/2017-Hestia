package hestia.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;

/**
 * This class opens the dialog to change the username and password to login.
 * It uses some of the static variables and the static hashing function form LoginActivity.
 * @see LoginActivity
 */

public class ChangeLoginDialog extends Dialog implements View.OnClickListener {
    private EditText oldPassField, newPassField, newPassCheckField, newUserField;
    private Button confirm, cancel;
    private Context context;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private final String pass_old_wrong = "Old password is incorrect ";
    private final String pass_check_wrong = "Passwords did not match ";
    private final String pass_set = "Password set to : ";
    private final String user_set = "Username set to : ";
    private final String user_not_set = "Username not changed (length<5)";

    public ChangeLoginDialog(Context activity) {
        super(activity);
        this.context = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_login);
        newUserField = (EditText) findViewById(R.id.newUser);
        newUserField.requestFocus();
        newPassField = (EditText) findViewById(R.id.newPass);
        newPassCheckField = (EditText) findViewById(R.id.newPassCheck);
        oldPassField = (EditText) findViewById(R.id.oldPass);
        confirm = (Button) findViewById(R.id.confirm_button);
        cancel = (Button) findViewById(R.id.back_button);
        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String newUser = newUserField.getText().toString();
        String newPass = newPassField.getText().toString();
        String newPassCheck = newPassCheckField.getText().toString();
        String oldPass = oldPassField.getText().toString();
        loginPreferences = context.getSharedPreferences(LoginActivity.LOGIN_PREFERENCES
                , Context.MODE_PRIVATE);

        switch (view.getId()) {
            case R.id.confirm_button:
                String feedback = "";
                if(checkOldPass(oldPass)){
                    if(newPass.equals(newPassCheck) && !newPass.equals("")){
                        setSharedPrefs(LoginActivity.prefsPass,LoginActivity.hashPassword(newPass));
                        feedback = pass_set + newPass + "\n";
                    } else{
                        feedback = pass_check_wrong + "\n";
                    }
                    if(newUser.length()>4){
                        setSharedPrefs(LoginActivity.prefsUser,newUser);
                        feedback = feedback + user_set + newUser;
                    } else {
                        feedback = feedback + user_not_set;
                    }
                    showToast(feedback);
                } else {
                    showToast(pass_old_wrong);
                }
                break;
            case R.id.back_button:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private boolean checkOldPass(String oldPass){
        String corrpass = loginPreferences.getString(LoginActivity.prefsPass, "");
        return corrpass.equals(LoginActivity.hashPassword(oldPass));
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
