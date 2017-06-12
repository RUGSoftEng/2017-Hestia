package hestia.UI.dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import hestia.UI.activities.login.LoginActivity;

import static com.rugged.application.hestia.R.string.standardPass;
import static hestia.UI.activities.login.LoginActivity.hashString;

/**
 * This class opens the dialog to change the username and password to login.
 * It uses some of the static variables and the static hashing function from the LoginActivity.
 *
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

    /**
     * This method is called when the confirm button is pressed on the dialog. We store the new
     * password and username in variables. Next we check whether the credentials are admissible,
     * after first verifying that the old password which was provided is correct.
     */
    @Override
    void pressConfirm() {
        String newUser = newUserField.getText().toString();
        String newPass = newPassField.getText().toString();
        String newPassCheck = newPassCheckField.getText().toString();
        String oldPass = oldPassField.getText().toString();
        loginPreferences = getActivity().getSharedPreferences(getString(R.string.loginPrefs)
                , Context.MODE_PRIVATE);

        String feedback = "";
        if(checkOldPass(oldPass)){
            if(newPass.equals(newPassCheck) && !newPass.equals("") && newUser.length()>4){
                setSharedPrefs(getString(R.string.loginPrefsPass), hashString(newPass));
                setSharedPrefs(getString(R.string.loginPrefsUser),hashString(newUser));
                feedback = getString(R.string.passSet);
            } else {
                feedback = getString(R.string.passCheckWrong);
            }
            showToast(feedback);
        } else {
            showToast(getString(R.string.passOldWrong));
        }
        dismiss();
    }

    /**
     * This method displays a simple message if the cancel button is pressed.
     */
    @Override
    void pressCancel() {
        refreshUserInterface();
    }

    private boolean checkOldPass(String oldPass) {
        String corrpass = loginPreferences.getString(getString(R.string.loginPrefsPass), hashString(getString(standardPass)));
        return corrpass.equals(hashString(oldPass));
    }

    /**
     * A method for changing the value of a specific resource stored in the system.
     *
     * @param name  The key of the string as it is stored in the system
     * @param value The new value for the string in the system
     */
    private void setSharedPrefs(String name, String value) {
        SharedPreferences.Editor loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putString(name, value);
        loginPrefsEditor.apply();
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }
}