package hestia.UI.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.rugged.application.hestia.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hestia.backend.ServerCollectionsInteractor;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class IpDialog extends HestiaDialog {
    private EditText ipField;
    private ServerCollectionsInteractor serverCollectionsInteractor;
    private Pattern pattern;
    private Matcher matcher;
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    private final String INCORRECT_IP = "The IP address should be between 0.0.0.0 and 255.255.255.255";

    public IpDialog(Activity activity, ServerCollectionsInteractor serverCollectionsInteractor) {
        super(activity, R.layout.ip_dialog, "Set IP");
        this.serverCollectionsInteractor = serverCollectionsInteractor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipField = (EditText) findViewById(R.id.ip);
        if (this.serverCollectionsInteractor.getHandler().getIp()!=null){
            ipField.setText(this.serverCollectionsInteractor.getHandler().getIp());
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    void pressCancel() {}

    @Override
    void pressConfirm() {
        String ip = ipField.getText().toString();
        Log.d("IPDIALOG",ip);
        if(ip!=null&&checkIp(ip)) {
            serverCollectionsInteractor.getHandler().setIp(ip);
            // TODO refresh layout
            Toast.makeText(getContext(),R.string.ipSetTo + serverCollectionsInteractor.getHandler().getIp() + ":"
                    + serverCollectionsInteractor.getHandler().getPort(),Toast.LENGTH_SHORT).show();

            //TODO give correct response from server after changing ip
            Toast.makeText(getContext(), "Server returned message: + serverMessage",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), INCORRECT_IP,Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkIp(String ip) {
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
