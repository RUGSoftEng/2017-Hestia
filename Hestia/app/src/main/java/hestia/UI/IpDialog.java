package hestia.UI;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import hestia.backend.NetworkHandler;
import hestia.backend.Cache;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class IpDialog extends HestiaDialog implements android.view.View.OnClickListener{
    private EditText ipField;
    private String ip;
    private NetworkHandler networkHandler;
    private Cache cache;

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
        dismiss();

    }

    @Override
    void pressConfirm() {
        ip = ipField.getText().toString();
        Toast.makeText(getContext(), "ip is: " + ip, Toast.LENGTH_SHORT).show();
        if(ip!=null) {
            cache.setIp(ip);
            networkHandler.updateDevices();
            Toast.makeText(getContext(),R.string.ipSetTo + cache.getIp() + ":"
                    + cache.getPort(),Toast.LENGTH_SHORT).show();

            //TODO give correct response from server after changing ip
            Toast.makeText(getContext(), "Server returned message: + serverMessage",
                    Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }
}
