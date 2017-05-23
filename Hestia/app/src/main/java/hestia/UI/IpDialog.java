package hestia.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hestia.backend.NetworkHandler;
import hestia.backend.Cache;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class IpDialog extends HestiaDialog{
    private EditText ipField;
    private String ip;
    private NetworkHandler networkHandler;
    private Cache cache;
    private Pattern pattern;
    private Matcher matcher;
    private static final String ipSetTo = "Ip address set to : ";
    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";


    public IpDialog(Activity activity) {
        super(activity, R.layout.ip_dialog, "Set IP");
        this.networkHandler = NetworkHandler.getInstance();
        this.cache = Cache.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ipField = (EditText) findViewById(R.id.ip);
        ipField.setText(this.cache.getIp());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    void pressCancel() {
    }

    @Override
    void pressConfirm() {
        ip = ipField.getText().toString();
        if(checkIp(ip)) {
            cache.setIp(ip);
            networkHandler.updateDevices();
            Toast.makeText(getContext(),ipSetTo + cache.getIp() + ":"
                    + cache.getPort(),Toast.LENGTH_SHORT).show();

            //TODO give correct response from server after changing ip
            Toast.makeText(getContext(), "Server returned message: + serverMessage",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), R.string.invalidIp, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkIp(String ip){
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
