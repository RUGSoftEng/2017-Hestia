package hestia.UI.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
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

    public static ChangeCredentialsDialog newInstance() {
        return new ChangeCredentialsDialog();
    }

    @Override
    String buildTitle() {
        return getString(R.string.changeCredsTitle);
    }

    @Override
    View buildView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.change_credentials_dialog, null);

        newUserField = (EditText) view.findViewById(R.id.newUser);
        newUserField.requestFocus();
        newPassField = (EditText) view.findViewById(R.id.newPass);
        newPassCheckField = (EditText) view.findViewById(R.id.newPassCheck);
        oldPassField = (EditText) view.findViewById(R.id.oldPass);

        return view;
    }

    @Override
    void pressConfirm() {
        String newUser = newUserField.getText().toString();
        String newPass = newPassField.getText().toString();
        String newPassCheck = newPassCheckField.getText().toString();
        String oldPass = oldPassField.getText().toString();
        loginPreferences = getActivity().getSharedPreferences(getString(R.string.loginPrefs)
                ,Context.MODE_PRIVATE);

        String feedback = "";
        if(checkOldPass(oldPass)){
            if(newPass.equals(newPassCheck) && !newPass.equals("")){
                setSharedPrefs(getString(R.string.loginPrefsPass
                ), hashString(newPass));
                feedback = getString(R.string.passSet) + newPass + "\n";
            } else{
                feedback = getString(R.string.passCheckWrong) + "\n";
            }
            if(newUser.length()>4){
                setSharedPrefs(getString(R.string.loginPrefsUser),hashString(newUser));
                feedback = feedback + getString(R.string.userSet) + newUser;
            } else {
                feedback = feedback + getString(R.string.userNotSet);
            }
            showToast(feedback);
        } else {
            showToast(getString(R.string.passOldWrong));
        }
        dismiss();
    }

    @Override
    void pressCancel() {
    }

    private boolean checkOldPass(String oldPass){
        String corrpass = loginPreferences.getString(getString(R.string.loginPrefsPass), "");
        return corrpass.equals(hashString(oldPass));
    }

    private void setSharedPrefs(String name, String value){
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putString(name, value);
        loginPrefsEditor.apply();
    }

    private void showToast(String text){
        Toast.makeText(getActivity(), text , Toast.LENGTH_LONG).show();
    }
}